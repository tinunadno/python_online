<script>
import PythonEditor from "@/components/parts/PythonEditor.vue";
import Menu from "@/components/parts/menu.vue";

export default {
  components: { Menu, PythonEditor },

  data() {
    return {
      isMenuOpen: false,
      isDarkTheme: false,
      code: 'print("Hello, World!")',
      commandArgs: "", // Аргументы командной строки
      consoleOutput: "", // Вывод в консоль
      editorOptions: {
        mode: 'python',
        theme: '3024-day',
        lineNumbers: true,
      },
    };
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
    toggleTheme() {
      this.isDarkTheme = !this.isDarkTheme;
      localStorage.setItem("theme", this.isDarkTheme ? "dark" : "light");
      this.applyTheme();
    },
    applyTheme() {
      document.body.classList.toggle("dark-theme", this.isDarkTheme);
      this.$refs.codeEditor.setTheme(this.isDarkTheme ? "dracula" : "3024-day");
    },
    login() {
      this.$router.push('/login');
    },
    info() {
      this.$router.push('/info');
    },
    registration() {
      this.$router.push('/registration');
    },
    runCode() {
      const output = `>>> Аргументы: ${this.commandArgs}\n>>> Результат: Hello, World!`;
      this.consoleOutput = output;
    },
  },
};
</script>


<template>
  <div id="app" :class="{ 'dark-theme': isDarkTheme, 'menu-open': isMenuOpen }">
    <!-- Меню -->
    <div class="side-menu">
      <div class="menu-header">
        <h2 @click="info"><b>LPL</b></h2>
        <button @click="toggleMenu" class="close-button"><-</button>
      </div>

      <Menu />
    </div>

    <!-- Основной контент -->
    <div class="content">
      <div class="header">
        <div class="menu">
          <button
              @click="toggleMenu"
              class="menu-button"
              :class="{ 'collapsed': isMenuOpen }"
          >
            Меню
          </button>
          <button @click="toggleTheme" class="theme-toggle">
            {{ isDarkTheme ? '🌞' : '🌙' }}
          </button>
        </div>
        <div class="reg-log">
          <button @click="login" class="login-button">Войти</button>
          <button @click="registration" class="register-button">Зарегистрироваться</button>
        </div>
      </div>

      <div class="main">
        <h1 class="name">Python Code Editor</h1>
        <PythonEditor ref="codeEditor" class="code-editor" v-model="code" :options="editorOptions" />
        <div class="to-execute">
          <button @click="runCode">Run</button>
          <button>$</button>
          <input type="text" id="command" placeholder="Command Line Arguments...">
        </div>

        <!-- Консольный вывод -->
        <div class="console-output">
          <pre>{{ consoleOutput }}</pre>
        </div>
      </div>

    </div>
  </div>
</template>

<style>
@import "../style/colors.css";

body {
  margin: 0;
  padding: 1em;
  font-family: Arial, sans-serif;
  background: var(--bg-color);
  color: var(--text-color);
  overflow-x: hidden; /* Чтобы избежать горизонтального скролла */
}

#app {
  position: relative;
  transition: margin-left 0.3s ease; /* Анимация изменения отступа */
}

#app.menu-open {
  margin-left: 250px; /* Сдвигаем контент вправо, освобождая место для меню */
}

.side-menu {
  position: fixed;
  top: 0;
  left: -250px; /* Меню скрыто за пределами экрана */
  width: 250px;
  height: 100%;
  background-color: var(--bg-color-darker);
  color: var(--bg-button-color);
  transition: left 0.3s ease; /* Анимация открытия меню */
  z-index: 1000;
}

.menu-header {
  display: flex;
  justify-content: space-between;
  margin: 1em;
  align-content: center;
}


.menu-header h2{
  height: 100%;
  width: 100%;
  margin: auto;
}

#app.menu-open .side-menu {
  left: 0; /* Меню появляется */
}

.content {
  width: 100%;
  transition: width 0.3s ease; /* Анимация изменения ширины контента */
}

.header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.main {
  display: flex;
  flex-direction: column;
  width: 70%;
  margin: 5em auto auto;
}

.main h1 {
  text-align: center;
}

.menu {
  display: flex;
  flex-direction: row;
}

button {
  background: var(--bg-button-color);
  color: var(--text-button-color);
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s ease, color 0.3s ease;
}

.theme-toggle {
  background: none;
  font-size: 14px;
  margin-left: 1em;
}

.menu-button,
.login-button,
.register-button {
  border: none;
  padding: 10px 20px;
}

.menu-button{
  transition: width 0.3s ease, padding 0.3s ease; /* Анимация ширины и отступов */
  width: 100px; /* Начальная ширина кнопки */
}

.menu-button.collapsed {
  width: 0; /* Ширина кнопки при схлопывании */
  padding: 0; /* Убираем отступы */
  margin-right: 0;
}

.register-button {
  margin-left: 1em;
  color: var(--bg-button-color);
  background: var(--text-button-color);
  border: 1px solid var(--bg-button-color);
}

.close-button {
  color: var(--text-button-color);
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.to-execute {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
  padding: 10px;
  background-color: var(--bg-color-darker);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.to-execute button {
  background-color: var(--bg-button-color);
  color: var(--text-button-color);
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
  transition: background-color 0.3s ease;
}

.to-execute button:hover {
  background-color: var(--bg-button-color-hover);
}

.to-execute input[type="text"] {
  padding: 10px;
  border: 1px solid var(--bg-button-color);
  border-radius: 4px;
  flex-grow: 1;
  background-color: var(--bg-color);
  color: var(--text-color);
  font-family: monospace;
}

.to-execute input[type="text"]::placeholder {
  //color: var(--text-color-secondary);
}

.console-output {
  margin-top: 20px;
  padding: 15px;
  background-color: var(--bg-color-darker);
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  color: var(--text-color);
  font-family: monospace;
  white-space: pre-wrap; /* Сохраняет форматирование текста и переносит строки */
  overflow-x: auto; /* Добавляет горизонтальный скролл, если текст слишком длинный */
  max-height: 300px; /* Ограничивает высоту консоли */
  overflow-y: auto; /* Добавляет вертикальный скролл, если текст не помещается */
}

.console-output pre {
  margin: 0; /* Убирает отступы внутри <pre> */
}
</style>