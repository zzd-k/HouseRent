<template>
  <div class="signature-pad-container">
    <canvas
        ref="canvas"
        @mousedown="startDrawing"
        @mousemove="draw"
        @mouseup="stopDrawing"
        @mouseleave="stopDrawing"
    ></canvas>
    <div class="actions">
      <el-button @click="clear">清除</el-button>
      <el-button type="primary" @click="save">保存签名</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const canvas = ref(null)
const isDrawing = ref(false)
const emit = defineEmits(['save'])

let ctx = null

onMounted(() => {
  ctx = canvas.value.getContext('2d')
  resizeCanvas()
})

const resizeCanvas = () => {
  canvas.value.width = canvas.value.offsetWidth
  canvas.value.height = 200
}

const startDrawing = (e) => {
  isDrawing.value = true
  draw(e)
}

const draw = (e) => {
  if (!isDrawing.value) return

  ctx.lineWidth = 2
  ctx.lineCap = 'round'
  ctx.strokeStyle = '#000'

  ctx.lineTo(
      e.clientX - canvas.value.offsetLeft,
      e.clientY - canvas.value.offsetTop
  )
  ctx.stroke()
  ctx.beginPath()
  ctx.moveTo(
      e.clientX - canvas.value.offsetLeft,
      e.clientY - canvas.value.offsetTop
  )
}

const stopDrawing = () => {
  isDrawing.value = false
  ctx.beginPath()
}

const clear = () => {
  ctx.clearRect(0, 0, canvas.value.width, canvas.value.height)
}

const save = () => {
  const dataUrl = canvas.value.toDataURL('image/png')
  emit('save', dataUrl)
}
</script>