// stores/medicheck/medimodal.js
import { ref } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';

const REST_API_URL = 'http://localhost:8080/medi';

export const useMediModal = defineStore('modal', () => {
  const showModal = ref(false);
  const selectedDate = ref(null);
  const selectedOptions = ref({
    피검사: false,
    약처방: false,
  });
  const isSubmitting = ref(false);

  // 이벤트 저장
  const saveEvent = async (userId, date, bloodCheck, medicine) => {
    
    if (!date || (!bloodCheck && !medicine)) {
      throw new Error('필수 입력 항목이 누락되었습니다.',{date});
    }

    isSubmitting.value = true;
    try {
      const response = await axios.post(REST_API_URL, {
        id: userId,
        bloodCheck,
        medicine,
        date
      });
      return response.data;
    } catch (error) {
      console.error('저장 실패:', error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // 이벤트 수정
  const modifyEvent = async (userId, date, bloodCheck, medicine) => {
    if (!date || (!bloodCheck && !medicine)) {
      throw new Error('필수 입력 항목이 누락되었습니다.');
    }

    isSubmitting.value = true;
    try {
      const response = await axios.put(`${REST_API_URL}`, {
        id: userId,
        bloodCheck,
        medicine,
        date
      });
      return response.data;
    } catch (error) {
      console.error('수정 실패:', error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // 이벤트 삭제
  const deleteEvent = async (userId, date) => {
    if (!date) {
      throw new Error('필수 입력 항목이 누락되었습니다.');
    }

    isSubmitting.value = true;
    try {
      const response = await axios.delete(`${REST_API_URL}`, {
        data: {
          id: userId,
          bloodCheck:true,
          medicine:true,
          date,
        },
      });
      return response.data;
    } catch (error) {
      console.error('삭제 실패:', error);
      throw error;
    } finally {
      isSubmitting.value = false;
    }
  };

  // 모달 열기
  const openModal = (date, existingOptions = null) => {
    selectedDate.value = date;
    if (existingOptions) {
      selectedOptions.value = { ...existingOptions };
    } else {
      selectedOptions.value = {
        피검사: false,
        약처방: false,
      };
    }
    showModal.value = true;
  };

  // 모달 닫기
  const closeModal = () => {
    showModal.value = false;
    selectedDate.value = null;
    selectedOptions.value = {
      피검사: false,
      약처방: false,
    };
  };

  return {
    showModal,
    selectedDate,
    selectedOptions,
    isSubmitting,
    saveEvent,
    modifyEvent,
    openModal,
    closeModal,
    deleteEvent
  };
});