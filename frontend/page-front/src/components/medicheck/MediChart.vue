<template>
  <div class="chart-container" style="width: 500px; height:500px">
    <LineChart :chart-data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
  import { ref, watch, onMounted } from 'vue'
  import { useMedicalStore } from '@/stores/medicheck/medichart'
  import { Line as LineChart } from 'vue-chartjs'
  import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
  } from 'chart.js'

  ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
  )

  // 스토어 호출
  const store = useMedicalStore();
  const userId = JSON.parse(sessionStorage.getItem('user')).id;

  // 차트 데이터
  const chartData = ref({
    labels: [],  // x축 (날짜)
    datasets: [
      {
        label: 'LDL',
        data: [],  // y축 (LDL)
        borderColor: 'rgb(225, 72, 72)',
        tension: 0.1,
        fill: false,
      },
      {
        label: 'HDL',
        data: [],  // y축 (HDL)
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1,
        fill: false,
      }
    ]
  })

  // 차트 옵션 설정
  const chartOptions = ref({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          boxWidth: 10,
          font: {
            size: 12
          },
        },
      },
    },
    layout: {
      padding: {
        bottom: 20,
      },
    },
    scales: {
      y: {
        beginAtZero: false,
        min: 30, // y축 최소값
        max: 180, // y축 최대값
        ticks: {
          stepSize: 30, // y축 간격 설정
          font: {
            size: 12
          },
        },
      },
      x: {
        ticks: {
          font: {
            size: 12
          },
        },
      },
    },
  })

  // 차트 데이터 업데이트 함수
  const updateChart = () => {
    // 날짜 기준 오름차순 정렬
    const sortedData = [...store.userChart].sort((a, b) => new Date(a.date) - new Date(b.date));

    // x축: 날짜
    chartData.value.labels = sortedData.map((item) => item.date);
    
    // y축: LDL
    chartData.value.datasets[0].data = sortedData.map((item) => item.ldl);
    
    // y축: HDL
    chartData.value.datasets[1].data = sortedData.map((item) => item.hdl);
  }

  // 데이터 변경 시 차트 업데이트
  watch(
    () => store.userChart,
    () => {
      updateChart();
    }
  )

  // 컴포넌트 마운트 시 데이터 가져오기
  onMounted(() => {
    store.getMedicData(userId);  // 데이터 가져오기
  })

</script>

<style scoped>
  .chart-container {
    margin: auto;
  }
</style>
