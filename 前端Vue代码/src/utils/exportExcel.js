import { utils, writeFileXLSX } from 'xlsx'

export function exportJsonToExcel({ header, data, filename }) {
    // 1. 创建工作簿
    const wb = utils.book_new()

    // 2. 添加表头和数据
    const wsData = [header, ...data]

    // 3. 创建工作表
    const ws = utils.aoa_to_sheet(wsData)

    // 4. 设置列宽
    ws['!cols'] = header.map((_, i) => ({
        wch: Math.max(
            ...data.map(row => String(row[i] || '').length),
            header[i].length
        ) + 2 // 加2个字符的边距
    }))

    // 5. 添加到工作簿并导出
    utils.book_append_sheet(wb, ws, '租金报表')
    writeFileXLSX(wb, `${filename}.xlsx`)
}