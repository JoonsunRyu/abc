// stores/healthCalendar/exercise.js
import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

const REST_API_URL = `http://localhost:8080/exercise`

export const useExerciseStore = defineStore('exercise', () => {
  // 상태 변수
  const exerciseList = ref([])
  const exercise = ref(null)
  const errorMessage = ref('')
  const successMessage = ref('')
  
  const userId = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user?.id;
  });

  /* 운동 조회 (ID 전체 조건 조회) */
  const getExerciseById = (id) => {
    axios.get(`${REST_API_URL}/${id}`)
      .then((response) => {
        if (response.status === 200) {
          exerciseList.value = response.data
        } else {
          exerciseList.value = []
        }
      })
      .catch((error) => {
        // 에러 발생 시 빈 배열로 초기화
        exerciseList.value = []
        errorMessage.value = error.message
        console.error("운동 기록 조회 중 오류 발생:", error)
      })
  }

  /* 운동 조회 (ID와 날짜 조건 조회) */
  const getExerciseByIdAndDate = (userId, date) => {
    return axios.get(`${REST_API_URL}/${userId}/${date}`)
      .then((response) => {
        if (response.status === 200) {
          exercise.value = response.data
          return response.data
        } else {
          exercise.value = null
          return null
        }
      })
      .catch((error) => {
        exercise.value = null
        errorMessage.value = error.message
        console.error("특정 날짜 운동 기록 조회 중 오류 발생:", error)
        return null
      })
  }

  /* 운동 등록 */
  const registerExercise = (newExercise) => {
    return axios.post(REST_API_URL, newExercise)
      .then((response) => {
        if (response.status === 201) {
          successMessage.value = "운동이 성공적으로 등록되었습니다."
          // 새로 등록된 운동 다시 불러오기
          getExerciseById(newExercise.id)
        } else {
          alert("운동 등록에 실패했습니다.")
        }
      })
      .catch((error) => {
        errorMessage.value = error.message
        alert("운동 등록에 실패했습니다.")
      })
  }

  /* 운동 수정 */
  const modifyExercise = (updatedExercise) => {
    return axios.put(REST_API_URL, updatedExercise)
      .then((response) => {
        if (response.status === 200) {
          successMessage.value = "운동이 성공적으로 수정되었습니다."
          // 수정된 운동 다시 불러오기
          getExerciseById(updatedExercise.id)
        } else {
          alert("운동 수정에 실패했습니다.")
        }
      })
      .catch((error) => {
        errorMessage.value = error.message
        alert("운동 수정에 실패했습니다.")
      })
  }

  /* 운동 삭제 */
  const removeExercise = (userId, date) => {
    return axios.delete(REST_API_URL, { data: { id: userId, date: date } })
      .then((response) => {
        if (response.status === 200) {
          successMessage.value = "운동이 성공적으로 삭제되었습니다."
          // 삭제 후 다시 운동 기록 불러오기
          getExerciseById(userId)
        } else {
          alert("운동 삭제에 실패했습니다.")
        }
      })
      .catch((error) => {
        errorMessage.value = error.message
        alert("운동 삭제에 실패했습니다.")
      })
  }

  return {
    exerciseList,
    exercise,
    errorMessage,
    successMessage,
    getExerciseById,
    getExerciseByIdAndDate,
    registerExercise,
    modifyExercise,
    removeExercise,
  }
})