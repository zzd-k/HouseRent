// /​**​
// * 日期格式化工具
// * @param {Date|string} date - 日期对象或日期字符串
// * @param {string} format - 格式字符串，默认 'YYYY-MM-DD HH:mm:ss'
// * @returns {string} 格式化后的日期字符串
// */
export function formatDate(date, format = 'YYYY-MM-DD HH:mm:ss') {
    if (!date) return ''

    let d = new Date(date)
    if (isNaN(d.getTime())) return ''

    const padZero = (num) => (num < 10 ? `0${num}` : num)

    const replacements = {
        YYYY: d.getFullYear(),
        MM: padZero(d.getMonth() + 1),
        DD: padZero(d.getDate()),
        HH: padZero(d.getHours()),
        mm: padZero(d.getMinutes()),
        ss: padZero(d.getSeconds()),
    }

    return format.replace(/YYYY|MM|DD|HH|mm|ss/g, (match) => replacements[match])
}

// /​**​
// * 获取相对时间描述
// * @param {Date|string} date - 日期对象或日期字符串
// * @returns {string} 相对时间描述
// */
export function getRelativeTime(date) {
    if (!date) return ''

    const d = new Date(date)
    if (isNaN(d.getTime())) return ''

    const now = new Date()
    const diffInSeconds = Math.floor((now - d) / 1000)

    if (diffInSeconds < 60) return '刚刚'
    if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}分钟前`
    if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}小时前`

    const diffInDays = Math.floor(diffInSeconds / 86400)
    if (diffInDays < 7) return `${diffInDays}天前`

    return formatDate(d, 'YYYY-MM-DD')
}

// /​**​
// * 计算两个日期之间的天数差
// * @param {Date|string} date1 - 第一个日期
// * @param {Date|string} date2 - 第二个日期
// * @returns {number} 天数差
// */
export function getDaysBetween(date1, date2) {
    const d1 = new Date(date1)
    const d2 = new Date(date2)

    if (isNaN(d1.getTime()) || isNaN(d2.getTime())) return 0

    const diffInMs = Math.abs(d2 - d1)
    return Math.floor(diffInMs / (1000 * 60 * 60 * 24))
}
export function formatTime(date, format = 'YYYY-MM-DD HH:mm:ss') {
    if (!date) return '--'

    let dateObj
    if (date instanceof Date) {
        dateObj = date
    } else if (typeof date === 'number') {
        // 处理时间戳（假设是毫秒级）
        dateObj = new Date(date)
    } else if (typeof date === 'string') {
        // 处理日期字符串
        dateObj = new Date(date.replace(/-/g, '/')) // 解决iOS兼容性问题
    } else {
        return '--'
    }

    // 检查日期是否有效
    if (isNaN(dateObj.getTime())) {
        return '--'
    }

    const padZero = (num) => String(num).padStart(2, '0')

    const map = {
        YYYY: dateObj.getFullYear(),
        MM: padZero(dateObj.getMonth() + 1),
        DD: padZero(dateObj.getDate()),
        HH: padZero(dateObj.getHours()),
        mm: padZero(dateObj.getMinutes()),
        ss: padZero(dateObj.getSeconds()),
        SSS: padZero(dateObj.getMilliseconds()).padStart(3, '0'),
    }

    return format.replace(
        /YYYY|MM|DD|HH|mm|ss|SSS/g,
        (matched) => map[matched]
    )
}


export function relativeTime(date) {
    const now = new Date()
    const target = new Date(date)
    const diff = now - target

    const minute = 60 * 1000
    const hour = 60 * minute
    const day = 24 * hour
    const month = 30 * day
    const year = 365 * day

    if (diff < minute) {
        return '刚刚'
    } else if (diff < hour) {
        return `${Math.floor(diff / minute)}分钟前`
    } else if (diff < day) {
        return `${Math.floor(diff / hour)}小时前`
    } else if (diff < month) {
        return `${Math.floor(diff / day)}天前`
    } else if (diff < year) {
        return `${Math.floor(diff / month)}个月前`
    } else {
        return `${Math.floor(diff / year)}年前`
    }
}


export function getWeekday(date, prefix = false) {
    const dateObj = new Date(date)
    const weekdays = ['日', '一', '二', '三', '四', '五', '六']
    return prefix ? `星期${weekdays[dateObj.getDay()]}` : weekdays[dateObj.getDay()]
}
export default {
    formatTime,
    relativeTime,
    getWeekday,
    formatDate,
    getRelativeTime,
    getDaysBetween
}