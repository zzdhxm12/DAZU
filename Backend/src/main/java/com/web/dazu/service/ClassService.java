package com.web.dazu.service;

import java.util.List;

import com.web.dazu.model.Class;
import com.web.dazu.model.ClassNotice;
import com.web.dazu.model.ClassReview;
import com.web.dazu.model.ClassRoom;
import com.web.dazu.model.ClassTime;

public interface ClassService {

	List<Class> selectAllClass(String dong) throws Exception;

	Class selectClass(String classcode) throws Exception;

	ClassTime selectClassTime(String classcode) throws Exception;

	List<Class> selectClassByStore(String storecode) throws Exception;

	List<ClassReview> selectClassReview(String classcode) throws Exception;

	List<ClassNotice> selectClassNotice(String classcode) throws Exception;

	void insertClassNotice(ClassNotice classNotice) throws Exception;

	void insertClassReview(ClassReview classreview) throws Exception;

	ClassNotice selectClassNoticeDetail(String class_notice_board_code) throws Exception;

	void insertClassRoom(ClassRoom room) throws Exception;

}
