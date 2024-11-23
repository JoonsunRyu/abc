<template>
  <div>
    <div class="chatbot-icon" @click="toggleChatbot">
      <img src="@/assets/img/chatbot-icon.png" alt="Chatbot Icon" />
      <!-- Optional label on hover -->
      <span>Chat</span>
    </div>

    <div v-if="isChatbotOpen" class="chatbot-modal">
      <div class="chatbot-header">
        <h3>챗봇</h3>
        <button class="close-btn" @click="toggleChatbot">
          <span class="close-icon"></span>
        </button>
      </div>

      <div class="chatbot-messages" ref="messagesContainer">
        <div v-for="(message, index) in messages" :key="index" 
             class="message-container"
             :class="message.sender === 'bot' ? 'bot-container' : 'user-container'">
          <div class="message-bubble" :class="message.sender">
            <div v-html="markdownToHtml(message.text)"></div>
          </div>
        </div>
      </div>

      <div class="chatbot-input">
        <textarea
          v-model="userMessage"
          placeholder="메시지를 입력하세요..."
          rows="2"
          @keyup.enter="sendMessage"
        ></textarea>
        <button @click="sendMessage">전송</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import { useOpenAIStore } from '@/stores/openAI'
import { marked } from 'marked'
import DOMPurify from 'dompurify'

const openAIStore = useOpenAIStore()
const isChatbotOpen = ref(false)
const userMessage = ref('')
const messages = ref([])
const messagesContainer = ref(null)

// Initialize marked options
marked.setOptions({
  breaks: true,
  gfm: true
})

const markdownToHtml = (text) => {
  // Convert markdown to HTML and sanitize
  const rawHtml = marked(text)
  return DOMPurify.sanitize(rawHtml)
}

// Load messages from localStorage on page load
onMounted(() => {
  const storedMessages = localStorage.getItem('messages')
  if (storedMessages) {
    messages.value = JSON.parse(storedMessages)
  }
})

onBeforeUnmount(() => {
  // Save messages to localStorage before unloading
  localStorage.setItem('messages', JSON.stringify(messages.value))
})

// Auto-scroll to bottom when new messages arrive
watch(() => messages.value.length, () => {
  if (messagesContainer.value) {
    setTimeout(() => {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }, 0)
  }
})

const toggleChatbot = () => {
  isChatbotOpen.value = !isChatbotOpen.value
  document.querySelector('.chatbot-icon').classList.toggle('active', isChatbotOpen.value)
}

const sendMessage = async () => {
  if (userMessage.value.trim() !== '') {
    const message = userMessage.value.trim()
    
    // Push the user message
    messages.value.push({ text: message, sender: 'user' })

    // Show "입력중..." message while waiting for the bot's response
    messages.value.push({ text: '입력중...', sender: 'bot', isTyping: true })
    
    userMessage.value = ''
    
    // Send the message to the backend and wait for a response
    await openAIStore.generateMessage(message)
    
    // Update the "입력중..." message with the actual bot response
    localStorage.setItem('messages', JSON.stringify(messages.value))
  }
}

watch(() => openAIStore.generationResult, (newResult) => {
  if (newResult) {
    // Replace "입력중..." with the actual bot response
    const typingMessageIndex = messages.value.findIndex(msg => msg.isTyping)
    if (typingMessageIndex !== -1) {
      messages.value[typingMessageIndex] = {
        text: newResult,
        sender: 'bot'
      }
    }

    localStorage.setItem('messages', JSON.stringify(messages.value))
  }
})

watch(() => openAIStore.errorMessage, (newError) => {
  if (newError) {
    // If there's an error, replace "입력중..." with an error message
    const typingMessageIndex = messages.value.findIndex(msg => msg.isTyping)
    if (typingMessageIndex !== -1) {
      messages.value[typingMessageIndex] = {
        text: `Error: ${newError}`,
        sender: 'bot'
      }
    }
  }
})
</script>

<style scoped>
.chatbot-icon {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background: linear-gradient(135deg, #3498db, #2ecc71);  /* Smooth gradient */
  border-radius: 50%;  /* Circular shape */
  padding: 15px;
  cursor: pointer;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);  /* Soft shadow effect */
  transition: transform 0.3s ease, box-shadow 0.3s ease, background 0.3s ease;  /* Smooth animations */
}

.chatbot-icon:hover {
  transform: scale(1.1);  /* Slightly enlarge the icon on hover */
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);  /* Increase shadow for hover effect */
}

.chatbot-icon img {
  width: 60px;
  height: 60px;
  transition: transform 0.3s ease;
}

/* Shrink image inside when chatbot is open for a fluid transition */
.chatbot-icon.active img {
  transform: scale(0.8);  /* Shrink the icon slightly */
}

/* Optional: If you want to add a text label to the icon */
.chatbot-icon span {
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 12px;
  font-weight: bold;
  color: white;
  text-transform: uppercase;
  letter-spacing: 1px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.chatbot-icon:hover span {
  opacity: 1;  /* Show label on hover */
}

.chatbot-modal {
  position: fixed;
  bottom: 100px;
  right: 20px;
  width: 28%;
  height: 80%;
  background-color: #f5f5f5;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  animation: slideUp 0.3s ease-out;
}

.chatbot-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #3498db;
  color: white;
}

.chatbot-header .close-btn {
  width: 30px;
  height: 30px;
  background-color: transparent;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 5px;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.chatbot-header .close-btn .close-icon {
  width: 18px;
  height: 18px;
  background-color: white;
  border-radius: 50%;
  position: relative;
}

.chatbot-header .close-btn .close-icon::before,
.chatbot-header .close-btn .close-icon::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 12px;
  height: 2px;
  background-color: #3498db;
  border-radius: 2px;
  transform: translate(-50%, -50%);
}

.chatbot-header .close-btn .close-icon::before {
  transform: translate(-50%, -50%) rotate(45deg);
}

.chatbot-header .close-btn .close-icon::after {
  transform: translate(-50%, -50%) rotate(-45deg);
}

.chatbot-header .close-btn:hover {
  background-color: rgba(52, 152, 219, 0.2); /* Light blue hover effect */
  transform: scale(1.1);  /* Slight scale effect on hover */
}

.chatbot-messages {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
}

.message-container {
  display: flex;
  margin-bottom: 15px;
  width: 100%;
}

.bot-container {
  justify-content: flex-start;
}

.user-container {
  justify-content: flex-end;
}

.message-bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
  word-wrap: break-word;
}

.message-bubble.bot {
  background-color: #3498db;
  color: white;
  border-bottom-left-radius: 4px;
}

.message-bubble.user {
  background-color: #ffd700;
  color: black;
  border-bottom-right-radius: 4px;
}

.chatbot-input {
  display: flex;
  flex-direction: column;
  padding: 15px;
  background-color: white;
  border-top: 1px solid #eee;
}

.chatbot-input textarea {
  width: 100%;
  padding: 12px;
  margin-bottom: 10px;
  border-radius: 20px;
  border: 1px solid #ddd;
  font-size: 1rem;
  resize: none;
  background-color: #f8f8f8;
}

.chatbot-input button {
  padding: 12px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-weight: 500;
}

.chatbot-input button:hover {
  background-color: #2980b9;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}
</style>
