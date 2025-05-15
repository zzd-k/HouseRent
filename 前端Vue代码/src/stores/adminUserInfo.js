import {defineStore} from "pinia";

export const useAdminUserInfoStore = defineStore('adminUserInfo', {
    state: () => ({ info: {} }),
    actions: {
        setInfo(info) { this.info = info },
        removeInfo() { this.info = {} }
    }
});