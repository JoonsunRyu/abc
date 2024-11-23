// stores/medichart.js
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";

const REST_API_URL = `http://localhost:8080/chole`;



export const useMedicalStore = defineStore('medical', () => {
  const userId = JSON.parse(sessionStorage.getItem('user')).id;
  const userChart = ref([]);

  /* 검사 데이터 서버로 전송 */
  const mediInfo = function (date, ldlvalue, hdlvalue) {
    if (!date && ldlvalue && hdlvalue) {
      alert("날짜를 지정해주세요.");
      return; // 종료
    }
    if (date && !ldlvalue && hdlvalue) {
      alert("LDL 수치를 입력해주세요.");
      return; // 종료
    }
    if (date && ldlvalue && !hdlvalue) {
      alert("HDL 수치를 입력해주세요.");
      return; // 종료
    }

    // 정상 입력, Promise를 반환하여 호출 측에서 then()을 사용할 수 있도록 함
    return axios.post(`${REST_API_URL}`, {
      id: userId,
      date: date,
      ldl: ldlvalue,
      hdl: hdlvalue,
    })
      .then((response) => {
        userChart.value = response.data;
      })
      .catch((error) => {
        console.error('저장 실패:', error);
        throw error; // 오류를 던져서 외부에서 잡을 수 있도록 함
      });
  };

  /* 데이터 받아오기 */
  const getMedicData = function (userId) {
    return axios.get(`${REST_API_URL}/${userId}`)
      .then((response) => {
        userChart.value = response.data;
      })
      .catch((error) => {
        console.error('데이터 가져오기 실패:', error);
        throw error; // 오류를 던져서 외부에서 잡을 수 있도록 함
      });
  };

  return {
    userChart,
    mediInfo,
    getMedicData,
  };
});
