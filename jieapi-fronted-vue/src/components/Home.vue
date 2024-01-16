<script setup lang="ts">

import TheHeader from "./TheHeader.vue";
import TheFooter from "./TheFooter.vue";
import {onMounted, ref} from "vue";

const header = ref()
const footer = ref()
const main = ref()

onMounted(() => {
  const headerHeight = header.value.$el.offsetHeight
  const footerHeight = footer.value.$el.offsetHeight
  const mainHeight = window.innerHeight - headerHeight - footerHeight
  main.value.$el.style.height = mainHeight + 'px'
})
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header ref="header">
        <TheHeader/>
      </el-header>
      <el-main style="background-color: #f2f2f2;" ref="main">
        <router-view/>
      </el-main>
      <el-footer ref="footer">
        <TheFooter/>
      </el-footer>
    </el-container>
  </div>
</template>

<style scoped>
.common-layout{
  width: 100%;
}
.el-header{
  padding: 0;
}

.el-footer {
  padding: 0;
  height: auto !important;
}
</style>