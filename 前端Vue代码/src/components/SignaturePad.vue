<template>
  <div class="signature-pad">
    <canvas ref="canvas" @mousedown="startDrawing" @mousemove="draw"
            @mouseup="stopDrawing" @mouseleave="stopDrawing"
            @touchstart="startDrawing" @touchmove="draw" @touchend="stopDrawing"></canvas>
    <div class="controls">
      <el-button size="small" @click="clear">清除</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const canvas = ref(null)
const isDrawing = ref(false)
const lastX = ref(0)
const lastY = ref(0)

const ctx = ref(null)

onMounted(() => {
  const canvasEl = canvas.value
  canvasEl.width = canvasEl.offsetWidth
  canvasEl.height = canvasEl.offsetHeight

  ctx.value = canvasEl.getContext('2d')
  ctx.value.lineWidth = 2
  ctx.value.lineCap = 'round'
  ctx.value.strokeStyle = '#000'
})

const startDrawing = (e) => {
  isDrawing.value = true
  const pos = getPosition(e)
  lastX.value = pos.x
  lastY.value = pos.y
}

const draw = (e) => {
  if (!isDrawing.value) return

  const pos = getPosition(e)
  ctx.value.beginPath()
  ctx.value.moveTo(lastX.value, lastY.value)
  ctx.value.lineTo(pos.x, pos.y)
  ctx.value.stroke()

  lastX.value = pos.x
  lastY.value = pos.y
}

const stopDrawing = () => {
  isDrawing.value = false
}

const clear = () => {
  ctx.value.clearRect(0, 0, canvas.value.width, canvas.value.height)
}

const getPosition = (e) => {
  const canvasEl = canvas.value
  let x, y

  if (e.type.includes('touch')) {
    const touch = e.touches[0] || e.changedTouches[0]
    const rect = canvasEl.getBoundingClientRect()
    x = touch.clientX - rect.left
    y = touch.clientY - rect.top
  } else {
    x = e.offsetX
    y = e.offsetY
  }

  return { x, y }
}

const getSignature = () => {
  return canvas.value.toDataURL('image/png')
}

defineExpose({
  clear,
  getSignature
})
</script>

<style scoped>
.signature-pad {
  border: 1px solid #ddd;
  border-radius: 4px;
  position: relative;
  height: 200px;
}

.signature-pad canvas {
  width: 100%;
  height: 100%;
  background-color: #f9f9f9;
}

.controls {
  margin-top: 10px;
  text-align: right;
}
</style>