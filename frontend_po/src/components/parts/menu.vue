<script setup>
import { ref } from 'vue';

// Исходный список слов для поиска
const initialList = ref([
  { name: "Яблоко", requiresPassword: false },
  { name: "Банан", requiresPassword: false },
  { name: "Апельсин", requiresPassword: false },
  { name: "Груша", requiresPassword: false },
  { name: "Ананас", requiresPassword: false },
  { name: "Манго", requiresPassword: false },
  { name: "Манго1", requiresPassword: true }, // Требует пароль
  { name: "Манго2", requiresPassword: true }, // Требует пароль
  { name: "Манго3", requiresPassword: true }, // Требует пароль
  { name: "Киви", requiresPassword: false },
  { name: "Виноград", requiresPassword: false },
  { name: "Лимон", requiresPassword: false },
  { name: "Персик", requiresPassword: false },
]);

// Поисковый запрос
const searchQuery = ref("");

// Результаты поиска
const searchResults = ref([]);

// Состояние загрузки
const isLoading = ref(false);

// Активные элементы списка (для отображения поля ввода пароля)
const activeItems = ref({});

// Пароли для каждого элемента
const passwords = ref({});

// Функция для выполнения поиска
const performSearch = () => {
  if (searchQuery.value.trim() === "") {
    // Если запрос пустой, показываем весь список
    searchResults.value = initialList.value;
    return;
  }

  // Включаем анимацию загрузки
  isLoading.value = true;

  // Симуляция задержки поиска (например, запрос к API)
  setTimeout(() => {
    // Фильтруем список по запросу
    searchResults.value = initialList.value.filter((item) =>
        item.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    );

    // Выключаем анимацию загрузки
    isLoading.value = false;
  }, 1000); // Задержка 1 секунда
};

// Обработчик клика по элементу списка
const handleItemClick = (item) => {
  if (item.requiresPassword) {
    // Если требуется пароль, переключаем состояние элемента
    activeItems.value[item.name] = !activeItems.value[item.name];
  } else {
    // Иначе просто выводим элемент в консоль
    console.log("Выбранный элемент:", item.name);
  }
};

// Обработчик отправки пароля
const submitPassword = (item) => {
  const itemPassword = passwords.value[item.name] || "";
  if (itemPassword === "1234") { // Пример пароля
    console.log("Пароль верный! Выбранный элемент:", item.name);
    activeItems.value[item.name] = false; // Скрываем поле ввода пароля
    passwords.value[item.name] = ""; // Очищаем поле ввода
  } else {
    alert("Неверный пароль!");
  }
};
</script>

<template>
  <div class="main-block">
    <!-- Поле поиска и кнопка -->
    <div class="search-container">
      <input
          v-model="searchQuery"
          type="text"
          placeholder="Введите запрос"
          class="search-input"
          @keyup.enter="performSearch"
      />
      <button @click="performSearch" class="search-button" :disabled="isLoading">
        <!-- Иконка лупы или анимация загрузки -->
        <span v-if="!isLoading" class="search-icon">
          <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
          >
            <circle cx="11" cy="11" r="8"/>
            <line x1="21" y1="21" x2="16.65" y2="16.65"/>
          </svg>
        </span>
        <span v-else class="loading-icon"></span> <!-- Анимация загрузки -->
      </button>
    </div>

    <!-- Список результатов -->
    <ul class="results-list">
      <li
          v-for="(item, index) in searchResults"
          :key="index"
          @click="handleItemClick(item)"
          class="result-item"
      >
        {{ item.name }}
        <!-- Поле для ввода пароля с анимацией -->
        <transition name="slide">
          <div v-if="item.requiresPassword && activeItems[item.name]" class="password-prompt" @click.stop>
            <input
                v-model="passwords[item.name]"
                type="password"
                placeholder="Введите пароль"
                class="password-input"
                @keyup.enter="submitPassword(item)"
                @click.stop
            />
          </div>
        </transition>
      </li>
    </ul>
  </div>
</template>

<style scoped>
@import "../style/colors.css";

.main-block {
  padding: 1em;
}

/* Контейнер для поиска */
.search-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

/* Поле ввода */
.search-input {
  width: 70%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

/* Кнопка поиска */
.search-button {
  padding: 8px;
  background-color: var(--bg-button-color);
  color: var(--text-button-color);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s ease;
}

.search-button:disabled {
  cursor: not-allowed;
  opacity: 0.7;
}

.search-button:hover:not(:disabled) {
  background-color: var(--bg-button-color-hover);
}

/* Иконка лупы */
.search-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-icon svg {
  width: 20px;
  height: 20px;
}

/* Анимация загрузки */
.loading-icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid #ccc;
  border-top: 2px solid var(--bg-button-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* Список результатов */
.results-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

/* Элемент списка */
.result-item {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  position: relative;
}

.result-item:hover {
  border-color: #a6a6a6;
}

/* Поле для ввода пароля */
.password-prompt {
  margin-top: 10px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.password-input {
  padding: 4px;
  width: 50%;
  border: 1px solid #ccc;
  border-radius: 4px;
  flex-grow: 1;
}

/* Анимация для плавного выезжания и заезжания */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
}
</style>