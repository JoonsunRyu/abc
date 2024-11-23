<template>
  <div class="login-container">
    <h2>로그인</h2>
    <form @submit.prevent="login">
      <div class="login-form">
        <label for="userid">아이디</label>
        <input type="text" id="userid" v-model.trim="id" placeholder="아이디를 입력하세요" />
      </div>
      <div class="login-form">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model.trim="password" placeholder="비밀번호를 입력하세요" />
      </div>
      <button type="submit" class="login-button">로그인</button>
      <div class="regist-link">
        <a @click.prevent="goToSignup">회원가입</a>
      </div>
    </form>
  </div>
</template>

<script setup>
  import { ref } from "vue";
  import { useUserStore } from "@/stores/user";
  import router from '@/router';

  const store = useUserStore();

  const id = ref('')
  const password = ref('')

  const login = function() {
    store.userLogin(id.value, password.value)
  }

  const goToSignup = () => {
    router.push('/signup')
  }
</script>

<style scoped>
  .login-container {         /* 전체 화면에서 창 화면으로 바꿔도 정중앙 정렬 & 크기 비율 유지 */
    width: 80vw;             /* 화면 너비의 80% 사용 */
    max-width: 500px;        /* 최대 너비 제한 */
    height: auto;
    padding: 8vh 5vw;        /* 화면 높이와 너비 기준 패딩 설정 */
    margin: 10vh auto;       /* 세로로 중앙 위치 조정 */
    border: 1px solid #ccc;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  h2 {
    text-align: center;
    font-size: 2.5rem;
    margin-bottom: 3rem;
  }

  .login-form {
    margin-bottom: 2rem;
  }

  label {
    display: block;
    font-size: 1.1rem;
    margin-bottom: 0.5rem;
  }

  input[type="text"],
  input[type="password"] {
    width: 100%;
    padding: 1rem;
    font-size: 1.1rem;
    border: 1px solid #ccc;
    border-radius: 4px;
  }

  .login-button {
    width: 100%;
    padding: 1.5rem;
    font-size: 1.2rem;
    margin-top: 1.5rem;
    border: none;
    border-radius: 4px;
    color: #fff;
    cursor: pointer;
    background-color: #4CAF50;
  }

  .regist-link {
    text-align: center;
    margin-top: 2rem;
  }

  .regist-link a {
    color: #007bff;
    font-size: 1.1rem;
    text-decoration: underline;
    cursor: pointer;
  }
</style>