import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const REST_API_URL = 'http://localhost:8080/medi';

export const useMediCalendar = defineStore('calendar', () => {
  const userId = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user?.id;
  });
  
  const userPlan = ref([]);
  const eventNotes = ref({});
  const isLoading = ref(false);

  // 날짜 포맷 함수
  const formatDate = (date) => {
    if (!date) return '';
    const d = new Date(date);
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
  };

  // DB 데이터를 eventNotes에 매핑
  const mapDataToEvents = (data) => {
    eventNotes.value = {};
    data.forEach(plan => {
      const dateKey = formatDate(plan.date);
      const options = [];
      
      if (plan.bloodCheck) options.push('피검사');
      if (plan.medicine) options.push('약처방');
      
      if (options.length > 0) {
        eventNotes.value[dateKey] = {
          options: options
        };
      }
    });
  };

  // 데이터 가져오기
  const getPlanData = async (userId) => {
    if (!userId) return;

    isLoading.value = true;
    try {
      const response = await axios.get(`${REST_API_URL}/${userId}`);
      userPlan.value = response.data;
      mapDataToEvents(response.data);
    } catch (error) {
      console.error('데이터 가져오기 실패:', error);
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  // 특정 날짜의 이벤트 체크
  const hasEvents = (date) => {
    const formattedDate = formatDate(date);
    return !!eventNotes.value[formattedDate];
  };

  // 특정 날짜의 이벤트 옵션 가져오기
  const getEventOptions = (date) => {
    const formattedDate = formatDate(date);
    return eventNotes.value[formattedDate]?.options || [];
  };

  // Computed: 이벤트 노트 가져오기
  const getEventNotes = computed(() => eventNotes.value);

  return {
    userId,
    userPlan,
    isLoading,
    formatDate,
    getPlanData,
    hasEvents,
    getEventOptions,
    getEventNotes,
  };
});