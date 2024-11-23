<template>
    <div class="search-container">
        <h4 class="search-title">유튜브 검색</h4>
        <div class="search-input-wrapper">
            <input type="text" v-model.trim="keyword" class="search-input" placeholder="검색어를 입력하세요" />
            <button @click="search" class="search-button">
                <i class="fa fa-search"></i> 검색
            </button>
        </div>
    </div>
</template>

<script setup>
import { useYoutubeStore } from '@/stores/health/youtube';
import { ref } from 'vue';

const keyword = ref('')
const store = useYoutubeStore();  

const search = function () {
    store.videoList=[];
    store.youtubeSearch(keyword.value);  
    store.selectVideo=null;
}
</script>

<style scoped>
.search-container {
  display: flex;
  flex-direction: column;
  max-width: 80%;
  align-items: center;
  padding: 20px;
  background-color: #f8f8f8;  /* Light gray background */
 
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);  /* Subtle shadow for modern look */
}

.search-title {
  font-size: 24px;
  color: #333;  /* Dark text for better contrast on light background */
  margin-bottom: 15px;
}

.search-input-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

.search-input {
  padding: 12px 18px;
  font-size: 16px;
  border: 1px solid #ccc;  /* Lighter border */
  border-radius: 30px;  /* Rounded corners */
  outline: none;
  background-color: #fff;  /* White background for the input */
  color: #333;  /* Dark text for readability */
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: #6c63ff;  /* Soft purple on focus */
  box-shadow: 0 0 5px rgba(108, 99, 255, 0.3);  /* Subtle shadow effect on focus */
}

.search-button {
  margin-left: 12px;
  padding: 12px 24px;
  font-size: 16px;
  background-color: #6c63ff;  /* Soft purple for the button */
  border: none;
  border-radius: 30px;  /* Rounded corners */
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
  flex-shrink: 0;  /* 화면 축소해도 모양 유지 */
}

.search-button:hover {
  background-color: #5a52cc;  /* Darker shade on hover */
}
</style>
