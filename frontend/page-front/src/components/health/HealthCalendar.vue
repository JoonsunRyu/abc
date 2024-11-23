<template>
  <div class="calendar-container">
    <div class="calendar-header">
      <button @click="previousMonth">&lt;</button>
      <h2>{{ currentMonthYear }}</h2>
      <button @click="nextMonth">&gt;</button>
    </div>

    <div class="weekdays">
      <div v-for="(day, index) in weekDays" :key="day"
        :class="{
          'sunday': index === 0,
          'saturday': index === 6
        }">
        {{ day }}
      </div>
    </div>

    <div class="calendar-grid">
      <div v-for="(day, index) in calendarDays" :key="day.date"
        :class="[ 
          'calendar-day', 
          { 'current-month': day.currentMonth },
          { 'today': isToday(day.date) },
          { 'sunday': index % 7 === 0 },
          { 'saturday': index % 7 === 6 }
        ]"
        @click="openExerciseModal(day)">
        <div class="date">{{ day.dayOfMonth }}</div>
        <div class="exercise-note" v-if="exerciseNotes[formatDate(day.date)]" v-html="exerciseNotes[formatDate(day.date)]">
        </div>
      </div>
    </div>

    <!-- 운동 기록 모달 -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>{{ formatDate(selectedDate) }} 운동 기록</h3>
        <textarea
          v-model="exerciseNote"
          placeholder="오늘의 운동을 기록하세요"
          rows="4"
        ></textarea>
        <div class="modal-buttons">
          <!-- 기존 데이터 있는 경우 수정/삭제 버튼, 없는 경우 저장 버튼 -->
          <template v-if="existingExercise">
            <button @click="modifyExerciseNote" class="modify-btn">수정</button>
            <button @click="removeExerciseNote" class="delete-btn">삭제</button>
          </template>
          <template v-else>
            <button @click="saveExerciseNote" class="save-btn">저장</button>
          </template>
          <button @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useExerciseStore } from '@/stores/healthCalendar/exercise';
import { ref, computed, watch, onMounted } from 'vue';

// 상태 관리
const currentDate = ref(new Date());
const showModal = ref(false);
const selectedDate = ref(null);
const exerciseNote = ref('');
const exerciseNotes = ref({}); // 캘린더 날짜별 운동 기록 저장
const existingExercise = ref(null); // 기존 운동 기록 여부 확인
const userId = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user?.id;
  });

// 요일 배열
const weekDays = ['일', '월', '화', '수', '목', '금', '토'];
const exerciseStore = useExerciseStore();

// 현재 월/년 표시
const currentMonthYear = computed(() => {
  return `${currentDate.value.getFullYear()}년 ${currentDate.value.getMonth() + 1}월`;
});

// 달력 날짜 계산
const calendarDays = computed(() => {
  const year = currentDate.value.getFullYear();
  const month = currentDate.value.getMonth();
  
  const firstDayOfMonth = new Date(year, month, 1);
  const lastDayOfMonth = new Date(year, month + 1, 0);
  
  const days = [];
  
  // 이전 달의 날짜들
  for (let i = firstDayOfMonth.getDay(); i > 0; i--) {
    const date = new Date(year, month, 1 - i);
    days.push({
      date,
      dayOfMonth: date.getDate(),
      currentMonth: false
    });
  }
  
  // 현재 달의 날짜들
  for (let i = 1; i <= lastDayOfMonth.getDate(); i++) {
    const date = new Date(year, month, i);
    days.push({
      date,
      dayOfMonth: i,
      currentMonth: true
    });
  }
  
  // 다음 달의 날짜들
  const remainingDays = 42 - days.length; // 6주 채우기
  for (let i = 1; i <= remainingDays; i++) {
    const date = new Date(year, month + 1, i);
    days.push({
      date,
      dayOfMonth: date.getDate(),
      currentMonth: false
    });
  }
  
  return days;
});

// 날짜 포맷 함수
const formatDate = (date) => {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

// 오늘 날짜 확인
const isToday = (date) => {
  const today = new Date();
  return date.getDate() === today.getDate() &&
         date.getMonth() === today.getMonth() &&
         date.getFullYear() === today.getFullYear();
};

// 이전/다음 달 이동
const previousMonth = () => {
  currentDate.value = new Date(
    currentDate.value.getFullYear(),
    currentDate.value.getMonth() - 1
  );
};

const nextMonth = () => {
  currentDate.value = new Date(
    currentDate.value.getFullYear(),
    currentDate.value.getMonth() + 1
  );
};

// 초기 데이터 로드 함수
const loadInitialExerciseData = () => {
  if (userId.value) {
    // 사용자의 모든 운동 기록 불러오기
    exerciseStore.getExerciseById(userId.value);
  }
};

// 마운트 시 데이터 로드
onMounted(() => {
  loadInitialExerciseData();

  // 로드된 데이터를 exerciseNotes에 매핑
  watch(() => exerciseStore.exerciseList, (newList) => {
    // exerciseNotes 초기화
    exerciseNotes.value = {};

// 불러온 운동 기록을 exerciseNotes에 추가
    newList.forEach(exercise => {
      exerciseNotes.value[exercise.date] = exercise.exer.replace(/\n/g, '<br>');
    });
  }, { immediate: true });
});

// 모달 관련 함수 수정
const openExerciseModal = (day) => {
  selectedDate.value = day.date;
  const formattedDate = formatDate(day.date);
  
  // 해당 날짜의 운동 기록 불러오기
  exerciseStore.getExerciseByIdAndDate(userId.value, formattedDate);
  
  watch(() => exerciseStore.exercise, (newExercise) => {
    existingExercise.value = newExercise; // 기존 운동 기록 참조
    exerciseNote.value = newExercise ? newExercise.exer : '';
    showModal.value = true;
  }, { immediate: true });
};
// 수정 함수 추가
const modifyExerciseNote = () => {
  if (exerciseNote.value.trim()) {
    const updatedExercise = {
      id: userId.value,
      date: formatDate(selectedDate.value),
      exer: exerciseNote.value
    };

    // 운동 기록 수정
    exerciseStore.modifyExercise(updatedExercise);

    // 캘린더에 반영
    exerciseNotes.value[updatedExercise.date] = exerciseNote.value.replace(/\n/g, '<br>');
    
    closeModal();
  }
};

// 삭제 함수 추가
const removeExerciseNote = () => {
  const dateToDelete = formatDate(selectedDate.value);
  
  // 운동 기록 삭제
  exerciseStore.removeExercise(userId.value, dateToDelete);
  
  // 캘린더에서 삭제
  delete exerciseNotes.value[dateToDelete];
  
  closeModal();
};


// 모달 닫기 함수 수정
const closeModal = () => {
  showModal.value = false;
  exerciseNote.value = '';
  existingExercise.value = null; // 기존 운동 기록 초기화
};

// 기존 저장 함수는 새로운 운동 기록 추가에만 사용
const saveExerciseNote = () => {
  if (exerciseNote.value.trim()) {
    const newExercise = {
      id: userId.value,
      date: formatDate(selectedDate.value),
      exer: exerciseNote.value
    };

    // 새로운 운동 기록 등록
    exerciseStore.registerExercise(newExercise);

    // 캘린더에 반영
    exerciseNotes.value[newExercise.date] = exerciseNote.value.replace(/\n/g, '<br>');
    
    closeModal();
  }
};

</script>
  
<style scoped>
  .calendar-container {
    max-width: 800px;
    margin: 0 auto;
    font-family: Arial, sans-serif;
  }
  
  .calendar-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 10px;
  }
  
  .calendar-header button {
    padding: 5px 15px;
    cursor: pointer;
  }
  
  .weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    text-align: center;
    font-weight: bold;
    background-color: #f0f0f0;
    padding: 10px 0;
  }
  
  .weekdays .sunday {
    color: #ff0000;
  }
  
  .weekdays .saturday {
    color: #0066cc;
  }
  
  .calendar-grid {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 1px;
    background-color: #ddd;
  }
  
  .calendar-day {
    min-height: 100px;
    padding: 5px;
    background-color: white;
    cursor: pointer;
  }
  
  .calendar-day:hover {
    background-color: #f0f0f0;
  }
  
  .calendar-day .date {
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .current-month {
    color: black;
  }
  
  .calendar-day:not(.current-month) {
    color: #999;
  }
  
  .calendar-day.saturday {
    color: #0066cc;
  }

  .calendar-day.sunday {
    color: #ff0000;
  }
  
  .calendar-day.saturday:not(.current-month) .date {
    color: rgba(70, 130, 180, 0.5); /* 흐릿한 파란색 */
  }
  
  .calendar-day.sunday:not(.current-month) .date {
    color: rgba(220, 20, 60, 0.5); /* 흐릿한 빨간색 */
  }
  
  .today {
    background-color: #e6f3ff;
  }
  
  .exercise-note {
    font-size: 0.8em;
    color: #666;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  /* 운동 기록 모달 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.3s ease-out;
}

.modal-content {
  background-color: #fff;
  padding: 25px;
  border-radius: 12px;
  width: 90%;
  max-width: 480px;
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
  transform: translateY(-30px);
  animation: slideUp 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    transform: translateY(30px);
  }
  to {
    transform: translateY(0);
  }
}

.modal-content h3 {
  margin-bottom: 15px;
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}

.modal-content textarea {
  width: 95%;
  min-height: 120px;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #ddd;
  font-size: 1rem;
  resize: vertical;
  transition: border-color 0.3s ease;
}

.modal-content textarea:focus {
  border-color: #3498db;
  outline: none;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 15px;
}

.modal-buttons button {
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.modal-buttons .save-btn {
  background-color: #3498db;
  color: white;
}

.modal-buttons .modify-btn {
  background-color: #2ecc71;
  color: white;
}

.modal-buttons .delete-btn {
  background-color: #e74c3c;
  color: white;
}

.modal-buttons .save-btn:hover {
  background-color: #2980b9;
  transform: scale(1.05);
}

.modal-buttons .modify-btn:hover {
  background-color: #27ae60;
  transform: scale(1.05);
}

.modal-buttons .delete-btn:hover {
  background-color: #c0392b;
  transform: scale(1.05);
}

.modal-buttons button:active {
  transform: scale(1);
}

.modal-buttons .delete-btn:hover,
.modal-buttons .modify-btn:hover,
.modal-buttons .save-btn:hover {
  transform: scale(1.05);
}

/* 취소 버튼 스타일 */
.modal-buttons .cancel-btn {
  background-color: #f1f1f1;
  color: #333;
  border: 1px solid #ddd;
}

.modal-buttons .cancel-btn:hover {
  background-color: #ddd;
}

.modal-buttons .cancel-btn:active {
  transform: scale(1);
}
</style>
