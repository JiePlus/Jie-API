<script setup lang="ts">
import {listInterfaceInfoByPage} from "../api/interfaceInfo.ts";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import vSideIn from "../assets/js/vSlideIn.js"
import {applyUserInterfaceInfo, checkLeftNum} from "../api/userInterfaceInfo.ts";
import {ElMessage, ElMessageBox} from "element-plus";

const interfaceInfo = ref([])
const isShow = ref(false)
let current = 0
const getInterfaceInfo = () => {
  if (isShow.value) {
    return
  }
  listInterfaceInfoByPage('current=' + current).then(res => {
    if (res.data.records.length === 0) {
      // console.log(111)
      isShow.value = true
      return;
    }
    if (interfaceInfo.value.length === 0) {
      console.log(res.data.records)
      interfaceInfo.value = res.data.records
      // console.log(222)
      console.log(interfaceInfo.value)
      return
    }
    for (let data of res.data.records) {
      interfaceInfo.value.push(data)
    }
  })
}

onMounted(() => {
  // getInterfaceInfo()
})


const load = () => {
  current += 1
  getInterfaceInfo()
}

const router = useRouter()

const goToRun = async (id: number) => {
  // 先检查该用户是否有调用次数
  const res: any = await checkLeftNum({id});
  if (res.code !== 0 && res.code !== 40400) {
    ElMessage.error(res.message)

    return
  }
  if (res.code === 40400 || res.data.leftNum === 0) {
    ElMessageBox.confirm(
        '检测到您未申请调用次数，是否申请？',
        '提示',
        {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning',
        }
    )
        .then(async () => {
          const res: any = await applyUserInterfaceInfo({interfaceInfoId:id})

          if (res.code === 0) {
            ElMessage({
              type: 'success',
              message: '申请成功',
            })
            await router.push(`/interfaceRun/${id}`)
          } else {
            ElMessage({
              type: 'error',
              message: '申请失败',
            })
          }
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '已取消申请',
          })
        })
    return
  }
  await router.push(`/interfaceRun/${id}`)
}
</script>

<template>
  <ul v-infinite-scroll="load" class="infinite-list" infinite-scroll-distance="200" style="overflow: auto">
    <li v-for="item in interfaceInfo">
      <el-card v-side-in class="box-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <h2>{{ item.name }}</h2>
            <el-button class="button" text type="primary" :disabled="item.status === 0" @click="goToRun(item.id)">试运行</el-button>
          </div>
        </template>
        <div class="text item">
          <span>接口介绍：</span>
          <span>{{ item.description }}</span>
        </div>
        <div class="text item">
          <span>接口地址：</span>
          <span>{{ item.host + item.path }}</span>
        </div>
      </el-card>
    </li>
    <li v-show="isShow">
      <div style="background-color: #f2f2f2; text-align: center; padding: 20px 0;">
        <div style="display: flex; justify-content: center; align-items: center; height: 30px; border-top: 1px solid #ccc; margin: 0 auto; width: 80%; position: relative;">
          <div style="position: absolute; top: -15px; background-color: #f2f2f2;">没有更多数据了</div>
        </div>
      </div>
    </li>
  </ul>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  margin-bottom: 18px;
}

.infinite-list {
  height: 750px;
  padding: 0;
  margin: 0;
  list-style: none;
}
</style>