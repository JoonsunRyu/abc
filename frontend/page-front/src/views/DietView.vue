<template>
  <div>
    <HeaderNav />
    
    <!-- 요일과 식단 -->
    <main class="diet-main">
      <div v-for="(day, index) in days" :key="index" class="day-box">
        <div class="day-header">
          <div class="day-label">
            {{ day }}
          </div>
        </div>

        <!-- 첫 번째 white-box -->
        <div class="white-box">
          <div class="add-btn" @click="openPopup(index, 0)">+</div>
          <div v-if="meals[index] && meals[index].items[0]" class="meal-summary">
            <div class="meal">{{ meals[index].items[0].join(' ') }}</div>
          </div>
        </div>

        <!-- 두 번째 white-box -->
        <div class="white-box">
          <div class="add-btn" @click="openPopup(index, 1)">+</div>
          <div v-if="meals[index] && meals[index].items[1]" class="meal-summary">
            <div class="meal" v-html="meals[index].items[1].join('<br>')"></div>
          </div>
        </div>

        <!-- 세 번째 white-box -->
        <div class="white-box">
          <div class="add-btn" @click="openPopup(index, 2)">+</div>
          <div v-if="meals[index] && meals[index].items[2]" class="meal-summary">
            <div class="meal">{{ meals[index].items[2].join(' ') }}</div>
          </div>
        </div>

        <!-- 주 단위 삭제 -->
        <button class="del-day" @click="deleteDayMeals">삭제</button>
      </div>
    </main>
    
    <!-- 추가 섹션 영역 -->
    <section class="diet-section"></section>
    
    <!-- 푸터 -->
    <footer>
      <p>&copy; 2024 Medical Check App. All rights reserved.</p>
    </footer>

    <!-- 팝업 창 -->
    <!-- <DietPopup/> -->
    <div v-if="isPopupVisible" class="popup-overlay">
      <div class="popup">
        <button class="close-popup" @click="closing">x</button>
        <h2>{{ days[selectedDay] }} {{ mealOptions[selectedBoxIdx] }} 기록</h2>
        
        <!-- 메뉴 추가 -->
        <div class="input-group">
          <div class="menu-container">
            <button class="add-menu-btn" @click="saveMeals">+</button>
            <div class="menu-list">
              <div v-for="(item, i) in menuInputs" :key="i" class="menu-item">
                <span>{{ item }}</span>
                <button class="del-menu-btn" @click="deleteEachMeals(i); delMenu(i)">-</button>
              </div>
            </div>
          </div>
        </div>

        <div class="popup-buttons">
          <button class="save-btn" @click="closePopup">저장</button>
          <!-- <button class="del-btn" v-if="menuInputs.length > 0" @click="deleteMeals">전체삭제</button> -->
          <button class="cancel-btn" @click="closePopup">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import HeaderNav from "@/components/common/HeaderNav.vue";
  import { onMounted, reactive, ref } from "vue";
  import {useDietStore} from '@/stores/diet/diets'

  const days = ["월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"];
  const mealOptions = ["아침", "점심", "저녁"]
  const meals = reactive(Array(7).fill(null).map(() => ({ items: [] })));  // 요일별 독립된 배열 생성
  const isPopupVisible = ref(false);
  const selectedDay = ref(null);
  const selectedBoxIdx = ref(0);  // white-box 구분
  const menuInputs = reactive([]);  // 팝업에서 입력한 메뉴 목록
  const existsData = ref(false);
  
  const store = useDietStore();  // 스토어
  const closing = () => closePopup();
  
  // DB 데이터를 화면에 로드
  const initializeMeals = () => {
    store.getMeals()
      .then((response) => {
        response.forEach(({ weekday, meal, food }) => {
          const dayIdx = days.indexOf(weekday);
          const mealIdx = mealOptions.indexOf(meal);
          if (dayIdx !== -1 && mealIdx !== -1) {
            if (!meals[dayIdx].items[mealIdx]) {
              meals[dayIdx].items[mealIdx] = [];
            }
            meals[dayIdx].items[mealIdx].push(food);
          }
        });
      })
      .catch((error) => console.error("초기 데이터 로드 실패:", error));
  };

  /* 메뉴 저장 직후 화면에 표시 */
  const showMeals = () => {
    const dayIdx = selectedDay.value;
    const mealIdx = selectedBoxIdx.value;

    // 해당 요일, 시간 메뉴 조회
    store.showMealDayTime(days[dayIdx], mealOptions[mealIdx])
    .then((response) => {
      meals[dayIdx].items[mealIdx] = response.map(item => item.food);
    })
    .catch(error => {
      console.log("메뉴 조회 실패: ", error)
    })
  }

  // 특정 요일 전체메뉴 조회
  const showMealsDay = (dayIdx) => {
    store.showMealsDay(days[dayIdx])  // 스토어에서 해당 요일의 전체 메뉴 조회
    .then((response) => {

      response.forEach(item => {  // 조회된 데이터를 시간대별로 분류하여 meals 객체에 저장
        const mealIdx = mealOptions.indexOf(item.meal);

        if (mealIdx !== -1) {
          // 해당 시간대 배열 없으면 초기화
          if(!meals[dayIdx].items[mealIdx]) {
            meals[dayIdx].items[mealIdx] = [];
          }

          // 같은 시간대 메뉴들만 필터링하여 저장
          meals[dayIdx].items[mealIdx] = response
          .filter(r => r.meal === mealOptions[mealIdx])
          .map(r => r.food)
        }
      });
    })
    .catch(error => {
      console.error("일별 메뉴 조회 실패: ", error)
    })
  }

  // 팝업 창 열리면 기존 데이터 보이도록
  const openPopup = (dayIdx, boxIdx) => {
    isPopupVisible.value = true;
    console.log(dayIdx, boxIdx)
    selectedDay.value = dayIdx;
    selectedBoxIdx.value = boxIdx;

  };

  // 입력 완료 후 팝업 창 종료 (Front에서)
  const closePopup = () => {
    isPopupVisible.value = false;
    selectedDay.value = null;
    selectedBoxIdx.value = null;
    menuInputs.splice(0, menuInputs.length);
  };

  // 팝업 창 안에서 메뉴 삭제 (프론트)
  const delMenu = (index) => {
    menuInputs.splice(index, 1);
  };


  /* DB 연동 */

  /* 조회 */
  // 메뉴

  /* 입력 (팝업 창 안 +버튼) */
  const saveMeals = () => {
    const newItem = prompt("메뉴를 입력하세요");
    
    // 현재 선택된 요일과 시간대의 메뉴 배열 가져오기
    let currentMeals = meals[selectedDay.value]?.items[selectedBoxIdx.value];
    
    // 만약 해당 배열이 없다면 초기화
    if (!currentMeals) {
      if (!meals[selectedDay.value]) {
        meals[selectedDay.value] = { items: {} };
      }
      if (!meals[selectedDay.value].items[selectedBoxIdx.value]) {
        meals[selectedDay.value].items[selectedBoxIdx.value] = [];
      }
      currentMeals = meals[selectedDay.value].items[selectedBoxIdx.value];
    }

    if (newItem && !currentMeals.includes(newItem)) {  // 스토어 호출: 메뉴 저장
      store.saveMeals(days[selectedDay.value], mealOptions[selectedBoxIdx.value], newItem);
      
      // UI 업데이트
      currentMeals.push(newItem);
      menuInputs.push(newItem);
      showMeals()
    } else if (currentMeals.includes(newItem)) {
      alert("이미 입력한 메뉴입니다.");
    } else {
      alert("최소 1개의 메뉴를 입력해주세요.");
    }
  };

  // 3. 컴포넌트가 마운트될 때 초기화
  onMounted(() => {
    initializeMeals();
  });

  /* 삭제 */
  // 팝업 창 안 빨간색 - 버튼
  const deleteEachMeals = (foodIdx) => {
    // 선택된 요일, 시간대 음식
    const currentMeals = meals[selectedDay.value].items[selectedBoxIdx.value];

    // 특정 음식 삭제
    if (currentMeals && currentMeals.length > foodIdx) {
      const deletedMeal = currentMeals.splice(foodIdx, 1)[0];  // Array 반환
      const deletedMealString = deletedMeal.toString();  // String으로 변환
      meals[selectedDay.value].items[selectedBoxIdx.value] = currentMeals;      

      // 스토어 호출
      store.deleteEachMeals({
        days: days[selectedDay.value],
        mealOpt: mealOptions[selectedBoxIdx.value],
        diet: deletedMealString,
      });
    }
  }

  // 팝업창 하단 '전체 삭제' 버튼
  const deleteDayTimeMeals = () => {
    store.deleteDayTimeMeals(days[selectedDay.value], mealOptions[selectedBoxIdx.value]);
    meals[selectedDay.value].items.splice(selectedBoxIdx.value, 1, "");
    menuInputs.splice(0, menuInputs.length);

  };

  
  // day-box 하단 특정 요일 전체 삭제



  /* 조회 */
  // 저장 즉시 조회
  const showMeal = () => {
    
  }

  // 해당 날짜 조회 (white-box 표시)
  const showDayMeals = () => {
    
  }

</script>

<style scoped>
  .diet-main {
    padding: 16px;
    display: flex;
    justify-content: space-between; /* 동일한 간격 유지 */
    margin: 0 8px; /* 좌우 여백 */
    align-items: center;
  }

  .diet-main {
    align-items: flex-start; /* 위쪽 정렬 */
    flex-wrap: wrap; /* 화면 크기에 따라 자동으로 줄 바꿈 */
  }

  .day-box {
  flex-grow: 1; /* 자식 요소의 크기에 따라 크기 확장 */
  max-width: calc(100% / 7 - 16px); /* 여유 간격 포함 계산 */
  min-width: 100px;
  min-height: 500px; /* 최소 높이 설정 */
  border: 1px solid #ccc;
  border-radius: 12px;
  background: #f9f9f9;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  padding: 8px; /* 전체 padding 축소 */
  box-sizing: border-box; /* padding 포함 크기 계산 */
  height: auto; /* 자식 요소에 맞춰 자동으로 높이 조정 */
}

.day-header, .white-box {
  position: relative; /* 위로 이동할 수 있게 */
  top: 0; /* 상단 고정, day-box 내에서 위쪽에 정렬 */
}

.white-box {
  width: 80%; /* 크기 조정 */
  min-height: 120px;
  background-color: #ffffff;
  border: 1px solid #ccc;
  border-radius: 8px;
  margin: 8px 0; /* 박스 간격 */
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 0.9rem;
}

.day-header {
  width: 100%;
  display: flex;
  justify-content: center; /* 중앙 정렬 */
  align-items: center; /* 수직 정렬 */
  position: relative;
  top: 0; /* day-box 내에서 위쪽에 정렬 */
  height: 30%; /* 비율로 높이 설정, 화면 크기에 따라 비례 */
  padding-top: 16px; /* 추가적인 위쪽 여백 설정 */
}

.day-label {
  margin-bottom: 20px;
  font-weight: bold;
  font-size: 20px;
  text-align: center; /* 텍스트 중앙 정렬 */
}



  .add-btn {
    position: absolute; /* white-box 안에서 절대 위치 */
    font-size: 2rem;
    color: #666;
    cursor: pointer;
    top: -7px; /* 상단 여백 */
    right: 8px; /* 우측 여백 */

  }

  .diet-section {
    height: 100px; /* 중간 섹션 높이 */
    background-color: #f0f0f0;
    margin: 16px 0;
  }

  .diet-footer {
    height: 60px;
    background-color: #ddd;
    text-align: center;
    line-height: 60px;
    font-size: 1rem;
  }

  /* 팝업 스타일 */
  .menu-container {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .menu-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }

  .menu-item {
    background-color: #f0f0f0;
    border-radius: 12px;
    padding: 4px 12px;
    display: flex;
    align-items: center;
    gap: 4px;
    position: relative;
  }

  .del-menu-btn {
    background-color: red;
    color: white;
    border: none;
    border-radius: 50%;
    width: 20px;
    height: 20px;
    cursor: pointer;
    font-size: 1rem;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }

  .popup {
    background-color: #ffffff;
    padding: 2rem;
    border-radius: 15px;  /* 둥근 모서리 */
    width: 400px;
    max-width: 90%;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);  /* 그림자 효과 */
  }

  .input-group {
    margin-bottom: 16px;
  }

  .close-popup {
    position: relative;
    top: 10%;
    left: 95%;
    background: none;
    border: none;
    font-size: 24px;
    cursor: pointer;
    color: #666;
    padding: 0;
    margin: 0;
    line-height: 1;
  }

  .save-btn, .del-btn, .cancel-btn {
    padding: 0.75rem 1.5rem;
    border: none;
    border-radius: 8px;  /* 버튼도 둥글게 */
    cursor: pointer;
    font-size: 1rem;
    transition: all 0.2s ease;
  }

  .save-btn {
    background-color: #4CAF50;
    color: white;
  }

  .save-btn:hover {
    background-color: #45a049;
    transform: translateY(-1px);
  }

  .del-btn {
    background-color: goldenrod;
    color: white;
  }

  .del-btn:hover {
    background-color: darkgoldenrod;
    transform: translateY(-1px);
  }

  .cancel-btn {
    background-color: #f44336;
    color: white;
  }

  .cancel-btn:hover {
    background-color: #da190b;
    transform: translateY(-1px);
  }

  button {
    margin-top: 8px;
    margin-right: 8px;
  }

  footer {
      /* background-color: #e9ecef; */
      background-color: #333;
      color: white;
      text-align: center;
      padding: 0.5rem;
    }
</style>