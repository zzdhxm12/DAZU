<template>
  <div>
    <v-card :loading="loading" class="mx-auto my-12" max-width="900">
      <template slot="progress">
        <v-progress-linear
          color="deep-purple"
          height="10"
          indeterminate
        ></v-progress-linear>
      </template>

      <v-card-title>
        <div class="card-title-container">
          <span>{{ detailData.board_title }}</span>
          <span class="grey--text"
            >(조회수 : {{ detailData.board_visit }})</span
          >
        </div>
      </v-card-title>

      <v-card-text>
        <div class="writer-info">
          <div class="my-4 subtitle-1 datetime">
            {{ detailData.board_write_datetime }}
          </div>
          <div class="my-4 subtitle-1">작성자 : {{ detailData.nickname }}</div>
        </div>
        <v-sheet
          class="checkout__title"
          font-size="30px"
          color="grey lighten-4"
          elevation="1"
          height="300"
          width="800"
        >
          <div class="blog__details__text">
            <p>
              {{ detailData.board_contents }}
            </p>
          </div>
        </v-sheet>
      </v-card-text>
      <v-row class="btn-container">
        <TipUpdate :boardId="boardId"></TipUpdate>
        <v-btn
          v-if="this.detailData.member_usercode === this.$store.state.usercode"
          @click="deleteThisTip()"
          >삭제</v-btn
        >
      </v-row>
      <v-divider class="mx-4"></v-divider>
      <div class="comment-container">
        <v-card-title>댓글</v-card-title>

        <v-col cols="8">
          <v-row>
            <v-text-field
              label="댓글을 입력하세요."
              outlined
              clearable
              v-model="commentInput"
              @keydown.enter="postComment"
            ></v-text-field>
            <v-btn outlined color="indigo" height="55px" @click="postComment"
              >입력</v-btn
            >
          </v-row>
        </v-col>
        <v-card-text>
          <div v-for="comment in commentList" :key="comment">
            <p>{{ comment.nickname }}({{ comment.comment_write_datetime }})</p>

            <p>{{ comment.comment_contents }}</p>
            <v-divider></v-divider>
          </div>
        </v-card-text>
      </div>
    </v-card>
    <v-row class="justify-content-center">
      <v-btn
        tile
        elevation="0"
        large
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
        @click="goList"
        >목록</v-btn
      >
    </v-row>
  </div>
</template>

<script>
import { getTipDetail } from '@/api/community';
import { getTipDetailComment } from '@/api/community';
import { postTipComment } from '@/api/community';
import { deleteTip } from '@/api/community';
import TipUpdate from '@/components/community/TipUpdate.vue';
export default {
  components: {
    TipUpdate,
  },
  data() {
    return {
      detailData: [],
      commentList: [],
      commentInput: '',
      boardId: Number,
    };
  },
  async created() {
    if (!this.$store.getters.isLogin) {
      console.log('goto loginBtn');
      this.$router.push('/user');
    } else {
      this.$store.state.header = '1';
    }
    const id = this.$route.params.id;
    this.boardId = id;
    const tipDetail = await getTipDetail(id);
    const tipComment = await getTipDetailComment(id);
    this.detailData = tipDetail.data;
    this.commentList = tipComment.data;
  },
  methods: {
    async postComment() {
      await postTipComment({
        board_code: this.$route.params.id,
        comment_code: '',
        comment_contents: this.commentInput,
        comment_write_datetime: '',
        member_usercode: this.$store.state.usercode,
      });
      this.$router.go(this.$router.currentRoute);
    },
    goList() {
      this.$router.push(`/community`);
    },
    async deleteThisTip() {
      await deleteTip(this.boardId);
      this.$router.push(`/community`);
    },
    // async deleteComment(data) {
    //   console.log(typeof data.comment_code);
    //   await deleteTipComment(Number(data.comment_code));
    // },
  },
};
</script>

<style scoped>
.btn-container {
  margin-left: 1.2rem;
  display: flex;
  width: 30%;
  justify-content: space-evenly;
  align-items: flex-end;
}
.card-title-container {
  margin-left: auto;
  margin-right: auto;
}
.checkout__title {
  font-size: 1.2rem;
  padding: 0.5rem;
  margin-left: auto;
  margin-right: auto;
}
.writer-info {
  display: flex;
  margin-right: 3rem;
  justify-content: flex-end;
}
.my-4 {
  margin-right: 0.5rem;
}
.comment-container {
  padding: 1rem;
  margin-left: 1.3rem;
}
.blog__details__text {
  padding: 1rem;
}
</style>
