
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
  chartData: []
}

const mutations = {
  updateChartData (state, chartData) {
    state.chartData = chartData
  }
}

const actions = {
  updateChartData: ({ commit }, chartData) => {
    return commit('updateChartData', chartData)
  }
}

const getters = {
  chartData: state => {
    return state.chartData
  }
}

export default new Vuex.Store({
  state,
  getters,
  actions,
  mutations
})