<template>
  <div id="app">
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <b-container fluid>
      <b-tabs>
        <b-tab title="데이터입력">
          <First/>
        </b-tab>
        <b-tab title="예측결과">
          <Second/>
        </b-tab>
        <!-- <b-tab title="예측결과1" >
          <Counter/>
        </b-tab> -->
      </b-tabs>
    </b-container>
  </div>
</template>

<script>
import First from "./components/First.vue";
import Second from "./components/Second.vue";

export default {
  name: "app",
  components: {
    First,
    Second
  },
  data() {
    return {
      tab_el: null
    }
  }
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
