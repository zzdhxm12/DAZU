<template>
  <div>
    <v-col cols="12" class="text-center">
      <vc-date-picker :attributes="attributes" v-model="date">
        <div slot="day-popover" slot-scope="{ dayTitle, attributes }">
          <div class="text-xs text-gray-300 font-semibold text-center">
            {{ dayTitle }}
          </div>
          <ul>
            <li v-for="{ key, customData } in attributes" :key="key">
              {{ customData.class_timecode }}
              {{ customData.class_starttime.slice(0, 5) }}
            </li>
          </ul>
        </div>
      </vc-date-picker>
    </v-col>
    <v-col cols="12">
      <v-select
        :items="classTimeList"
        label="시간 선택"
        dense
        solo
        :disabled="disabled == 1"
        v-model="timecode"
      ></v-select>
    </v-col>
    <ul class="checkout__total__all">
      <li v-if="mealkitchoice === '밀키트 포함' && timecode !== ''">
        Total
        <span>₩ {{ classData.class_price + classData.mealkit_price }}</span>
      </li>
      <li v-else-if="mealkitchoice === 'Only 밀키트' && timecode !== ''">
        Total
        <span>₩ {{ classData.mealkit_price }}</span>
      </li>
      <li v-else>
        Total <span v-if="timecode !== ''"> ₩ {{ classData.class_price }}</span>
      </li>
    </ul>
    <button class="site-btn" @click="onClickKakaoPayAPI">
      ORDER - KAKAO PAY
    </button>
  </div>
</template>

<script>
import { fetchClassTime, postClassTime } from '@/api/classes';
// postClassTime
import { postKAKAOPAYReady } from '@/api/classes';

export default {
  props: {
    mealkitchoice: {
      type: String,
    },
    disabled: {
      type: String,
      required: true,
    },
    classData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      date: new Date(),
      classDataList: [],
      classTimeList: [],
      timecode: '',
    };
  },
  computed: {
    attributes() {
      return [
        // Attributes for todos
        ...this.classDataList.map(todo => ({
          dates: new Date(
            parseInt(todo.class_date.slice(0, 4)),
            parseInt(todo.class_date.slice(5, 7)) - 1,
            parseInt(todo.class_date.slice(8, 10)),
          ),
          dot: {
            color: 'orange',
            class: 'opacity-75',
          },
          popover: {
            slot: 'day-popover',
            label: todo.class_starttime.slice(0, 5) + todo.class_timecode,
            visibility: 'focus',
            positionFixed: true,
          },
          customData: todo,
        })),
      ];
    },
  },
  methods: {
    async onClickKakaoPayAPI() {
      console.log('클릭페이버튼!!!!!!');
      const id = this.$store.state.usercode;
      console.log(
        this.classData.class_name +
          ' ' +
          this.classData.class_price +
          ' ' +
          this.classData.classcode +
          ' ' +
          id,
      );
      const { data } = await postKAKAOPAYReady({
        item_name: this.classData.class_name,
        total_amount: this.classData.class_price,
        class_information_classcode: this.classData.classcode,
        class_time_information_class_timecode: this.timecode.slice(0, 2),
        member_usercode: id,
      });
      console.log(data);
      window.open(data);
    },
    async submitTime() {
      const Tcode = this.timecode.slice(0, 2);
      console.log(Tcode);
      const id = this.$route.params.id;
      console.log(id);
      const res = await postClassTime({
        class_information_classcode: id,
        class_time_information_class_timecode: Tcode,
        member_usercode: this.$store.state.usercode,
      });
      console.log(res);
    },
  },

  async created() {
    const id = this.$route.params.id;
    console.log(id);

    // const resu = await fetchMealkit(id);

    const res = await fetchClassTime(id);
    this.classDataList = res.data;
    console.log(this.classDataList);
    for (var key in res.data) {
      const value =
        res.data[key].class_timecode +
        ' ' +
        res.data[key].class_date.slice(0, 4) +
        '-' +
        res.data[key].class_date.slice(5, 7) +
        '-' +
        res.data[key].class_date.slice(8, 10) +
        ' ' +
        res.data[key].class_starttime.slice(0, 5);
      this.classTimeList.push(value);
    }
  },
};
</script>

<style scoped>
.submit {
  margin-top: 10px;
}
</style>
