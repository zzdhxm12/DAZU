<template>
  <div>
    <v-img :src="imgsrc"></v-img>
  </div>
</template>

<script>
import { fetchClass } from '@/api/classes';

export default {
  data() {
    return {
      classcode: '',
      imgsrc: '',
    };
  },

  async mounted() {
    const id = this.$route.params.id;
    // console.log(id);
    const { data } = await fetchClass(id);
    console.log('클래스 소개 mounted:  ' + data.classcode);
    this.classcode = data.classcode;
    this.imgsrc =
      'https://i4d104.p.ssafy.io/dazu/file/image/' +
      this.classcode +
      '_class_intro';
    console.log('클래스 코드 들어간 src' + this.imgsrc);
  },
};
</script>

<style></style>
