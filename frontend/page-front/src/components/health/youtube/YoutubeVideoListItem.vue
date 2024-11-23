<template>
  <li class="video-item" @click="clickVideo">
      <img :src="video.snippet.thumbnails.default.url" alt="" class="video-thumbnail" />
      <div class="video-info">
          <span class="video-title">{{ videoTitle }}</span>
      </div>
  </li>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useYoutubeStore } from '@/stores/health/youtube';
import _ from 'lodash';

const store = useYoutubeStore();
const props = defineProps({
  video: Object
});

const videoTitle = computed(() => {
  return _.unescape(props.video.snippet.title);
});

const clickVideo = function () {
  store.clickVideo(props.video);
}
</script>

<style scoped>
.video-item {
display: flex;
align-items: center;
padding: 15px;
background-color: #ffffff;
border-radius: 12px;
margin: 10px 0;
cursor: pointer;
transition: transform 0.3s ease, box-shadow 0.3s ease;
box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
width: 100%; /* Ensure full width of parent container */
max-width: 90%; /* Max width to control size */
height: 100px; /* Fixed height for each list item */
overflow: hidden; /* Prevent content overflow */
}

.video-item:hover {
transform: translateY(-5px);
box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.video-thumbnail {
width: 120px;
height: 80px;
border-radius: 8px;
margin-right: 15px;
}

.video-info {
display: flex;
flex-direction: column;
justify-content: center;
flex-grow: 1; /* Allow title area to take up remaining space */
}

.video-title {
color: #333;
font-size: 14px; /* Adjust font size */
font-weight: bold;
line-height: 1.2;
text-overflow: ellipsis; /* Ensure overflow is hidden with ellipsis */
white-space: nowrap; /* Prevent text wrapping */
overflow: hidden; /* Ensure no text overflow */
max-width: 220px; /* Limit title width */
}
</style>
