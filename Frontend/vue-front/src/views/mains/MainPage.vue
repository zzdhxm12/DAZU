<template>
  <div>
    <div>
      <ComingClass></ComingClass>
    </div>

    <div>
      <br /><br />
      <v-card elevation="0">
        <v-tabs
          class="tabs"
          v-model="tab"
          centered
          icons-and-text
          color="orange accent-4"
        >
          <v-tab class="tab" v-for="item in items" :key="item.tab">
            <div class="categories__item">
              <div class="categories__item__icon">
                <span :class="item.icon"></span>
                <div>{{ item.tab }}</div>
              </div>
            </div>
          </v-tab>
        </v-tabs>

        <section class="testimonial spad">
          <div class="container">
            <div class="row">
              <div class="col-lg-12 text-center ttt">
                <div class="section-title">
                  <span>Our Neighborhood</span>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-lg-12 text-center">
                <div class="section-title">
                  <v-tabs-items v-model="tab">
                    <v-tab-item v-for="item in items" :key="item.tab">
                      <div flat v-if="item.tab === '지도'">
                        <MainMap></MainMap>
                      </div>
                      <div flat v-else-if="item.tab === '목록'">
                        <MainClassList></MainClassList>
                      </div>
                    </v-tab-item>
                  </v-tabs-items>
                </div>
              </div>
            </div>
          </div>
        </section>
      </v-card>
    </div>
  </div>
</template>

<script>
import '@/assets/css/style.css';

import ComingClass from '@/components/classes/ComingClass.vue';
import MainMap from '@/components/mains/MainMap.vue';
import MainClassList from '@/components/mains/MainClassList.vue';
export default {
  data() {
    return {
      isLoading: false,
      tab: null,
      items: [
        {
          tab: '지도',
          icon: 'flaticon-034-chocolate-roll',
        },
        {
          tab: '목록',
          icon: 'flaticon-029-cupcake-3',
        },
      ],
    };
  },
  components: {
    ComingClass,
    MainClassList,
    MainMap,
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

<style scoped>
.tabs {
  height: 110px;
}
.tab {
  height: 3.5rem;
}
.testimonial {
  background: #fdf3ea;
  padding-top: 60px;
  padding-bottom: 0px;
}
</style>
