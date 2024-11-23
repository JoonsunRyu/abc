<template>
  <div class="regist-container">
    <h2>회원가입</h2>
    <form @submit.prevent="signup">
      <div class="regist-form">
        <label for="userid">아이디</label>
        <input type="text" id="userid" v-model="userid" placeholder="아이디를 입력하세요" />
        <button type="button" @click="checkUserId" class="check-id-btn">아이디 중복 체크</button>
        <p v-if="isIdTaken" class="error-message">이미 사용 중인 아이디입니다.</p>
        <p v-else-if="idChecked && !isIdTaken" class="success-message">사용 가능한 아이디입니다.</p>
      </div>
      <div class="regist-form">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" placeholder="비밀번호를 입력하세요" />
      </div>
      <div class="regist-form">
        <label for="username">이름</label>
        <input type="text" id="username" v-model="username" placeholder="이름을 입력하세요" />
      </div>
      <div class="regist-form">
        <label for="useremail">이메일</label>
        <input type="email" id="useremail" v-model="useremail" placeholder="이메일을 입력하세요" />
      </div>
      <div class="regist-form">
        <label for="userage">나이</label>
        <input type="number" id="userage" v-model="userage" placeholder="나이를 입력하세요" />
      </div>
      <button type="submit" class="regist-button" :disabled="isIdTaken">가입하기</button>
    </form>
  </div>
</template>

<script setup>
  import { ref } from "vue";
  import { useUserStore } from '@/stores/user';
  
  const store = useUserStore();

  const userid = ref('');
  const password = ref('');
  const username = ref('');
  const useremail = ref('');
  const userage = ref('');
  const isIdTaken = ref(false); // To track if ID is taken
  const idChecked = ref(false); // To track if ID has been checked

  const signup = function() {
    store.userSignup(userid.value, password.value, username.value, useremail.value, userage.value)
  }

  const checkUserId = async function() {
    if (!userid.value) {
      alert("아이디를 입력하세요.");
      return;
    }
    
    const response = await store.userCheck(userid.value);
    console.log(response)
    if (response.available) {
      isIdTaken.value = false; 
    } else {
      isIdTaken.value = true; 
    }

    idChecked.value = true; 
  }
</script>

<style scoped>
  .regist-container {
    max-width: 500px;
    margin: 10vh auto;
    padding: 4rem 3rem;
    border: 1px solid #ccc;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  h2 {
    text-align: center;
    font-size: 2.5rem;
    margin-bottom: 2rem;
  }

  .regist-form {
    margin-bottom: 1.5rem;
  }

  label {
    display: block;
    font-size: 1.1rem;
    margin-bottom: 0.5rem;
  }

  input[type="text"],
  input[type="password"],
  input[type="email"],
  input[type="number"] {
    width: 100%;
    padding: 0.75rem;
    font-size: 1.1rem;
    border: 1px solid #ccc;
    border-radius: 4px;
  }

  .regist-button {
    width: 100%;
    padding: 1rem;
    font-size: 1.2rem;
    margin-top: 1rem;
    border: none;
    border-radius: 4px;
    color: #fff;
    cursor: pointer;
    background-color: #4CAF50;
  }

  .check-id-btn {
    margin-top: 1rem;
    padding: 0.75rem;
    font-size: 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .check-id-btn:hover {
    background-color: #0056b3;
  }

  .error-message {
    color: red;
    font-size: 0.9rem;
    margin-top: 0.5rem;
  }

  .success-message {
    color: green;
    font-size: 0.9rem;
    margin-top: 0.5rem;
  }
</style>
