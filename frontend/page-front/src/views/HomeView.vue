<template>
  <div class="main-container">
    <HeaderNav/>

    <!-- Main Section (이미지 3개) -->
    <main class="main-content">
      <div class="img-main" @click="navigateTo('/health')">
        <img src="@/assets/img/running.png" alt="running" />
      </div>
      <div class="img-main" @click="navigateTo('/medicheck')">
        <img src="@/assets/img/medical.jpg" alt="medical" />
      </div>
      <div class="img-main" @click="navigateTo('/diet')">
        <img src="@/assets/img/salad.jpg" alt="menu" />
      </div>
    </main>

    <!-- Footer Section -->
    <footer class="footer">
      <p>&copy; 2024 Your Company</p>
      <!-- 여기에 원하는 푸터 내용을 추가하세요 -->
    </footer>
  </div>
</template>

<script setup>
  import { useRouter } from 'vue-router';
  import HeaderNav from "@/components/common/HeaderNav.vue";

  const router = useRouter();

  const navigateTo = (route) => {
    router.push(route);
  }
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  display: flex;
  flex: 1;
  justify-content: space-around;
  align-items: center;
  padding: 20px;
}

.img-main {
  position: relative;
  cursor: pointer;
  text-align: center;
  overflow: hidden;
}

.img-main img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  transition: transform 0.3s ease;  /* 이미지 확대 효과 */
}

.img-main::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 검정 */
  opacity: 0; /* 초기에는 투명 */
  transition: opacity 0.3s ease; /* hover 시 부드럽게 변화 */
}

.img-main:hover img {
  transform: scale(1.1); /* 이미지 약간 확대 */
}

.img-main:hover::after {
  opacity: 1; /* hover 시 검정색 오버레이 활성화 */
}

.footer {
  padding: 10px;
  background-color: #333;
  color: #fff;
  text-align: center;
}
</style>
