import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const REST_API_URL = 'http://localhost:8080/diet';

export const useDietStore = defineStore('diet', () => {
  const userId = JSON.parse(sessionStorage.getItem('user')).id;
  const userDiet = ref([]);
  const dietData = ref({});

  // DB 데이터를 불러오는 함수
  const mapData = (data) => {
    // 초기화
    dietData.value = {}; // 요일, 끼니 정보 담을 배열 초기화
    meals.splice(0, meals.length, ...Array(7).fill(null).map(() => {{ items: [] }}))
    
    // 데이터 매핑
    data.forEach(dietDB => {
      const dayIdx = days.indexOf(dietDB.weekday);  // 요일
      const mealIdx = mealOptions.indexOf(dietDB.meal);  // 끼니

      if (dayIdx !== -1 && mealIdx !== -1) {
        if (!meals[dayIdx].items[mealIdx]) {
          meals[dayIdx].items[mealIdx] = [];  // 데이터 없으면 빈 배열
        }

        // 음식 추가
        meals[dayIdx].items[mealIdx].push(dietDB.food);
      }
    })
    console.log("매핑된 데이터: ", meals)
  }

  /* 조회 (Read) */
  const getMeals = () => {
    return axios
      .get(`${REST_API_URL}/${userId}`, {
        params: { id: userId }, // 쿼리 파라미터로 사용자 ID 전달
      })
      .then((response) => {
        console.log("조회 성공:", response.data);
        // 조회된 데이터를 userDiet에 저장
        userDiet.value = response.data;

        // 데이터 매핑 및 UI 반영
        response.data.forEach((diet) => {
          const dayIndex = days.indexOf(diet.weekday); // 요일 인덱스 찾기
          const mealIndex = mealOptions.indexOf(diet.meal); // 끼니 인덱스 찾기

          if (dayIndex !== -1 && mealIndex !== -1) {
            if (!meals[dayIndex].items[mealIndex]) {
              meals[dayIndex].items[mealIndex] = [];
            }
            meals[dayIndex].items[mealIndex].push(diet.food);
          }
        });

        return response.data; // 데이터 반환
      })
      .catch((error) => {
        console.error("조회 실패:", error);
        throw error;
      });
  };



  /* 입력 (Create) */
  const saveMeals = (days, mealOpt, diet) => {    
    axios.post(`${REST_API_URL}`, {
      id: userId,
      weekday: days,
      meal: mealOpt,
      food: diet,
    })
    .then((response) => {
      userDiet.value = response.data;
      console.log(response.data)

      // 저장 완료 후 UI 업데이트
      let currentMeals = meals[selectedDay.value]?.items[selectedBoxIdx.value];
      if (!currentMeals) {
        meals[selectedDay.value].items[selectedBoxIdx.value] = [];
        currentMeals = meals[selectedDay.value].items[selectedBoxIdx.value];
      }

      currentMeals.push(newItem);  // UI에 반영
      menuInputs.push(newItem);    // 팝업 창에도 반영
    })
    .catch((error) => {
      // console.error("저장 실패", error)
    })
  };


  // 삭제 (해당 요일 해당 시간 각각 음식)
  const deleteEachMeals = ({ days }) => {
    const deleteData = {
      id: userId,
      weekday: days,
    };
    console.log(deleteData);

    return axios.delete(`${REST_API_URL}/${userId}/${days}`, {
      data: deleteData
    })
    .then(() => {
      console.log("삭제 성공");
    })
    .catch((error) => {
      console.error("삭제 실패: ", error);
      throw error;
    });
  };

  // 해당 요일 식단 전체 삭제
  const deleteDayMeals = ({ days }) => {
    const deleteData = {
      id: userId,
      weekday: days,
    };
    return axios.delete(`${REST_API_URL}/${userId}/${days}`, {
      data: deleteData
    })
    .then(() => {
      console.log("삭제 성공");
    })
    .catch((error) => {
      console.log("삭제 실패", error);
      throw error
    })
  };

  // 1주일 식단 모두 삭제
  const deleteAll = () => {
    return axios.delete(`${REST_API_URL}/${userId}`, {  // return 안쓰면 화면에서 안 사라짐
        params: { id: userId }, // 사용자 ID 전달
      })
      .then((response) => {
        console.log("1주일 식단 삭제 성공:", response.data);        
      })
      .catch((error) => {
        console.error("1주일 식단 삭제 실패:", error);
        throw error; // 에러 전달
      });
  };

  return {
    userId,
    userDiet,
    mapData,
    getMeals,
    saveMeals,
    deleteEachMeals,
    deleteDayMeals,
    deleteAll,
  };

});