<template>
  <div>
    <div class="breadcrumb-option">
      <div class="container">
        <div class="row">
          <div class="col-lg-6 col-md-6 col-sm-6">
            <div class="breadcrumb__text">
              <h2>Notice</h2>
            </div>
          </div>
        </div>
      </div>
    </div>
    <hr />
    <br />
    <NoticeListTable></NoticeListTable>
  </div>
</template>
<script>
import NoticeListTable from '@/components/notice/NoticeListTable.vue';
export default {
  components: {
    NoticeListTable,
  },
  created() {
    if (!this.$store.getters.isLogin) {
      console.log('goto loginBtn');
      this.$router.push('/user');
    } else {
      this.$store.state.header = '1';
    }
  },
};
</script>
<style scope></style>
