<template>
  <div>
    <RouterView />
    <div v-if="loginUser">
      <Chatbot />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useUserStore } from '@/stores/user';
import Chatbot from './components/health/chatbot.vue';

const userStore = useUserStore();
const loginUser = ref(userStore.loginUser); // Pinia store에서 loginUser 상태 가져오기

// // 페이지가 처음 로드될 때, sessionStorage에서 값 불러오기
// onMounted(() => {
//   const storedUser = sessionStorage.getItem('user');
//   if (storedUser) {
//     userStore.loginUser = JSON.parse(storedUser); // sessionStorage에서 가져온 값을 Pinia 상태에 반영
//   }
//   console.log(storedUser.value)
// });

// loginUser 상태 변화를 감지하여 Chatbot 표시 여부 결정
watch(() => userStore.loginUser, (newLoginUser) => {
  loginUser.value = newLoginUser;
});
</script>





<style >
html, body {
  font-family: '여기서 수정', sans-serif !important;
}
@font-face {
  font-family:'NanumBarunpenB';
  src: url('assets/fonts/NanumBarunpenB.ttf') format('truetype');
  font-weight: 400;
}
@font-face {
  font-family:'NanumBarunpenR';
  src: url('assets/fonts/NanumBarunpenR.ttf') format('truetype');
  font-weight: 300;
}
@font-face {
  font-family:'NanumSquare_acB';
  src: url('assets/fonts/NanumSquare_acB.ttf') format('truetype');
  font-weight: 700;
}
@font-face {
  font-family:'NanumSquare_acR';
  src: url('assets/fonts/NanumSquare_acR.ttf') format('truetype');
  font-weight: 700;
}
</style>
