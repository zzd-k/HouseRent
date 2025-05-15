// 存储用户提交的房源数据
const HOUSE_KEY = 'rent_houses'

export default {
    // 保存房源数据
    saveHouses(data) {
        localStorage.setItem(HOUSE_KEY, JSON.stringify(data))
    },

    // 获取房源数据
    getHouses() {
        return JSON.parse(localStorage.getItem(HOUSE_KEY)) || []
    },

    // 清除数据（测试用）
    clear() {
        localStorage.removeItem(HOUSE_KEY)
    }
}