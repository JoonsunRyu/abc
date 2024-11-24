<!--MediCalendar.vue-->
<template>
  <div class="calendar-container">
    <!-- Header -->
    <div class="calendar-header">
      <button @click="previousMonth">&lt;</button>
      <h2>{{ currentMonthYear }}</h2>
      <button @click="nextMonth">&gt;</button>
    </div>

    <!-- Weekday Labels -->
    <div class="weekdays">
      <div v-for="(day, index) in weekDays" 
           :key="index" 
           :class="{ sunday: index === 0, saturday: index === 6 }">
        {{ day }}
      </div>
    </div>

    <!-- Calendar Grid -->
    <div class="calendar-grid">
      <div 
        v-for="(day, index) in calendarDays" 
        :key="index"
        :class="getDayClasses(day, index)"
        @click="openModal(day)">
        <div class="date">{{ day.dayOfMonth }}</div>
        <div class="event-indicators" v-if="hasEvents(day.date)">
          <span v-for="option in getEventOptions(day.date)" 
                :key="option" 
                :class="['event-dot', option]">
            {{ option }}
          </span>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="modal" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ formattedSelectedDate }} 검진 기록</h3>
        <div class="checkbox-group">
          <label>
            <input type="checkbox" v-model="local피검사" :disabled="isSubmitting" />
            피검사
          </label>
          <label>
            <input type="checkbox" v-model="local약처방" :disabled="isSubmitting" />
            약처방
          </label>
        </div>
        <div class="modal-buttons">
          <button v-if="eventExists" @click="modifyEvent" :disabled="isSubmitting">
            수정
          </button>
          <button v-else @click="saveEvent" :disabled="isSubmitting">
            저장
          </button>
          <button v-if="eventExists" @click="deleteEvent" :disabled="isSubmitting">
            삭제
          </button>
          <button @click="closeModal" :disabled="isSubmitting">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useMediCalendar } from '@/stores/medicheck/medicalendar';
import { useMediModal } from '@/stores/medicheck/medimodal';

/* 스토어 */
const calendarStore = useMediCalendar();
const modalStore = useMediModal();

const currentDate = ref(new Date());
const selectedDate = ref(null);
const showModal = ref(false);
const isSubmitting = ref(false);


/* 모달 */
const local피검사 = ref(false);
const local약처방 = ref(false);

const weekDays = ['일', '월', '화', '수', '목', '금', '토'];

const currentMonthYear = computed(() => {
  const month = currentDate.value.getMonth() + 1;
  const year = currentDate.value.getFullYear();
  return `${year}년 ${month}월`;
});

const formattedSelectedDate = computed(() => {
  if (!selectedDate.value) return '';
  const d = selectedDate.value;
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
});


/* 달력 */
const calendarDays = computed(() => {
  const year = currentDate.value.getFullYear();
  const month = currentDate.value.getMonth();
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const days = [];


  // 이전 달 날짜
  for (let i = firstDay.getDay(); i > 0; i--) {
    const date = new Date(year, month, 1 - i);
    days.push({ date, dayOfMonth: date.getDate(), currentMonth: false });
  }

  // 이번 달 날짜
  for (let i = 1; i <= lastDay.getDate(); i++) {
    const date = new Date(year, month, i);
    days.push({ date, dayOfMonth: i, currentMonth: true });
  }

  // 다음 달 날짜
  const remainingDays = 42 - days.length;
  for (let i = 1; i <= remainingDays; i++) {
    const date = new Date(year, month + 1, i);
    days.push({ date, dayOfMonth: date.getDate(), currentMonth: false });
  }

  return days;
});

const getDayClasses = (day, index) => ({
  'calendar-day': true,
  'current-month': day.currentMonth,
  'today': isToday(day.date),
  'sunday': index % 7 === 0,
  'saturday': index % 7 === 6,
  'has-events': hasEvents(day.date),
});

const isToday = (date) => {
  const today = new Date();
  return date.getDate() === today.getDate() &&
         date.getMonth() === today.getMonth() &&
         date.getFullYear() === today.getFullYear();
};


/* 동작 컨트롤 */
const hasEvents = (date) => {
  const formattedDate = calendarStore.formatDate(date);
  return calendarStore.getEventNotes[formattedDate] !== null;
};

const getEventOptions = (date) => {
  const formattedDate = calendarStore.formatDate(date);
  const eventOptions = calendarStore.getEventNotes[formattedDate]?.options || [];
  return eventOptions.map(option => {
    if (option === '피검사') {
      return '피검사';
    } else if (option === '약처방') {
      return '약처방';
    }
    return ''; // 다른 이벤트는 기본적으로 빈 값 반환
  });
};

const eventExists = computed(() => {
  const formattedDate = calendarStore.formatDate(selectedDate.value);
  return calendarStore.getEventNotes[formattedDate] !== undefined;
});

const openModal = (day) => {
  selectedDate.value = day.date;
  const formattedDate = calendarStore.formatDate(day.date);
  const events = calendarStore.getEventNotes[formattedDate]?.options || [];
  
  // 피검사, 약처방 상태 초기화
  local피검사.value = events.includes('피검사');
  local약처방.value = events.includes('약처방');
  
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  // 상태 초기화
  local피검사.value = false;
  local약처방.value = false;
};

const saveEvent = async () => {
  if (!local피검사.value && !local약처방.value) {
    alert('최소 하나의 항목을 선택해주세요.');
    return;
  }
  isSubmitting.value = true;
  const formattedDate = calendarStore.formatDate(selectedDate.value);
  await modalStore.saveEvent(calendarStore.userId, formattedDate, local피검사.value, local약처방.value);
  await calendarStore.getPlanData(calendarStore.userId);  // 캘린더 데이터 다시 로드
  
  // 저장 후 상태 초기화
  local피검사.value = false;
  local약처방.value = false;
  isSubmitting.value = false;
  closeModal();
};

const modifyEvent = async () => {
  isSubmitting.value = true;
  const formattedDate = calendarStore.formatDate(selectedDate.value);
  await modalStore.modifyEvent(calendarStore.userId, formattedDate, local피검사.value, local약처방.value);
  await calendarStore.getPlanData(calendarStore.userId);  // 캘린더 데이터 다시 로드

  // 수정 후 상태 초기화
  local피검사.value = false;
  local약처방.value = false;
  isSubmitting.value = false;
  closeModal();
};

// 삭제
const deleteEvent = async () => {
  isSubmitting.value = true;
  const formattedDate = calendarStore.formatDate(selectedDate.value);
  
  await modalStore.deleteEvent(calendarStore.userId, formattedDate);
  await calendarStore.getPlanData(calendarStore.userId);  // 캘린더 데이터 다시 로드

  isSubmitting.value = false;
  
  closeModal();
};


const previousMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1);
};

const nextMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1);
};

onMounted(async () => {
  await calendarStore.getPlanData(calendarStore.userId);  // 캘린더 데이터 초기 로드
});
</script>


<style scoped>
/* 캘린더 스타일 */
.calendar-container {
    max-width: 800px;
    margin: 0 auto;
    font-family: Arial, sans-serif;
  }

  .calendar-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
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
    min-height: 80px;
    padding: 5px;
    background-color: white;
    cursor: pointer;
  }

  .calendar-day:hover {
    background-color: #cccaca;
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


/* 이벤트에 따른 색상 */
.calendar-day.has-events .event-dot {
  display: inline-block;
  border-radius: 4px;
  padding: 2px 6px;
  margin: 2px 0;
}

.calendar-day.has-events .event-dot.피검사 {
  background-color: #007bff; /* 파란색 */
  color: white;
}

.calendar-day.has-events .event-dot.약처방 {
  background-color: #28a745; /* 초록색 */
  color: white;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  max-width: 400px;
}

.modal-buttons {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
