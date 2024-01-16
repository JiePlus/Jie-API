<script setup lang="ts">

import {onMounted, ref, watch} from 'vue'
import {useRoute, useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import {getLoginUser, userLogout} from "../api/user.ts";
import getImgUrl from "../assets/js/utils.ts";
import store from "../store";

const route = useRoute()
const router = useRouter()
const activeIndex = ref(route.path)


// 定义菜单数据
const  menuList =ref([
    {
      index: "/index",
      title: '系统首页'
    },
  {
    index: "/about",
    title: '关于我们',
  }
])

const adminMenu = {
  index: "/admin",
  title: '管理面板',
  children: [
    {
      index: "/admin/user",
      title: '用户管理'
    },
    {
      index: "/admin/interfaceInfo",
      title: '接口管理'
    },
    {
      index: "/admin/interfaceAnalysis",
      title: '接口分析'
    }
  ]
}

const getUserInfo = async () => {
  const res: any = await getLoginUser();

  if (res.code !== 0){
    ElMessage.error(res.message)
    return
  }

  store.commit('setUser', res.data)
}

const userImg = ref('')

const userAvatar = ref(store.state.user.userAvatar)
const userRole = ref(store.state.user.userRole)
onMounted(async () => {
  if (!store.state.user.id){
    await getUserInfo();
  }

  if (!userRole) {
    router.push('/')
    ElMessage.error("请先登录")
    return
  }

  if (userAvatar.value) {
    getImgUrl(userAvatar.value).then(res => {
      userImg.value = res
    })
  }

})

const logout = async () => {
  const res = await userLogout(null);
  if (res.code !== 0) {
    ElMessage.error(res.message)
    return
  }
  sessionStorage.removeItem('userRole')
  sessionStorage.removeItem('userName')
  sessionStorage.removeItem('userAvatar')
  await router.push('/')
}

watch(() => store.state.user.userAvatar, (newV) => {
  userAvatar.value = newV

  if (userAvatar) {
    getImgUrl(userAvatar.value).then(res => {
      userImg.value = res
    })
  }
}, {deep: true, immediate: true})

watch(() => store.state.user.userRole, (newV) => {
  userRole.value = newV
  if (userRole.value === "admin") {
    menuList.value.splice(1, 0, adminMenu)
  }
}, {deep: true, immediate: true})

watch(() => route.path, (newV) => {
  activeIndex.value = newV
}, {deep: true, immediate: true})
</script>

<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      background-color="#006fc9"
      mode="horizontal"
      text-color="#fff"
      active-text-color="#FFC900"
      router
  >
    <el-menu-item @click="router.push('/index')" style="overflow: hidden">
      <img src="../assets/img/logo.png" alt="" style="height: 35px;margin-right: 10px">
    </el-menu-item>
    <template v-for="menu in menuList" key="index">
      <el-menu-item v-if="!menu.children" :index="menu.index">{{ menu.title }}</el-menu-item>
      <el-sub-menu v-if="menu.children" index="2">
        <template #title>
          {{ menu.title }}
        </template>
        <el-menu-item v-for="item in menu.children" :index="item.index">
          {{ item.title }}
        </el-menu-item>
      </el-sub-menu>
    </template>

    <el-sub-menu index="9" style="position: absolute;right: 0;">
      <template #title>
        <el-avatar :src="userImg" size="small" style="margin-right: 10px" :fit="'cover'"></el-avatar>
        <span>
          {{ store.state.user.userName }}
        </span>
      </template>
      <el-menu-item @click="router.push('/userinfo')">个人中心</el-menu-item>
      <el-menu-item @click="logout">退出登录</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<style scoped>
</style>