import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import router from '@/router'


export const useYoutubeStore = defineStore('youtube', () => {
  
  const videoList = ref([])
  const renderVideos = ref([]);     
  const currentPage = ref(0);    

  const youtubeSearch = function (keyword) {
    const url = 'https://www.googleapis.com/youtube/v3/search'
    const API_KEY = import.meta.env.VITE_YOUTUBE_API_KEY
    axios({
      url,
      method: 'GET',
      params: {
        key: API_KEY,
        part: 'snippet',
        maxResults: 20,
        q: keyword,
        type: 'video',

      }
    })
      .then((response) => {
        videoList.value = response.data.items
        renderVideos.value = videoList.value.slice(0,5)
        currentPage.value=1
      })
      .catch(()=>{})
  }

  const selectVideo = ref(null)

  const loadMoreVideos = function () {
    const nextPage = currentPage.value * 5;
    const nextVideos = videoList.value.slice(nextPage, nextPage + 5);
    renderVideos.value.push(...nextVideos);  // Add the next 5 videos to the current list
    currentPage.value += 1;  // Increment the page number
  };

  const clickVideo = function (video) {
    selectVideo.value=video
  }

  return { 
      youtubeSearch, videoList,selectVideo,clickVideo,loadMoreVideos,renderVideos,currentPage,
   }
})