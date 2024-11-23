import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

const REST_API_URL = 'http://localhost:8080/openai'

export const useOpenAIStore = defineStore('openai', () => {
  const generationResult = ref('')
  const errorMessage = ref('')
  const conversationActive = ref(false)
  
  const userId = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'))
    return user?.id
  })

  const generateMessage = async (message, reset = false) => {
    if (!message && !reset) {
      alert("메시지를 입력해주세요.")
      return
    }

    try {
      const response = await axios.get(`${REST_API_URL}/generate`, {
        params: { 
          message, 
          userId: userId.value, 
          reset
        }
      })
      
      if (response.status === 200) {
        generationResult.value = response.data.generation
        if (!reset) {
          conversationActive.value = true
        }
      }
    } catch (error) {
      errorMessage.value = error.message
      alert("OpenAI 응답을 처리하는 데 실패했습니다.")
    }
  }

  const clearConversation = async () => {
    try {
      if (userId.value) {
        await axios.delete(`${REST_API_URL}/conversation/${userId.value}`)
        
        // 프론트엔드 상태 초기화
        generationResult.value = ''
        errorMessage.value = ''
        conversationActive.value = false
        
        // 로컬 스토리지 초기화
        localStorage.removeItem('messages')
        
        // 대화 초기화 요청
        await generateMessage('', true)
      }
    } catch (error) {
      console.error('Failed to clear conversation:', error)
    }
  }

  return {
    generationResult,
    errorMessage,
    generateMessage,
    clearConversation,
    conversationActive,
    userId,
  }
})
