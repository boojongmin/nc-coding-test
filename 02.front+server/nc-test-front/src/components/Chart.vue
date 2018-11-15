<template>
  <div id="chart">
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import * as d3 from 'd3'

export default {
  name: 'Chart',
  computed: mapGetters([
    'chartData'
  ]),
  watch: {
    chartData: (value) => {
      drawCahrt(value)
    }
  }
}

function drawCahrt(data) {
  // init
  const chart = document.getElementById('chart');
  if(chart.firstChild)
    chart.removeChild(chart.firstChild)
  // chart
    var chartData = [];
    chartData.push(data.map(function (d) { return { "year": new Date(d.time), value: d.value } }));
    chartData.push(data.map(function (d) { return { "year": new Date(d.time), value: d.fit } }));
    chartData.push(data.map(function (d) { return { "year": new Date(d.time), value: d.lwr } }));
    chartData.push(data.map(function (d) { return { "year": new Date(d.time), value: d.upr } }));

    var detectResults = data.map(d => d.anormal_flag === 'normal'? true : false)

    var margin = { top: 20, right: 200, bottom: 30, left: 100 };
    var width = 1240 - margin.left - margin.right;
    var height = 500 - margin.top - margin.bottom;
    var xScale = d3.scaleTime().range([0, width]);
    var yScale = d3.scaleLinear().range([height, 0])

    var line = d3.line()
        .x(d => xScale(d.year))
        .y(d => yScale(d.value));

    xScale.domain(d3.extent(chartData[0], d => d.year))
    yScale.domain([0, d3.max(chartData[0], d => d.value)]);


    var svg = d3.select("#chart").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .style("border", "solid 1px black")
        .append("g").attr("transform", `translate(${margin.left},${margin.top})`);

    var color = d3.scaleOrdinal(d3.schemeCategory10);

    var lines = svg.append('g').attr('class', 'lines');
    lines.selectAll('.line-group')
        .data(chartData)
        .enter()
        .append('path')
        .attr('class', 'line')
        .attr('d', d => line(d))
        .style('stroke', (d, i) => color(i))
        .style("stroke-dasharray", (d, i) => i == 0 ? ("1, 0") : ("3, 3"))
    
        
    lines.selectAll("circle-group")
        .data(detectResults)
        .enter()
        .append("text")
        .text('!')
        .attr("class", "circle")
        .attr("x", (d, i) => xScale(chartData[0][i].year) + 5)
        .attr("y", (d, i) => yScale(chartData[0][i].value) + 5)
        .attr("fill", "blue")
        .attr("font-size", d => d ? 0 : 30)

    lines.selectAll("circle-group")
        .data(chartData[0])
        .enter()
        // .filter((d, i) => isValidEvent(d.value, i) == false)
        .append("text")
        .text('!')
        .attr("class", "circle")
        .attr("x", d => xScale(d.year) - 5)
        .attr("y", d => yScale(d.value))
        .attr("fill", "red")
        .attr("font-size", (d, i) => isValidEvent(d.value, i) ? 0 : 30  )

    function isValidEvent(value, i) {
        try {
            if (value < chartData[2][i].value) return false;
            if (value > chartData[3][i].value) return false;
        } catch(e) {
            return false;
        }
        return true;
    }

    // Add the X Axis
    svg.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call( d3.axisBottom(xScale).tickFormat(d3.timeFormat("%Y-%m-%d")));

    // Add the Y Axis
    svg.append("g")
        .call(d3.axisLeft(yScale));

    var index = ['value', 'fix', 'lwr', 'upr']
    svg
        .append("g")
        .selectAll("line")
        .data(index)
        .enter()
        .append("line")
        .attr("x1", width + 30)
        .attr("x2", width + 70)
        .attr("y1", (d, i) => 95 + (25 * i))
        .attr("y2", (d, i) => 95 + (25 * i))
        .attr("stroke", (d, i) => color(i))
        .style("stroke-dasharray", (d, i) => i == 0 ? ("1, 0") : ("3, 3"))

    svg
        .append("g")
        .selectAll("text")
        .data(index)
        .enter()
        .append("text")
        .attr("x", width + 80)
        .attr("y", (d, i) => 100 + (25 * i))
        .attr("width", 100)
        .attr("height", 100)
        .attr("font-family", "sans-serif")
        .attr("font-size", 12)
        .attr("fill", "red")
        .text(d => {
            return d;
        });
}
</script>

<style>
svg {
      font-family: Sans-Serif, Arial;
    }
    
    /* .line {
      stroke-width: 2;
      fill: none;
    } */
    .line {
      fill: none;
      stroke: green;
      stroke-width: 1px;
    }

    .axis path {
      stroke: black;
    }

    .text {
      font-size: 12px;
    }
</style>