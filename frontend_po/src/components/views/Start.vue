<script>
  import PythonEditor from "@/components/parts/PythonEditor.vue";

  export default {
    components: {PythonEditor},

    data() {
      return {
        isMenuOpen: false, // Управление состоянием меню
        isDarkTheme: false, // Управление текущей темой
        code: '# Напишите ваш Python-код здесь\nprint("Hello, World!")',
        editorOptions: {
          mode: 'python',
          theme: '3024-day',
          lineNumbers: true,
        },
      };
    },
    mounted() {
      // Проверяем сохранённую тему при загрузке страницы

      const savedTheme = localStorage.getItem("theme");
      this.isDarkTheme = savedTheme === "dark";
      this.applyTheme();


    },
    methods: {
      toggleMenu() {
        this.isMenuOpen = !this.isMenuOpen;
      },
      closeMenu() {
        this.isMenuOpen = false;
      },
      toggleTheme() {
        this.isDarkTheme = !this.isDarkTheme;
        localStorage.setItem("theme", this.isDarkTheme ? "dark" : "light");
        this.applyTheme();
      },
      applyTheme() {
        // Применение темы через CSS-классы
        document.body.classList.toggle("dark-theme", this.isDarkTheme);
        this.$refs.codeEditor.setTheme(this.isDarkTheme ? "dracula": "3024-day");
      },
      login() {
        this.$router.push('/login');
      },
      registration() {
        this.$router.push('/registration');
      },
    }
  };
</script>

<template>
  <div id="app" :class="{ 'dark-theme': isDarkTheme }">
    <div :class="['side-menu', { 'side-menu--open': isMenuOpen }]">
      <div>
        <button @click="toggleMenu" class="close-button">x</button>
      </div>
      <nav>
        <ul>
          <li>Что-то</li>
          <li>Что-то</li>
          <li>Что-то</li>
          <li>Что-то</li>
        </ul>
      </nav>
    </div>
    <div :class="{ overlay: isMenuOpen }" @click="closeMenu"></div>

    <div class="header">
      <div class="menu">
        <button @click="toggleMenu" class="menu-button">
          ☰ Меню
        </button>
        <button @click="toggleTheme" class="theme-toggle">
          {{ isDarkTheme ? '🌞' : '🌙' }}
        </button>
      </div>
      <div class="reg-log">
        <button @click="registration" class="login-button">
          Войти
        </button>
        <button @click="login" class="register-button">
          Зарегистрироваться
        </button>
      </div>
    </div>

    <div class="main">
      <h1 class="name">Python Code Editor</h1>
      <PythonEditor
          ref="codeEditor"
          v-model="code"
          :options="editorOptions"
      />
    </div>
  </div>
</template>

<style>

:root {
  --bg-color: #ffffff;
  --bg-button-color: #333333;
  --bg-button-color-hover: #222222;
  --text-button-color: #ffffff;
  --text-color: #333333;
}

body.dark-theme {
  --bg-color: #333333;
  --bg-button-color: #ffffff;
  --bg-button-color-hover: #dddddd;
  --text-button-color: #333333;
  --text-color: #ffffff;
}

body {
  margin: 0;
  padding: 1em;
  font-family: Arial, sans-serif;
  background: var(--bg-color);
  color: var(--text-color);
}

#app {
  position: relative;
}
.header{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.main{
  display: flex;
  flex-direction: column;
  width: 70%;
  margin: 5em auto auto;
}

.main h1{
  text-align: center;
}

.menu{
  display: flex;
  flex-direction: row;
}


button{
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
  transition: background-color 0.3s ease, color 0.3s ease;
}


.menu-button, .login-button, .register-button{
  border: none;
  padding: 10px 20px;
}

.register-button{
  margin-left: 1em;
  color: var(--bg-button-color);
  background: var(--text-button-color);
  border: 1px solid var(--bg-button-color);
}


.side-menu {
  position: fixed;
  top: 0;
  left: -250px;
  width: 250px;
  height: 100%;
  background-color: var(--bg-button-color);
  color: var(--text-button-color);
  overflow: auto;
  transition: left 0.3s ease;
  z-index: 1100;
}


.side-menu ul {
  list-style: none;
  padding: 0;
}

.side-menu li {
  margin: 1em;
  padding: 0.8em;

  background: #007bff;
}

.side-menu a:hover {
  background-color: var(--bg-button-color-hover);
}

.close-button {
  margin: 1em 1em;
  //background: blueviolet;
  color: var(--text-button-color);
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* Полупрозрачный фон */
  z-index: 1000; /* Выше большинства элементов, но ниже меню */
}

.editor {
  border: 1px solid #ddd;
  font-family: monospace;
  height: 300px;
  overflow: auto;
}

</style>