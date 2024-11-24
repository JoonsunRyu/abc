<template>
  <div>
    <nav>
      <HeaderNav/>
    </nav>
    
    <div class="content">
      <!-- Chart 영역 -->
      <main>
        <h2>운동 기록</h2>
        <HealthCalendar/>        
      </main>

      <aside>
        <section class="extra-content">
          <!-- 검색 -->
          <YoutubeVideoSearch/>

          <!-- 상세보기 -->
          
          <YoutubeVideoDetail/>

          <!-- 리스트 -->
          
          <YoutubeVideoList/>
        </section>
      </aside>
    </div>

    <!-- 유튭? -->
    

    <!-- 굳이 없어도 될 듯? -->
    <footer>
      <p>&copy; 2024 Medical Check App. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
  import { ref } from 'vue';
  import { useMedicalStore } from "@/stores/medicheck/medichart";
  // import { useCalendarStore } from "@/stores/medicalendar"
  import HeaderNav from "@/components/common/HeaderNav.vue";

  import YoutubeVideoList from '@/components/health/youtube/YoutubeVideoList.vue';
  import YoutubeVideoDetail from '@/components/health/youtube/YoutubeVideoDetail.vue';
  import YoutubeVideoSearch from '@/components/health/youtube/YoutubeVideoSearch.vue';
import HealthCalendar from '@/components/health/HealthCalendar.vue';

  

  const userObject = JSON.parse(sessionStorage.getItem('user'))
  const userName = ref(userObject.name)
  const store = useMedicalStore();
  const userId = JSON.parse(sessionStorage.getItem('user')).id;
  // const calStore = useCalendarStore();


</script>

<style scoped>
/* General Layout */
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* Main and Sidebar Area */
.content {
  display: flex;
  flex: 1;
  flex-wrap: wrap; /* Allow wrapping on smaller screens */
}

main {
  flex: 3;
  padding: 1rem;
  background-color: #f9f9f9;
  box-sizing: border-box;
}

aside {
  flex: 2;
  padding: 1rem;
  background-color: #f9f9f9;
  box-sizing: border-box;
  min-height: 50vh; /* Ensure the aside section takes at least 50% of the height */
}

/* Extra Content Styles (e.g., Youtube search, list, details) */
.extra-content {
  background-color: #f9f9f9;
  padding: 1rem;
  text-align: center;
  border-top: 1px solid #ddd;
  flex: 1; /* Flexibly adjust content to fill available space */
  min-height: 50vh; /* Set minimum height for better content structure */
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #ffffff;
  padding: 2rem;
  border-radius: 15px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.08);
}

/* Input Group Styles for Modal */
.input-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s ease;
}

/* Footer Styles */
footer {
  background-color: #333;
  color: white;
  text-align: center;
  padding: 0.5rem;
}

/* Responsive Styles (Mobile/Tablet) */
@media (max-width: 1024px) {
  /* Adjust the layout for medium-sized screens */
  .content {
    flex-direction: column;  /* Stack main and aside vertically on medium screens */
  }

  main {
    flex: 1;  /* Main content takes full width */
    margin-bottom: 1rem;  /* Add space below main content */
  }

  aside {
    flex: 1;  /* Sidebar adjusts to full width */
  }

  /* Adjust padding for small screens */
  .extra-content {
    min-height: 60vh; /* Increase height on tablets for better spacing */
  }

  .modal-content {
    width: 80%; /* Modal takes more width on tablets */
  }
}

@media (max-width: 768px) {
  /* Adjust layout for small screens (phones) */
  .content {
    flex-direction: column;  /* Stack main and aside vertically on small screens */
    padding: 10px;
  }

  main {
    flex: 1;
    padding: 1rem;
    margin-bottom: 1rem;
  }

  aside {
    flex: 1;
    margin-bottom: 1rem; /* Add space below aside content */
  }

  .extra-content {
    min-height: 70vh; /* Increase content height on smaller screens */
  }

  /* Footer: Ensure it stays at the bottom */
  footer {
    padding: 1rem;
  }

  /* Modal adjustments for mobile */
  .modal-content {
    width: 90%;  /* Modal takes almost full width on small screens */
    padding: 1rem;  /* Reduce padding for smaller screens */
  }
}

@media (max-width: 480px) {
  /* Further adjustments for very small screens (phones in portrait mode) */
  .search-title {
    font-size: 18px;  /* Smaller font for title */
  }

  .extra-content {
    min-height: 80vh; /* Ensure content is large enough on very small screens */
    padding: 0.5rem; /* Adjust padding */
  }

  footer {
    font-size: 12px; /* Reduce font size in footer */
  }

  /* Buttons and input fields: Smaller padding and font size */
  .input-group input {
    font-size: 0.875rem;  /* Smaller text for input fields */
    padding: 0.5rem;  /* Adjust input padding */
  }

  .save-btn, .cancel-btn {
    padding: 0.5rem 1rem;  /* Smaller buttons */
    font-size: 0.875rem;  /* Smaller text for buttons */
  }
}

</style>