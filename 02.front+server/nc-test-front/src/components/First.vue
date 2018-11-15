<template>
  <b-card title="파일입력">
    <b-row>
      <b-col cols="6">
        <b-form-group>
          <b-form-file v-model="file" id="test_data_file" name="test_data_file"></b-form-file>
            <div class="mt-3">Selected file: {{file && file.name}}</div>
        </b-form-group>
      </b-col>
      <b-col cols="6">
        <b-row>
          <b-col cols="6">
            <b-button class="btn-block" variant="primary" @click="drawTable">보기</b-button>
          </b-col>
          <b-col cols="6">
            <b-button class="btn-block" variant="success" @click="sendCsv">입력</b-button>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
    <b-row>
      <b-table v-show="csvData.length > 0" striped hover :items="csvData" :fields="fields"></b-table>
    </b-row>
  </b-card>
</template>

<script>
import { mapActions } from 'vuex'

function trimAndremoveDoubleQuote(text) {
  if(!text) return undefined
  return text.trim().split('"')[1];
}

 function sendCsvToServer(file, tab, updateChartData) {
    var httpRequest= new XMLHttpRequest();
    var formData = new FormData()
    formData.append('test_data_file', file);
    //local api server
    httpRequest.open('POST', 'http://localhost:8080/upload', true);
    // nc api server
    // httpRequest.open('POST', 'http://35.194.117.145:8080/upload_test_data', true);
    httpRequest.send(formData);

    httpRequest.onload = function(e) {
        var text = e.target.response;
        var json = JSON.parse(text);
        updateChartData(json)
        // drawChart(json);
        tab.click();
    }

    httpRequest.onerror = function() {
        alert('csv 파일을 처리중 오류가 발생했습니다.');
    }
}

export default {
  name: 'First',
  data() {
    return {
      file: null,
      fields: [ 'time', 'value', 'event1', 'event2' ],
      csvData: []
    }
  },
  methods: {
    drawTable() {
      var fileReader = new FileReader();
      fileReader.onloadend = (e2) => {
          const text = e2.target.result;
          const textArr = text.split("\n")
          //header 버림.
          textArr.shift(1)
          this.csvData = textArr.map(x => x.split(','))
            .map(x => {
              return {
                time: trimAndremoveDoubleQuote(x[0]),
                value: x[1],
                event1: trimAndremoveDoubleQuote(x[2]),
                event2: trimAndremoveDoubleQuote(x[3])
              }
            })
            .filter(x => x.value != undefined);
      }
      fileReader.readAsText(this.file);
    },
    sendCsv() {
      if(this.file == undefined) {
        alert('csv 파일을 입력해주세요.')
        return;
      }
      const tab = this.$parent.$parent.$el.querySelectorAll('a')[1]
      sendCsvToServer(this.file, tab, this.updateChartData);
    },
    ...mapActions(['updateChartData'])
  },
  mounted() {
    window.aa = this
  }
}
</script>

<style scoped>
</style>
