<template>
  <div>
    <button @click="auditHouse(1, 'approved')">审核房源</button>
  </div>
</template>

<script>
import { updateHouseRentAuditStatus } from '@/api/rent.js'

export default {
  methods: {
    async auditHouse(id, auditStatus) {
      try {
        const response = await updateHouseRentAuditStatus(id, auditStatus)
        console.log('审核成功', response)
      } catch (error) {
        console.error('审核失败', error)
      }
    },
  },
}
</script>
