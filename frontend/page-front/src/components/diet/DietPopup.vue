<template>
  <div>
    <!-- 팝업 창 -->
    <div v-if="isPopupVisible" class="popup-overlay">
      <div class="popup">
        <h2>{{ days[selectedDay] }} {{ mealOptions[selectedBoxIdx] }} 기록</h2>
        
        <!-- 메뉴 추가 -->
        <div class="input-group">
          <label>메뉴</label>
          <div class="menu-container">
            <button class="add-menu-btn" @click="addMenu">+</button>
            <div class="menu-list">
              <div v-for="(item, i) in menuInputs" :key="i" class="menu-item">
                <span>{{ item }}</span>
                <button class="del-menu-btn" @click="deleteMenu(i)">-</button>
              </div>
            </div>
          </div>
        </div>

        <div class="popup-buttons">
          <button class="modify-btn" v-if="existsData" @click="modifyMeals">수정</button>
          <button class="save-btn" v-else @click="saveMeals">저장</button>
          <button class="del-btn" v-if="menuInputs.length > 0" @click="deleteMeals">삭제</button>
          <button class="cancel-btn" @click="closePopup">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { reactive, ref } from "vue";
  import { useDietStore } from '@/stores/diet/diet';

  const props = defineProps({
  isPopupVisible: {
    type: Boolean,
    required: true
  },
  selectedDay: {
    type: Number,
    required: true
  },
  selectedBoxIdx: {
    type: Number,
    required: true
  }
});

// Emits 정의
const emit = defineEmits([
  'update:isPopupVisible', 
  'save-meals', 
  'modify-meals', 
  'delete-meals'
]);

// 상수 정의
const days = ["월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"];
const mealOptions = ["아침", "점심", "저녁"];

// 반응형 데이터
const menuInputs = reactive([]);
const existsData = ref(false);

// 스토어 사용
const useDietStore = defineStore();

// 메서드 구현
const addMenu = () => {
  const newItem = prompt("메뉴를 입력하세요");
  if (newItem && !menuInputs.includes(newItem)) {
    menuInputs.push(newItem);
  } else {
    alert("이미 입력한 메뉴이거나 유효하지 않은 메뉴입니다.");
  }
};

const deleteMenu = (index) => {
  menuInputs.splice(index, 1);
};

const closePopup = () => {
  emit('update:isPopupVisible', false);
};

const saveMeals = () => {
  if (menuInputs.length > 0) {
    const mealString = menuInputs.join(", ");
    emit('save-meals', {
      day: props.selectedDay,
      boxIdx: props.selectedBoxIdx,
      meal: mealString
    });
    closePopup();
  } else {
    alert("최소 하나의 메뉴를 입력해주세요.");
  }
};

const modifyMeals = () => {
  if (menuInputs.length > 0) {
    const mealString = menuInputs.join(", ");
    emit('modify-meals', {
      day: props.selectedDay,
      boxIdx: props.selectedBoxIdx,
      meal: mealString
    });
    closePopup();
  } else {
    alert("최소 하나의 메뉴를 입력해주세요.");
  }
};

const deleteMeals = () => {
  emit('delete-meals', {
    day: props.selectedDay,
    boxIdx: props.selectedBoxIdx
  });
  closePopup();
};

  // const days = ["월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"];
  // const mealOptions = ["아침", "점심", "저녁"]
  // const meals = reactive(Array(7).fill(null).map(() => ({ items: [] })));  // 요일별 독립된 배열 생성
  // const isPopupVisible = ref(false);
  // const selectedDay = ref(null);
  // const selectedBoxIdx = ref(0);  // white-box 구분
  // const menuInputs = reactive([]);  // 팝업에서 입력한 메뉴 목록

  // const openPopup = (dayIdx, boxIdx) => {
  //   selectedDay.value = dayIdx;
  //   selectedBoxIdx.value = boxIdx;
  //   isPopupVisible.value = true;
    
  //   /* 프론트에서만 처리하는 로직 */

  //   const existsData = meals[dayIdx]?.items[boxIdx];
  //   if (existsData) {  // 데이터 있으면 렌더링
  //     menuInputs.splice(0, menuInputs.length, ...existsData.split(", ")); // 기존 데이터를 복사
  //   }
  //   menuInputs.splice(0, menuInputs.length); // 없으면 초기화
  // };
  
  // const closePopup = () => {
  //   isPopupVisible.value = false;
  //   selectedDay.value = null;
  //   selectedBoxIdx.value = null;
  // };

  // // 팝업 창 안에서 메뉴 추가
  // const addMenu = () => {
  //   const newItem = prompt("메뉴를 입력하세요");
  //   if (newItem) {
  //     menuInputs.push(newItem);
  //   }
  //   else {
  //     alert("이미 입력한 메뉴입니다.")
  //   }
  // };

  // // 팝업 창 안에서 메뉴 삭제
  // const deleteMenu = (index) => {
  //   menuInputs.splice(index, 1);
  // };

</script>

<style scoped>
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

  .save-btn, .cancel-btn {
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
</style>