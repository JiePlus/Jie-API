<script setup lang="ts">

import {Message} from "@element-plus/icons-vue";
</script>

<template>
  <div class="about-me">
    <el-card class="el-card-list">
      <h2>项目信息</h2>
      <p>
        Jie-API平台是一个基于Vue、SpringBoot、Dubbo和Gateway的API接口调用平台。
      </p>
      <p>
        管理员可以接入和发布接口，并可视化显示各接口的调用情况。
      </p>
      <p>
        用户可以开通接口调用权限、浏览接口、在线调试，并能够通过客户端SDK轻松调用接口。
      </p>
    </el-card>

    <el-card class="el-card-list">
      <h2>关于我</h2>
      <p>
        我是一名热爱编程和学习的开发者，目前大四在读。我对Java后端开发有深厚的兴趣和丰富的实践经验。
      </p>
      <p>
        我希望通过这个Jie-API平台，能够减少更多的开发者的开发成本，提高开发效率。如果你对Jie-API平台有任何建议或问题，欢迎随时联系我。
      </p>
    </el-card>

    <el-card class="el-card-list">
      <h2>联系我</h2>
      <p>
        你可以在以下方式找到我：
      </p>
      <ul style="list-style-type: none;">
        <li><el-icon><Message /></el-icon><a href="mailto:haojie_cai@foxmail.com">haojie_cai@foxmail.com</a></li>
        <li><el-icon><Message /></el-icon><a href="https://github.com/JiePlus">GitHub</a></li>
        <li><el-icon><Message /></el-icon><a href="https://gitee.com/JiePlus">Gitee</a></li>
      </ul>
    </el-card>
  </div>
</template>

<style scoped>
.about-me{
  width: 100%;
  min-height: 86vh;
  display: flex;
  justify-content: space-evenly;
}
.el-card-list{
  margin-bottom: 20px;
  width: 30%;
}
.el-card-list p{
  margin: 10px 0;
}
li {
  margin: 10px 0;
}
a {
  text-decoration:none;
}
.el-icon {
  margin-right: 10px;
}
</style>