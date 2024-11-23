import { createRouter, createWebHistory } from 'vue-router';
import { useUserStore } from '@/stores/user';

import LoginView from '@/views/LoginView.vue';
import SignupView from '@/views/SignupView.vue';
import HomeView from "@/views/HomeView.vue";
import HealthView from '@/views/HealthView.vue';
import MedicalView from '@/views/MedicalView.vue';
import DietView from '@/views/DietView.vue';
import MyPage from '@/views/MyPage.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 로그인
    {
      path: '/',
      name: 'login',
      component: LoginView,
    },
    
    // 회원가입
    {
      path: '/signup',
      name: "signup",
      component: SignupView,
    },
    
    // 메인
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { isAuth: true },
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: MyPage,
      meta: { isAuth: true },
    },

    // 기능 1 (Health)
    {
      path: '/health',
      name: 'health',
      component: HealthView,
      meta: { isAuth: true },
    },

    // 기능 2 (Medical Check)
    {
      path: '/medicheck',
      name: 'medical',
      component: MedicalView,
      meta: { isAuth: true },
    },

    // 기능 3 (Diet)
    {
      path: '/diet',
      name: 'diet',
      component: DietView,
      meta: { isAuth: true },
    },
  ],
});

// 전역 가드 설정 (로그인 안하면 화면 이동 불가)
router.beforeEach((to, from, next) => {
  const store = useUserStore();

  // isAuth == true인 라우트만 체크
  if (to.meta.isAuth && !store.loginUser) {
    const storedUser = sessionStorage.getItem('user');
    if (storedUser) {
      store.loginUser = JSON.parse(storedUser); // sessionStorage에서 로그인 정보 복원
    }
  }

  if (to.meta.isAuth && !store.loginUser) {
    next('/');  // 로그인 페이지로 리다이렉트
  } else {
    next();  // 접근 허용
  }
});

export default router;
