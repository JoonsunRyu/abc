<template>
  <div class="modify-container">
    <h2>마이 페이지</h2>
    <form @submit.prevent="modify">
      <div class="modify-form">
        <label for="userid">아이디</label>
        <input type="text" id="userid" v-model="userid" readonly/>
      </div>
      <div class="modify-form">
        <label for="prepassword">이전 비밀번호</label>
        <input type="password" id="prepassword" v-model="prepassword" placeholder="이전 비밀번호를 입력하세요" />
        <button type="button" @click="checkPreviousPassword">이전 비밀번호 확인</button>
      </div>
      <div class="modify-form">
        <label for="postpassword">새로운 비밀번호</label>
        <input 
          type="password" 
          id="postpassword" 
          v-model="postpassword" 
          placeholder="새로운 비밀번호를 입력하세요" 
          :readonly="!passwordVerified"
        />
      </div>

      <div class="modify-form">
        <label for="postpasswordcheck">새로운 비밀번호 확인</label>
        <input 
          type="password" 
          id="postpasswordcheck" 
          v-model="postpasswordcheck" 
          placeholder="새로운 비밀번호를 입력하세요" 
          :readonly="!passwordVerified"
        />
      </div>

      <!-- 비밀번호 일치 메시지: 두 값이 비어 있지 않을 때만 표시 -->
      <p v-if="prepassword&& prepassword == postpassword" class="error-pwMessage"> 이전 비밀번호가 같습니다 다른 비밀번호로 변경하세요.</p>
      <p v-else-if="postpassword && postpasswordcheck && newPwChecked" class="success-pwMessage">비밀번호가 일치합니다.</p>
      <p v-else-if="postpassword && postpasswordcheck && !newPwChecked" class="error-pwMessage">비밀번호가 일치하지 않습니다.</p>
        <!-- 비밀번호 확인란이 비어 있을 경우 메시지가 뜨지 않도록 처리 -->
        <div class="modify-form">
          <label for="username">이름</label>
          <input type="text" id="username" v-model="username" placeholder="이름을 입력하세요" />
        </div>
        <div class="modify-form">
          <label for="useremail">이메일</label>
          <input type="email" id="useremail" v-model="useremail" placeholder="이메일을 입력하세요" />
        </div>
        <div class="modify-form">
          <label for="userage">나이</label>
          <input type="number" id="userage" v-model="userage" placeholder="나이를 입력하세요" />
        </div>
        <button type="submit" class="modify-button" :disabled="!newPwChecked">수정하기</button>
      
    </form>
    <button type="submit" @click="deleteUser" class="delete-button" :disabled="!passwordVerified">탈퇴하기</button>
  </div>
</template>

<script setup>
  import { ref, computed, watch } from "vue";
  import { useUserStore } from '@/stores/user';
  import router from "@/router";

  const store = useUserStore();

  const prepassword = ref('');
  const postpassword = ref('');
  const postpasswordcheck = ref('');
  const userid = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user?.id;
  });
  const username = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user?.name;
  });
  const useremail = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user?.email;
  });
  const userage = computed(() => {
    const user = JSON.parse(sessionStorage.getItem('user'));
    return user?.age;
  });

  const passwordVerified = ref(false); // 이전 비밀번호가 맞는지 여부
  const newPwChecked = ref(false); // 새로운 비밀번호 일치 여부

  // 비밀번호 일치 여부 확인
  watch([postpassword, postpasswordcheck], () => {
    newPwChecked.value = (postpassword.value === postpasswordcheck.value) && (prepassword.value !== postpassword.value);
  });

  const isValidPassword = ref(false)
  // 이전 비밀번호 확인 함수
  const checkPreviousPassword = async () => {
    try {
      console.log(prepassword.value)
      await store.userPwCheck(userid.value, prepassword.value)
      isValidPassword.value = store.checkPw
      console.log(isValidPassword.value)
      if (isValidPassword.value) {
        passwordVerified.value = true;
        alert("이전 비밀번호가 확인되었습니다.");
      } else {
        passwordVerified.value = false;
        alert("이전 비밀번호가 일치하지 않습니다.");
      }
    } catch (error) {
      console.error(error);
      passwordVerified.value = false;
      alert("서버와의 통신 오류가 발생했습니다.");
    }
  };
  
  // 수정 함수
  const modify = function() {
    if (newPwChecked.value) {
      store.userModify(userid.value, postpassword.value, username.value, useremail.value, userage.value);
    }
  };

  // 탈퇴 함수
  const deleteUser = async () => {
    const confirmflag = confirm("정말로 탈퇴하시겠습니까?");
    if (confirmflag) {
      await store.userExit(userid.value); // 탈퇴 요청
      alert("탈퇴하였습니다.");
      router.push("/"); // 홈 화면으로 이동
    } else {
      alert("탈퇴를 취소했습니다.");
    }
  };
</script>


<style scoped>
  .modify-container {
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

  .modify-form {
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

  .modify-button {
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
  .delete-button {
    width: 100%;
    padding: 1rem;
    font-size: 1.2rem;
    margin-top: 1rem;
    border: none;
    border-radius: 4px;
    color: #fff;
    cursor: pointer;
    background-color: #cf2727;
  }

  .modify-button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  .delete-button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }

  .success-pwMessage {
    color: green;
    font-size: 0.9rem;
    margin-top: 0.5rem;
  }

  .error-pwMessage {
    color: red;
    font-size: 0.9rem;
    margin-top: 0.5rem;
  }

  .error-message {
    color: red;
    font-size: 1rem;
    margin-top: 1rem;
  }

  button {
    margin-top: 1rem;
    padding: 0.5rem;
    font-size: 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
</style>
