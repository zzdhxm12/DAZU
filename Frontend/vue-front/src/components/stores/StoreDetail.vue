<template>
  <div>
    <div class="cart__discount">
      <h6>Store Detail</h6>

      <v-dialog v-model="dialog" persistent max-width="290">
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            style="
              font-size: 14px;
              color: #ffffff;
              background: #111111;
              font-weight: 600;
              border: none;
              text-transform: uppercase;
              display: inline-block;
              letter-spacing: 2px;
              padding: 14px 30px;
            "
            dark
            tile
            large
            block
            v-bind="attrs"
            v-on="on"
          >
            편집
          </v-btn>
        </template>
        <v-card>
          <v-card-title class="headline"> 가게 상세 설명 편집 </v-card-title>
          <v-card-text
            ><v-file-input
              v-model="selectfile"
              truncate-length="15"
              label="새로운 가게 상세 설명"
            ></v-file-input
          ></v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="putStoreDetail">
              SAVE
            </v-btn>
            <v-btn color="green darken-1" text @click="dialog = false">
              CLOSE
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script>
import { updateStoreDetail } from '@/api/store';
export default {
  data() {
    return {
      selectfile: null,
      dialog: false,
      message1: '기존값연결',
      imgsrc: '',
      id: Number,
    };
  },
  props: {
    storeData: Object,
  },
  created() {
    // const id = this.storeData.storecode;
    // console.log(this.imgsrc);
    this.imgsrc =
      'https://i4d104.p.ssafy.io/dazu/file/image/' +
      this.$store.state.storecode +
      '_store_intro';
    console.log(this.imgsrc);
  },
  computed: {},
  methods: {
    changeDialog() {
      this.dialog = false;
    },
    async putStoreDetail() {
      this.id = this.storeData.storecode;
      var fd = new FormData();
      fd.append('file', this.selectfile);
      console.log(this.selectfile);
      console.log(this.id);
      fd.append('key', this.id);
      await updateStoreDetail(fd);
      this.changeDialog();
      this.$router.go(this.$router.currentRoute);
    },
  },
};
</script>

<style></style>
