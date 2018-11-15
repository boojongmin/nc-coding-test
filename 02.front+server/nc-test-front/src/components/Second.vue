<template>
  <div>
    <b-row>
      <b-col cols="12">
        <Chart />
      </b-col>
    </b-row>
    <b-row>
      <b-table striped hover :items="chartData" :fields="fields"></b-table>
    </b-row>
  </div>
</template>

<script>
import Chart from './Chart.vue'

import { mapGetters } from 'vuex'

export default {
  name: 'Second',
  components: {
    Chart
  },
  props: {
  },
  data() {
    return {
      fields: ['time',	'anormal_flag', 'value',' fit', 'lwr', 'upr', 'residual', 'cumul_lwr', 'cumul_upr', 'cumulative_sum_of_residual']
    }
  },
  computed: {
    ...mapGetters(['chartData'])
  }
}
</script>

<style scoped>
</style>
