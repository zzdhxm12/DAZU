<template>
  <div>
    <h6 class="coupon__code">
      <span class="icon_tag_alt"></span> Check your class and schedule
    </h6>
    <h6 class="checkout__title">My Information</h6>
    <div class="row">
      <div class="col-lg-6">
        <div class="checkout__input">
          <p>Profile Image</p>
          <v-avatar size="230" style="border:1px solid #f08632;">
            <img :src="userImgUrl" alt="this.$store.username" />
          </v-avatar>
        </div>
      </div>
      <div class="col-lg-6">
        <div class="checkout__input">
          <p>Nickname<span>*</span></p>
          <input type="text" v-model="customerData.nickname" readonly />
        </div>
      </div>
    </div>
    <br />
    <div class="checkout__input">
      <p>Address<span>*</span></p>
      <input
        type="text"
        placeholder="Street Address"
        class="checkout__input__add"
        v-model="customerData.address"
      />
      <input
        type="text"
        placeholder="Apartment, suite, unite ect (optinal)"
        v-model="customerData.address_detail"
      />
    </div>
    <div class="checkout__input">
      <p>Dong</p>
      <input type="text" readonly v-model="customerData.dong" />
    </div>

    <v-dialog v-model="dialog" persistent max-width="600px">
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          tile
          elevation="0"
          large
          style="display: inline-block;font-size: 14px;font-weight: 600;text-transform: uppercase;padding: 14px 30px;color: #ffffff;background: #f08632;letter-spacing: 2px;"
          v-bind="attrs"
          v-on="on"
          block
        >
          주소변경
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">주소변경</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <div
                ref="searchWindow"
                :style="searchWindow"
                style="border:1px solid;width:500px;margin:5px 0;position:relative"
              >
                <img
                  src="//t1.daumcdn.net/postcode/resource/images/close.png"
                  id="btnFoldWrap"
                  style="cursor:pointer;position:absolute;right:0px;top:-1px;z-index:1"
                  @click="searchWindow.display = 'none'"
                  alt="close"
                />
              </div>
              <v-col cols="12">
                <input type="text" placeholder="우편번호" v-model="postcode"/>
                <v-btn
                  class="ma-2"
                  tile
                  large
                  style="font-size: 14px;color: #ffffff;background: #111111;font-weight: 600;border: none;text-transform: uppercase;display: inline-block;letter-spacing: 2px;padding: 14px 30px;"
                  value="우편번호 찾기"
                  @click="execDaumPostcode"
                  >우편번호 찾기</v-btn
                >
                <br
              /></v-col>
              <v-col cols="12">
                <v-text-field
                  type="text"
                  v-model="address"
                  placeholder="주소"
                  readonly
                />
                <br />
              </v-col>
              <v-col cols="12">
                <v-text-field
                  type="text"
                  v-model="dong"
                  placeholder="동"
                  readonly
                />
                <v-text-field
                  type="text"
                  v-model="extraAddress"
                  ref="extraAddress"
                  placeholder="상세주소"
                  readonly
                />
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="dialog = false">
            Close
          </v-btn>
          <v-btn text @click="submitInfo">
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <br /><br />
    <v-btn
      tile
      elevation="0"
      large
      style="font-size: 14px;color: #ffffff;background: #111111;font-weight: 600;border: none;text-transform: uppercase;display: inline-block;letter-spacing: 2px;padding: 14px 30px;"
      block
      @click="deleteUser"
    >
      회원탈퇴
    </v-btn>
  </div>
</template>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
import { deleteUser, logoutUser } from '@/api/auth';
import { deleteCookie } from '@/utils/cookies';
import { MypageCustomerInfo, MypageCustomerEditAddress } from '@/api/mypage';

export default {
  props: {
    customerData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      searchWindow: {
        display: 'none',
        height: '300px',
      },
      dialog: false,
      postcode: '',
      address: '',
      extraAddress: '',
      dong: '',
      usertype: '1',
    };
  },
  computed: {
    userImgUrl() {
      return this.$store.state.userimg;
    },
    userName() {
      return this.$store.state.username;
    },
  },
  methods: {
    execDaumPostcode() {
      const currentScroll = Math.max(
        document.body.scrollTop,
        document.documentElement.scrollTop,
      );

      new daum.Postcode({
        onComplete: data => {
          if (data.userSelectedType === 'R') {
            this.address = data.roadAddress;
          } else {
            this.address = data.jibunAddress;
          }
          if (data.userSelectedType === 'R') {
            if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
              this.dong = data.bname;
            }
            if (data.buildingName !== '' && data.apartment === 'Y') {
              this.dong +=
                this.dong !== '' ? `, ${data.buildingName}` : data.buildingName;
            }
            if (this.dong !== '') {
              this.dong = ` ${this.dong}`;
            }
          } else {
            this.extraAddress = '';
          }
          this.postcode = data.zonecode;
          this.$refs.extraAddress.focus();
          this.searchWindow.display = 'none';
          document.body.scrollTop = currentScroll;
        },
        onResize: size => {
          this.searchWindow.height = `${size.height}px`;
        },
        width: '100%',
        height: '100%',
      }).embed(this.$refs.searchWindow);
      this.searchWindow.display = 'block';
    },

    async submitInfo() {
      try {
        console.log('data3' + this.dong);
        const extrainfo = {
          dong: this.dong,
          usertype: this.usertype,
        };
        const response = await MypageCustomerEditAddress({
          accessToken: '',
          address: this.address,
          address_detail: this.extraAddress,
          usercode: this.$store.state.usercode,
          create_date: '',
          lat: '',
          lng: '',
          dong: this.dong,
          nickname: this.$store.state.username,
          usertype: this.usertype,
        });
        this.$store.dispatch('EXTRAINFO', extrainfo);
        this.$router.go(this.$router.currentRoute);
        console.log('고객님 주소변경 완료');
      } catch (error) {
        console.log(error);
      }
    },
    async deleteUser() {
      const response = await deleteUser(
        this.$store.state.usercode,
        this.$store.state.token,
      );
      console.log(response);
      this.token = this.$store.state.token;
      console.log(this.token);
      await logoutUser(this.token);
      this.$store.commit('clearUsername');
      this.$store.commit('clearToken');
      this.$store.commit('clearUserimg');
      this.$store.commit('clearUserCode');
      this.$store.commit('clearUserType');
      this.$store.commit('clearStoreCode');
      this.$store.commit('clearDong');
      deleteCookie('til_auth');
      deleteCookie('til_user');
      deleteCookie('til_img');
      deleteCookie('til_usercode');
      deleteCookie('til_usertype');
      deleteCookie('til_storecode');
      deleteCookie('til_dong');
      this.$router.push('/user');
    },
  },
  async created() {
    const res = await MypageCustomerInfo(this.$store.state.usercode);
    console.log(res.data);
    this.address = res.data.address;
    this.dong = res.data.dong;
    this.extraAddress = res.data.address_detail;
  },
};
</script>

<style>
.customer-mypage {
  display: flex;
  align-items: center;
}
.customer-info {
  padding: 40px;
}
</style>
