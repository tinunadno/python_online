
import { defineStore } from 'pinia';

import axios from 'axios';

export const useUserStore = defineStore('user', {
    state: () => ({
        success: true,
        result_message: '',
        currentUser: null,
    }),
    actions: {
        async login(userData) {
            console.log(userData)
            await axios
                .post(
                    'http://localhost:8085/pythonOnline/authentication/authorize',
                    userData,
                    {
                        headers: {
                            'Content-Type': 'application/json',
                        }
                    })
                .then((response) => {
                    sessionStorage.setItem('userId', response.data.userId);
                    sessionStorage.setItem('token', response.data.token);

                })
                .catch((error) => {
                    console.error('Ошибка при входе:', error);
                    this.result_message = "Ошибка входа"
                    this.success = false;
                })
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

        }
    },
});