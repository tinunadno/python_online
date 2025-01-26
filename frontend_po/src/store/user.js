
import { defineStore } from 'pinia';

// import axios from 'axios';

export const useUserStore = defineStore('user', {
    state: () => ({
        success: true,
        result_message: '',
        currentUser: null,
    }),
    actions: {
        async login(userData) {
            this.result_message = "успешный вход";

            this.currentUser = {name: userData.name}
            this.token = "qwwwweeeerrrtty"

            sessionStorage.setItem('currentUser', JSON.stringify({name: userData.name}));
            sessionStorage.setItem('token', "qwwwweeeerrrtty");

            // await axios
            //     .post(
            //         'http://localhost:8080/api/auth/login',
            //         userData,
            //         {
            //             headers: {
            //                 'Content-Type': 'application/json',
            //             }
            //         })
            //     .then((response) => {
            //         this.result_message = response.data.message;
            //         if (response.data.success) {
            //             this.currentUser = response.data.user
            //             this.token = response.data.token
            //         }
            //         this.success = response.data.success;
            //         sessionStorage.setItem('currentUser', JSON.stringify(response.data.user));
            //         sessionStorage.setItem('token', response.data.token);
            //
            //     })
            //     .catch((error) => {
            //         console.error('Ошибка при входе:', error);
            //         this.result_message = "Ошибка входа"
            //         this.success = false;
            //     })
        },

        logout() {
            this.currentUser = null;
            this.result_message = "";
            sessionStorage.removeItem('currentUser');
            sessionStorage.removeItem('token');
        },

        loadFromSession() {
            const user = sessionStorage.getItem('currentUser');
            if (user) {
                this.currentUser = JSON.parse(user);
            }
        },

        registration(userData){
            // axios
            //     .post(
            //         'http://localhost:8080/api/auth/reg',
            //         userData,
            //         {
            //             headers: {
            //                 'Content-Type': 'application/json',
            //                 Accept: 'application/json'
            //             }
            //         })
            //     .then((response) => {
            //         this.result_message = response.data.message;
            //         if (response.data.success) {
            //             this.currentUser = response.data.user
            //             this.token = response.data.token
            //             sessionStorage.setItem('currentUser', JSON.stringify(response.data.user));
            //             sessionStorage.setItem('token', response.data.token);
            //         }
            //         this.success = response.data.success;
            //     })
            //     .catch((error) => {
            //         console.error('Ошибка при регистрации:', error);
            //         this.result_message = "Ошибка при регистрации"
            //         this.success = false;
            //     })
        }
    },
});