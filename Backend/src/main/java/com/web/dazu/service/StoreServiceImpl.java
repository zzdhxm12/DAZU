package com.web.dazu.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import com.web.dazu.mapper.StoreMapper;
import com.web.dazu.model.ClassReview;
import com.web.dazu.model.Store;

@Service
public class StoreServiceImpl implements StoreService {
	@Autowired
	private FileUploadService fileuploadservice;
	@Autowired
	private SqlSession session;

	@Override
	public List<Store> selectAllStore(String dong) throws Exception {
		return session.getMapper(StoreMapper.class).selectAllStore(dong);
	}

	@Override
	public void insertStore(List<MultipartFile> file,Store store) throws Exception {
		System.out.println("서비스 진입");
		fileuploadservice.fileup(file, "store");
		String apiKey = "2ce9bedc0889520f06b58f54d0724e65";
	    String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
	    String jsonString = null;
	    System.out.println("서비스 진입2");
	    String addrutf = URLEncoder.encode(store.getStore_location(), "UTF-8");

        String addr = apiUrl + "?query=" + addrutf;
        System.out.println("서비스 진입3");
        URL url = new URL(addr);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);
        System.out.println("서비스 진입4");
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        System.out.println("서비스 진입5");
        String line = "";
        String result = "";
        System.out.println("서비스 진입6");
        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println(result);
        jsonString = result.toString();
        System.out.println("서비스 진입7"); 
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> XYMap = new HashMap<String, String>();
		System.out.println("서비스 진입8");
		TypeReference<Map<String, Object>> typeRef 
        = new TypeReference<Map<String, Object>>(){};
	    Map<String, Object> jsonMap = mapper.readValue(jsonString, typeRef);
	    System.out.println("서비스 진입9");
	    @SuppressWarnings("unchecked")
	    List<Map<String, String>> docList 
	        =  (List<Map<String, String>>) jsonMap.get("documents");	
	    System.out.println("서비스 진입10");
	    Map<String, String> adList = (Map<String, String>) docList.get(0);
	    XYMap.put("x",adList.get("x"));
	    XYMap.put("y", adList.get("y"));
	    System.out.println("서비스 진입11");
		store.setLat(XYMap.get("x"));
		store.setLng(XYMap.get("y"));
		System.out.println("서비스 진입12");
		System.out.println(store.getLat() + store.getLng());
		System.out.println("서비스 진입13");
	    session.getMapper(StoreMapper.class).insertStore(store);
	}

	@Override
	public void updateStoreGrade(ClassReview review) throws Exception {
		session.getMapper(StoreMapper.class).updateStoreGrade(review);
	}

	@Override
	public Store selectStore(String usercode) throws Exception {
		return session.getMapper(StoreMapper.class).selectStore(usercode);
	}

}
