<script lang="ts" setup>

import {getInterfaceInfoById, invokeInterfaceInfoUsingPOST} from "../api/interfaceInfo.ts";
import {useRoute} from "vue-router";
import {onMounted, reactive, ref, toRaw} from "vue";
import {ElMessage} from "element-plus";
import VueJsonPretty from 'vue-json-pretty';
import {checkLeftNum} from "../api/userInterfaceInfo.ts";
import {generateOutput} from "../common/utils.ts";

const route = useRoute()
const data: any = ref({})

let generateParams = ref('')
const getInterfaceInfo = async (id: any) => {
  const res = await getInterfaceInfoById(id)

  if (res.code !== 0) {
    ElMessage.error(res.message)
    return
  }

  data.value = res.data
  generateParams.value = generateOutput(data.value.requestParams)
}

const leftNum = ref(0)

const getLeftNum = async () => {
  const res: any = await checkLeftNum({id: route.params.id})
  if (res.code !== 0 && res.code !== 40400) {
    ElMessage.error(res.message)
    return
  }
  if (res.code === 40400) {
    leftNum.value = 0
    return
  }
  if (res.data.leftNum === 0){
    ElMessage.error('您的调用次数已用完')
    return
  }
  leftNum.value = res.data.leftNum
}


onMounted(() => {
  getInterfaceInfo(route.params.id)
  getLeftNum()

})

const jsonFormatter = (str: string) => {
  try {
    return eval("(" + str + ")")
  } catch (e) {
    return str
  }
}

const dateFormater = (date: string) => {
  if (!date) {
    return ''
  }
  return date.substring(0, 19).replace('T', ' ')
}

const responseResult = ref(null)
const invokeInterface = async () => {
  console.log(generateParams.value)
  const params = {
    id: route.params.id,
    userRequestParams: generateParams.value
  }
  const res: any = await invokeInterfaceInfoUsingPOST(params)
  if (res.code !== 0) {
    ElMessage.error(res.message)
    return
  }

  responseResult.value = res.data
  await getLeftNum()
}
</script>

<template>
  <div style="display: flex;justify-content: space-evenly">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <h3>{{ data.name }}</h3>
        </div>
      </template>
      <div class="text item">
        <span class="label">状态：</span>
        <span>{{ data.status === 1 ? "正常" : "关闭" }}</span>
      </div>
      <div class="text item">
        <span class="label">描述：</span>
        <span>{{ data.introduction }}</span>
      </div>
      <div class="text item">
        <span class="label">Host：</span>
        <span>{{ data.host }}</span>
      </div>
      <div class="text item">
        <span class="label">Path：</span>
        <span>{{ data.path }}</span>
      </div>
      <div class="text item">
        <span class="label">请求方式：</span>
        <span>{{ data.method }}</span>
      </div>
      <div class="text item">
        <span class="label">请求头：</span>
        <vue-json-pretty :data="jsonFormatter(data.requestHeader)"/>
      </div>
      <div class="text item">
        <span class="label">响应头：</span>
        <vue-json-pretty :data="jsonFormatter(data.responseHeader)"/>
      </div>
      <div class="text item">
        <span class="label">请求参数：</span>
        <vue-json-pretty :data="jsonFormatter(data.requestParams)"/>
      </div>
      <div class="text item">
        <span class="label">创建时间：</span>
        <span>{{ dateFormater(data.createTime) }}</span>
      </div>
      <div class="text item">
        <span class="label">更新时间：</span>
        <span>{{ dateFormater(data.updateTime) }}</span>
      </div>
    </el-card>
    <div style="display: flex;flex-direction: column;justify-content: space-between;">
      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <h3>在线测试</h3>
            <div>
              <span>剩余调用次数：<span style="color: red">{{ leftNum }}</span></span>
            </div>
          </div>
        </template>
        <div>
          <span>请求参数：</span>
          <el-input
              v-model="generateParams"
              v-if="generateParams"
              :autosize="{ minRows: 3, maxRows: null}"
              placeholder="Please input"
              type="textarea"
          />
        </div>
        <div class="dialog-footer" style="margin-top: 20px">
          <el-button type="primary" @click="invokeInterface">
            调用
          </el-button>
        </div>
      </el-card>
      <el-card class="box-card" style="flex: 1;margin-top: 20px">
        <template #header>
          <div class="card-header">
            <h3>返回结果</h3>
          </div>
        </template>
        <div>
          <vue-json-pretty :data="responseResult ? jsonFormatter(responseResult) : '暂无数据' "/>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
  display: flex;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
}
</style>