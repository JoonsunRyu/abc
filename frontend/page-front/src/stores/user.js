// stores/user.js
import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import router from '@/router'
import { useOpenAIStore } from './openAI'

const REST_API_URL = `http://localhost:8080/user`

export const useUserStore = defineStore('user', () => {
  const loginUser = ref(null)
  const openAIStore = useOpenAIStore()
  let checkPw = ref(false)
  /* 로그인 */
  const userLogin = async function(id, password) {
    // 입력값이 없을 때 뜨는 alert창
    if (!id) {
      alert("아이디를 입력하세요.")
      return;
    }
    if (!password) {
      alert("비밀번호를 입력하세요.")
      return;
    }
    await openAIStore.clearConversation()
    // 정상적으로 입력하면 서버로 데이터 전송
    axios.post(`${REST_API_URL}/login`, {
      id: id,
      password: password
    })

    // 성공하면 메인 화면으로 이동
    .then((response) => {

      loginUser.value = response.data;
      sessionStorage.setItem("user",JSON.stringify(loginUser.value))
      router.push('/home');
    })

    // 로그인 실패 시 alert창
    .catch((err) => {
      alert("로그인 정보가 일치하지 않습니다.")
    })
  }

  /* id 중복체크 */
  const userCheck = async function(id) {
    try {
      const response = await axios.get(`${REST_API_URL}/${id}`);
      
      if (response.status === 200 && response.data) {
        return { available: false, message: "이미 사용 중인 아이디입니다." };
      } else {
        return { available: true, message: "사용 가능한 아이디입니다." };
      }
    } catch (error) {
      console.error("Error checking ID:", error);
      return { available: false, message: "서버 오류 발생" };
    }
  }

  /* 회원가입 */
  const userSignup = function(id, password, name, email, age) {
    // 하나라도 빈칸이 있다면
    if (!id || !password || !name || !email || !age) {
      alert("모든 칸을 입력하세요.")
      return;
    }

    // 정상적으로 입력하면 서버로 데이터 전송
    axios.post(`${REST_API_URL}/signup`, {
      id: id,
      password: password,
      name: name,
      email: email,
      age: age,
    })

    .then((response) => {
      alert("회원가입이 완료되었습니다.")
      router.push("/")
    })
  }
  /* 로그아웃 */
  const logout = async () => {
    await openAIStore.clearConversation()
    loginUser.value = null;  // Pinia 상태 초기화
    sessionStorage.removeItem('user');  
    router.push('/');  // 홈 화면으로 이동
  };

  /* 유저 정보 수정*/
  const userModify = function(id, password, name, email, age) {
    // 하나라도 빈칸이 있다면
    if (!id || !password || !name || !email || !age) {
      alert("모든 칸을 입력하세요.")
      return;
    }

    // 정상적으로 입력하면 서버로 데이터 전송
    axios.put(`${REST_API_URL}/${id}`, {
      id: id,
      password: password,
      name: name,
      email: email,
      age: age,
    })

    .then((response) => {
      alert("회원 정보가 수정되었습니다.")
      checkPw.value=false
      router.push("/")
    })
  }

  // 비밀번호 확인
  const userPwCheck = async function(id, password) {
    try {
      const response = await axios.post(`${REST_API_URL}/login`, {
        id: id,
        password: password
      });
      
      // 로그인 성공 시 response가 있을 경우, 비밀번호가 일치하면 true 반환
      
      if (response.status === 202) {
        checkPw.value = true
      }
    
    } catch (err) {
      console.error("비밀번호 확인 오류:", err);
      return false; // 실패 시 false 반환
    }
  }
  

  // 유저 탈퇴하기
  const userExit = async function(id) {
    try {
        const response = await axios.delete(`${REST_API_URL}/${id}`);
    } catch (error) {
        console.error("Error during user deletion:", error);
        alert(`탈퇴 실패: ${error.response?.data?.message || error.message}`);
        
    }
}


  return {
    loginUser,
    userLogin,
    userSignup,
    logout,
    userCheck,
    userModify,
    userPwCheck,
    userExit,
    checkPw,
  }

})
