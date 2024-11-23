<!-- YoutubeVideoDetail.vue-->
<template>
    <div v-if="store.selectVideo" class="video-detail-container">
        <iframe 
            width="100%" 
            height="500px" 
            :src="videoURL"
            title="YouTube video player" 
            frameborder="0" 
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
            referrerpolicy="strict-origin-when-cross-origin" 
            allowfullscreen
        ></iframe>
    </div>
</template>

<script setup>
import { useYoutubeStore } from '@/stores/health/youtube';
import { computed } from 'vue';

const store = useYoutubeStore();

const videoURL = computed(() => {
    const videoSrc = store.selectVideo.id.videoId;
    return `https://www.youtube.com/embed/${videoSrc}`;
});
</script>

<style scoped>
.video-detail-container {
  
  padding: 20px;
  border-radius: 12px;
  max-width: 1200px;
  margin: 20px auto;
  text-align: center;
}


</style>
