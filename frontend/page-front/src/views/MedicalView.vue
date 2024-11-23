<template>
  <div>
    <nav>
      <HeaderNav/>
    </nav>
    
    <div class="content">
      <!-- Chart 영역 -->
      <main>
        <h2>{{ userName }}님의 수치 변화</h2>
        <MediChart/>
        <div class="chart-button">
          <button @click="openModal">입력하기</button>
        </div>

        <!-- 모달 창 -->
        <div v-if="isModalOpen" class="modal-overlay">
          <div class="modal-content">
            <h3>검사 결과 입력</h3>
            <div class="input-group">
              <label for="date">검사일</label>
              <input type="date" id="date" v-model="formData.date">
            </div>
            <div class="input-group">
              <label for="value">LDL</label>
              <input type="number" id="value" v-model="formData.ldlvalue" placeholder="LDL 수치를 입력하세요">
            </div>
            <div class="input-group">
              <label for="value">HDL</label>
              <input type="number" id="value" v-model="formData.hdlvalue" placeholder="HDL 수치를 입력하세요">
            </div>

            <div class="modal-buttons">
              <button class="save-btn" @click="saveData">저장</button>
              <button class="cancel-btn" @click="closeModal">취소</button>
            </div>
          </div>
        </div>
      </main>

      <!-- 검사 일정 관리 -->
      <aside>
        <h2>검진 일정</h2>
        <MediCalendar/>
      </aside>
    </div>

    <!-- 가입자 연령대의 평균 수치 비교 -->
    <section class="extra-content">
      <h2>Additional Content</h2>
      <p>This is an extra section for additional information or widgets.</p>
    </section>

    <!-- 굳이 없어도 될 듯? -->
    <footer>
      <p>&copy; 2024 Medical Check App. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
  import { ref } from 'vue';
  import { useMedicalStore } from "@/stores/medicheck/medichart";
  // import { useCalendarStore } from "@/stores/medicalendar"
  import HeaderNav from "@/components/common/HeaderNav.vue";
  import MediChart from "@/components/medicheck/MediChart.vue";
  import MediCalendar from "@/components/medicheck/MediCalendar.vue";

  const userObject = JSON.parse(sessionStorage.getItem('user'))
  const userName = ref(userObject.name)
  const store = useMedicalStore();
  const userId = JSON.parse(sessionStorage.getItem('user')).id;
  // const calStore = useCalendarStore();

  
  // 모달 관련 상태
  const isModalOpen = ref(false);
  const formData = ref({
    date: '',
    ldlvalue: '',
    hdlvalue: '',
  });

  // 모달 열기
  const openModal = () => {
    isModalOpen.value = true;
  };

  // 모달 닫기
  const closeModal = () => {
    isModalOpen.value = false;
    // 폼 데이터 초기화
    formData.value = {
      date: '',
      ldlvalue: '',
      hdlvalue: '',
    };
  };

  // 수치 입력 데이터 저장
  const saveData = () => {
    store.mediInfo(formData.value.date, formData.value.ldlvalue, formData.value.hdlvalue)
      .then(() => {
        // 저장이 완료되면 차트 업데이트
        store.getMedicData(userId);  // 데이터 다시 가져와서 차트 갱신
        
        closeModal();  // 모달 닫기
      })
      .catch((error) => {
        console.error('데이터 저장 실패:', error);
      });
  };

  // 검사 일정 데이터 저장
  const saveTestData = () => {
    
  }

</script>

<style scoped>
  .layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }

  /* 메인 + 사이드 영역 컨테이너 */
  .content {
    display: flex;
    flex: 1; /* 나머지 공간을 채움 */
  }

  main {
    flex: 3;
    padding: 1rem;
    background-color: #f9f9f9;
  }

  .chart-button {
    display: flex;
    justify-content: center;
    margin-top: 30px;
  }

  /* 모달 스타일 */
  .modal-overlay {
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

  .modal-content {
    background-color: #ffffff;
    padding: 2rem;
    border-radius: 15px;  /* 둥근 모서리 */
    width: 400px;
    max-width: 90%;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);  /* 그림자 효과 */
  }

  .modal-content h3 {
    margin-top: 0;
    margin-bottom: 1.5rem;
    color: #333;
    font-size: 1.25rem;
  }

  .input-group {
    margin-bottom: 1.5rem;
  }

  .input-group label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: bold;
    color: #555;
  }

  .input-group input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 8px;  /* 입력창도 둥글게 */
    font-size: 1rem;
    transition: border-color 0.2s ease;
  }

  .input-group input:focus {
    outline: none;
    border-color: #4CAF50;
    box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.1);
  }

  .modal-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 2rem;
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

  aside {
    flex: 2;
    padding: 1rem;
    background-color: #f0f0f0;
    border-left: 1px solid #ddd;
  }

  .extra-content {
    background-color: #e9ecef;
    padding: 1rem;
    text-align: center;
    border-top: 1px solid #ddd;
    flex: 1;  /* footer까지 차지하도록 */
    min-height: 50vh;  /* 최소 높이를 50%로 설정 */
  }

  footer {
    /* background-color: #e9ecef; */
    background-color: #333;
    color: white;
    text-align: center;
    padding: 0.5rem;
  }
</style>