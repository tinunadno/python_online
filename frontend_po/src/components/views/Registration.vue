<template>
  <div>
    <div class="header">
      <button @click="goBack" class="back-button">Назад</button>
    </div>

    <div class="main">
      <div class="button-group">
        <button class="register-button" @click="registerWithEmail">
          Зарегистрироваться по почте
        </button>
        <button class="register-button" @click="registerNormally">
          Зарегистрироваться по обычному
        </button>
      </div>
      <div class="animation-block">
        <slot name="animation"></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  methods: {
    goBack() {
      this.$router.go(-1);
    },
    registerWithEmail() {
      console.log("Зарегистрироваться по почте");
    },
    registerNormally() {
      this.$router.push('/registrationdef');
    },
  },
};
</script>

<style scoped>
@import "../style/colors.css";

body {
  margin: 0;
  font-family: Arial, sans-serif;
  background: var(--bg-color);
  color: var(--text-color);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-button {
  background: var(--bg-button-color);
  color: var(--text-button-color);
  border: none;
  border-radius: 6px;
  cursor: pointer;
  padding: 10px 20px;
  transition: background 0.3s ease, color 0.3s ease;
}

.back-button:hover {
  background: var(--bg-button-color-hover);
}

.main {
  margin-top: 2em;
  padding-top: 4em;
}

.button-group {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}


.register-button {
  background: var(--bg-button-color);
  color: var(--text-button-color);
  border-radius: 8px;
  padding: 15px 30px;
  font-size: 1.2em;
  cursor: pointer;
  transition: background 0.3s ease, transform 0.2s ease;
}

.register-button:hover {
  background: var(--bg-button-color-hover);
}

.animation-block {
  margin-top: 4em;
  width: 100%;
  height: 400px;
  border: 1px solid var(--text-button-color);
  border-radius: 12px;
  display: flex;
  justify-content: center;
}

.animation-block img {
  max-width: 100%;
  max-height: 100%;
}
</style>
