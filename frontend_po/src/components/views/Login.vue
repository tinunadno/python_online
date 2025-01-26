<template>
  <div>
    <!-- Кнопка "Назад" за пределами основного блока -->
    <button class="back_button" @click="goBack">Назад</button>

    <div class="main_block">
      <div>
        <h1 class="login_text">
          Вход
        </h1>
      </div>
      <div>
        <form class="login_form">
          <div class="input_group">
            <label for="login">Логин: </label>
            <input type="text" v-model="login" class="text_field" id="login" name="login" />
          </div>

          <div class="input_group">
            <label for="password">Пароль: </label>
            <input type="password" v-model="password" class="text_field" id="password" name="password" />
          </div>

          <div class="button_group">
            <button :class="{ shake: disabled }" class="button" @click="logIn">Войти</button>
          </div>
          <div class="button_group">
            <button type="button" class="button">Войти через GOOGLE</button>
          </div>
          <div>
            <label class="result_message">{{ result_message }}</label>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {useUserStore} from "@/store/user.js";

export default {
  data() {
    const userStore = useUserStore();


    return {
      result_message: 'введите логин и пароль',
      disabled: true,
      login: "",
      password: ""
    };
  },
  methods: {
    goBack() {
      this.$router.push('/');
    },
    async logIn (event) {
      event.preventDefault(); //

      const userData = {
        username: this.login,
        password: this.password
      };

      const userStore = useUserStore();
      await userStore.login(userData);

    },
  },
};
</script>

<style scoped>
@import "../style/form.css";

/* Кнопка "Назад" */
.back_button {
  position: fixed; /* Фиксация в углу экрана */
  top: 1.2em;
  left: 1.2em;
  background: var(--bg-button-color);
  color: var(--text-button-color);
  border: none;
  border-radius: 4px;
  padding: 10px 15px;
  cursor: pointer;
  transition: background 0.3s;
  z-index: 1000; /* Поверх всех элементов */
}

.back_button:hover {
  background: var(--bg-button-color-hover);
}

.main_block {
  display: flex;
  flex-direction: column;
  background: var(--bg-color);
  color: var(--text-color);
  width: 70%;
  max-width: 500px;
  margin: 50px auto;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.login_text {
  text-align: center;
  margin-bottom: 20px;
}

.input_group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.text_field {
  padding: 10px;
  border: 1px solid var(--bg-button-color);
  border-radius: 4px;
  font-size: 16px;
  background: var(--bg-color);
  color: var(--text-color);
}

.text_field:focus {
  outline: none;
  border-color: var(--bg-button-color-hover);
}

.button {
  background: var(--bg-button-color);
  color: var(--text-button-color);
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.button:hover:not(:disabled) {
  background: var(--bg-button-color-hover);
}

.result_message {
  display: block;
  text-align: center;
  color: var(--text-color);
}
</style>
