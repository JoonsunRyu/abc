<template>
  <div>
    <nav class="header">
      <div class="user"><span>{{ userName }}</span>님 반갑습니다</div>
      <div class="move">
        <router-link to="/home">홈</router-link>
        &nbsp;&nbsp;&nbsp;
        <router-link to="/health">운동 기록</router-link>
        &nbsp;&nbsp;&nbsp;
        <router-link to="/medicheck">수치 기록</router-link>
        &nbsp;&nbsp;&nbsp;
        <router-link to="/diet">식단 관리</router-link>
        &nbsp;&nbsp;&nbsp;
        <router-link to="/mypage">마이 페이지</router-link>
        &nbsp;&nbsp;&nbsp;
        <router-link to="/" @click="logout">로그아웃</router-link>
      </div>
    </nav>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user';
import { ref } from 'vue';
import { useRouter } from 'vue-router';  // `router.push` 사용을 위해 추가

const userObject = JSON.parse(sessionStorage.getItem('user'))
const userName = ref(userObject.name)
const userStore = useUserStore();
const router = useRouter();

// 로그아웃
const logout = () => {
  userStore.logout();  // Pinia 상태 초기화하고 sessionStorage에서 'user' 항목 제거
  router.push('/');  // 홈 화면으로 이동
};
</script>

<style scoped>
/* 모든 router-link에 대해서 밑줄 제거 및 색상 유지 */
.router-link {
  text-decoration: none !important; /* 밑줄 없애기 */
  color: inherit !important; /* 색상 변경 방지 */
}

/* 활성화된 링크에서도 밑줄과 색상 변경을 없애기 */
.router-link-active,
.router-link-exact-active {
  text-decoration: none !important; /* 밑줄 없애기 */
  color: inherit !important; 
}
.user{
  font-size: 25px;
  font-weight: 500; /* 굵은 글씨 */
  color: black;
}
.user span{
  font-size: 40px;
  font-weight: 700; /* 굵은 글씨 */
  color: black;
}
.header {
  min-height: 80px;
  padding: 20px;
  background-color: white;
  color: black;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header a {
  font-weight: bold;
  text-decoration: none;
  color: black;
}

/* 각 링크 스타일 */
.move a {
  font-size: 20px;
  font-weight: 700; /* 굵은 글씨 */
  color: black;
  margin-left: 20px; /* 링크 간격 조정 */
  transition: color 0.3s ease, transform 0.2s ease; /* 색상 및 크기 변환 효과 */
}

/* 링크 호버 효과 */
.move a:hover {
  color: #2ecc71; /* 링크 hover 시 색상 변경 */
  transform: scale(1.05); /* 살짝 확대 */
}

/* 첫 번째 링크 왼쪽 마진 제거 */
.move a:first-child {
  margin-left: 0;
}
</style>
