<!-- YoutubeVideoList.vue-->
<template>
    <div class="video-list-container">
        <ul class="video-list">
            <YoutubeVideoListItem
                v-for="video in store.renderVideos"
                :key="video.id.videoId"
                :video="video"
            />
        </ul>

        <button
            v-if="hasMoreVideos"
            @click="loadMore"
            class="load-more-btn"
            >
            더보기
            </button>
    </div>
</template>

<script setup>
import { useYoutubeStore } from '@/stores/health/youtube';
import YoutubeVideoListItem from './YoutubeVideoListItem.vue';
import { computed } from 'vue';
const store = useYoutubeStore();

const loadMore = () => {
  store.loadMoreVideos();  // Load the next 5 videos
};

// Check if there are more videos to load
const hasMoreVideos = computed(() => {
  return store.renderVideos.length < store.videoList.length;
});

</script>

<style scoped>
.video-list-container {
  padding: 20px;
  background-color: #fafafa;  /* Light gray background */
  max-width: 80%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);  /* Subtle shadow effect */
}

.list-title {
  font-size: 24px;
  color: #333;  /* Dark text for good contrast */
  margin-bottom: 20px;
}

.video-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.load-more-btn {
  padding: 10px 20px;
  background-color: #6c63ff;
  color: white;
  border: none;
  border-radius: 50px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 20px;
}

.load-more-btn:hover {
  background-color:#5a51fd;
}
</style>

