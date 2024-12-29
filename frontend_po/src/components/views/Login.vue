<script >
export default {
  data(){
    return{
      result_message: '',
      disabled: true,
    }
  }
}
</script>

<template>
  <div class="main_block">
    <div>
      <h1 class="login_text">
        Вход
      </h1>
    </div>
    <div>
      <form class="login_form">
        <div>
          <label>Логин: </label>
          <input type="text" v-model="login" class="text_field" id="login" name="login" />
        </div>

        <div>
          <label>Пароль: </label>
          <input type="password" v-model="password" class="text_field" id="password" name="password" />
        </div>

        <div>
          <button :class="{ shake: disabled }" class="button">Войти</button>
        </div>
        <div>
          <label class="result_message" >{{result_message}}</label>
        </div>
        <div>
          <button class="login_by_google"> Войти через GOOGLE</button>
        </div>
      </form>
    </div>
  </div>

</template>

<style scoped>
  @import "../style/form.css";

  .login_text{
    text-align: center;
  }

  .main_block{
    display: flex;
    flex-direction: column;
    background: #003366;
    width: 70%;
    height: 100%;
    margin: auto;
  }

  .main_block div {
    display: flex;
    flex-direction: row;
    width: 100%;
    margin: auto;
  }

  .shake {
    animation: shake 0.82s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
    transform: translate3d(0, 0, 0);
  }

  .login_form button{
    margin-left: auto;
    margin-right: auto;
  }
  .result_message{
    display: inline-block; /* Убедитесь, что элемент ведет себя как блочный */
    margin-left: auto;
    color: #e0e0e0;
    text-align: center;
  }
</style>