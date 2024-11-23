import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';
import { reject } from 'lodash';
import { Collapse } from 'bootstrap';

const REST_API_URL = 'http://localhost:8080/diet';

export const useDietStore = defineStore('diet', () => {
  const userId = JSON.parse(sessionStorage.getItem('user')).id;
  const userDiet = ref([]);

  /* DB 데이터 불러오기 */
  const getMeals = (days, mealOpt) => {
    return axios
      .get(`${REST_API_URL}/${userId}/${days}/${mealOpt}`)
      .then((response) => response.data)
      .catch((error) => {
        console.error("전체 식단 조회 실패:", error);
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


  /* 조회 (Read) */
  // 전체 식단 데이터 조회

  // 요일별 조회
  const showMealsDay = (days) => {
    return new Promise((resolve, reject) => {
      axios.get(`${REST_API_URL}/${userId}/${days}`, {
        data: days
      })
      .then(response => {
        if (response.data && Array.isArray(response.data)) {
          resolve(response.data)
        }
        else {
          resolve([])
        }
      })
      .catch(error => {
        console.error("요일별 조회 실패: ", error)
        reject(error);
      });
    });
  };

  // 요일 & 시간대별 조회
  const showMealsDayTime = (days, mealOpt) => {
    return new Promise((resolve, reject) => {
      axios.get(`${REST_API_URL}/${userId}/${days}/${mealOpt}`, {
        params: { days, mealOpt }
      })
        .then(response => {
          if (response.data && Array.isArray(response.data)) {
            resolve(response.data);
          } else {
            resolve([]);
          }
        })
        .catch(error => {
          console.error('시간대별 식단 조회 중 오류 발생:', error);
          reject(error);
        });
    });
  }

  // 저장 즉시 팝업 창에 뜨도록


  // 팝업 창 열리면 자동 조회 (해당 요일 해당 시간 조회)


  // day-box의 white-box에 표시되도록
    

  // 삭제 (해당 요일 해당 시간 각각 음식)
  const deleteEachMeals = ({ days, mealOpt, diet }) => {
    const deleteData = {
      id: userId,
      weekday: days,
      meal: mealOpt,
      food: diet,
    }
    console.log(deleteData)

    axios.delete(`${REST_API_URL}`, {
      data: deleteData
    })
    .then(() => {
      console.log("삭제 성공", diet);
    })
    .catch((error) => {
      console.error("삭제 실패: ", error);
    });
  }

  // 삭제 (해당 요일 해당 시간 식단 전체)
  const deleteDayTimeMeals = (days, mealOpt) => {
    axios.delete(`${REST_API_URL}/${userId}/${days}/${mealOpt}`)
      .then(() => {
        console.log("삭제 성공");
      })
      .catch((error) => {
        console.error("삭제 실패: ", error);
      });
  };

  return {
    userId,
    userDiet,
    getMeals,
    showMealsDay,
    showMealsDayTime,
    saveMeals,
    deleteEachMeals,
    deleteDayTimeMeals,
  };

});