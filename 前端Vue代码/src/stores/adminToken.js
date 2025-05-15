import {defineStore} from "pinia";

export const useAdminTokenStore = defineStore('adminToken', {
    state: () => ({ token: '' }),
    actions: {
        setToken(token) { this.token = token },
        removeToken() { this.token = '' }
    },
    persist: true // 持久化
});