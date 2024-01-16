<script lang="ts" setup>

import {
  addInterfaceInfo,
  deleteInterfaceInfo,
  getInterfaceInfoById,
  listInterfaceInfoByPage, offlineInterfaceInfo,
  onlineInterfaceInfo,
  updateInterfaceInfo
} from "../../api/interfaceInfo.ts";
import {onMounted, ref, watch} from "vue";
import VueJsonPretty from 'vue-json-pretty'
import 'vue-json-pretty/lib/styles.css'
import {ElMessage, ElMessageBox} from "element-plus";
import DialogContent from "../../components/DialogContent.vue";
import SearchContent from "../../components/SearchContent.vue";
import {deepCopy} from "../../common/utils.ts";

const tableData = ref([])

const jsonFormatter = (str: string) => {
  try {
    return eval("(" + str + ")")
  } catch (e) {
    return str
  }
}

// 接口状态格式化
const formatStatus = (row) => {
  if (row.status === 0) {
    return '关闭'
  } else if (row.status === 1) {
    return '开启'
  }
}

const baseData = [
  {
    name: "接口名称",
    key: "name",
    value: null,
    type: 'input',
  },
  {
    name: "描述",
    key: "description",
    value: null,
    type: 'input',
  },
  {
    name: "请求方法",
    key: "method",
    value: null,
    type: 'select',
    options: [
      {
        label: 'GET',
        value: 'GET'
      },
      {
        label: 'POST',
        value: 'POST'
      },
      {
        label: 'PUT',
        value: 'PUT'
      },
      {
        label: 'DELETE',
        value: 'DELETE'
      },
      {
        label: 'HEAD',
        value: 'HEAD'
      },
      {
        label: 'OPTIONS',
        value: 'OPTIONS'
      },
      {
        label: 'PATCH',
        value: 'PATCH'
      },
      {
        label: 'TRACE',
        value: 'TRACE'
      },
      {
        label: 'CONNECT',
        value: 'CONNECT'
      }
    ]
  },
  {
    name: "Host",
    key: "host",
    value: null,
    type: 'input',
  },
  {
    name: "Path",
    key: "path",
    value: null,
    type: 'input',
  },
  {
    name: "请求参数",
    key: "requestParams",
    value: null,
    type: 'input',
  },
  {
    name: "请求头",
    key: "requestHeader",
    value: null,
    type: 'input',
  },
  {
    name: "响应头",
    key: "responseHeader",
    value: null,
    type: 'input',
  },
  {
    name: "状态",
    key: "status",
    value: null,
    type: 'select',
    options: [
      {
        label: '开启',
        value: 1
      },
      {
        label: '关闭',
        value: 0
      }
    ]
  },
]

const searchKeyList = ref(deepCopy(baseData, ['requestParams', 'requestHeader', 'responseHeader']))

const addForm = ref(deepCopy(baseData, ['status']))

const resetForm = () => {
  for (let i = 0; i < searchKeyList.value.length; i++) {
    searchKeyList.value[i].value = null
  }
  search()
}

const search = async () => {
  let params = ''
  searchKeyList.value.forEach((item) => {
    if (item.value !== null && item.value !== '') {
      params += item.key + '=' + item.value + '&'
    }
  })
  params += 'current=' + currentPage.value
  // console.log(params)
  const res = await listInterfaceInfoByPage(params)
  tableData.value = res.data
}

onMounted(async () => {
  const res = await listInterfaceInfoByPage(null)
  tableData.value = res.data
})

const addVisible = ref(false)
const addInfo = () => {
  addVisible.value = true;
}

const editVisible = ref(false)

let editForm = ref(deepCopy(baseData, ["status"]))

const editId = ref(0)
const editInfo = async (id: number) => {
  const res = await getInterfaceInfoById(id)
  if (res.code !== 0) {
    ElMessage.error(res.message)
    return
  }
  editId.value = id
  // 循环遍历res.data，将res.data中的key和value赋值给editForm
  for (let i = 0; i < editForm.value.length; i++) {
    editForm.value[i].value = res.data[editForm.value[i].key]
  }
  editVisible.value = true;
}

const editSubmit = async () => {
  if (editId.value === null) {
    ElMessage.error('请选择要修改的接口')
    return
  }
  let params = {
    id: editId.value
  }
  for (let i = 0; i < editForm.value.length; i++) {
    if (editForm.value[i].value === null) {
      ElMessage.error('请填写所有信息')
      return
    }
    params[editForm.value[i].key] = editForm.value[i].value
  }
  // console.log(params)
  const res = await updateInterfaceInfo(params)
  if (res.code === 0) {
    ElMessage.success("修改成功")
    editVisible.value = false
    resetForm()
    await search()
  } else {
    ElMessage.error("修改失败")
  }
}


const addSubmit = async () => {
  let params: any = {}
  for (let i = 0; i < addForm.value.length; i++) {
    if (addForm.value[i].value === null) {
      ElMessage.error('请填写所有信息')
      return
    }
    params[addForm.value[i].key] = addForm.value[i].value
  }
  params["status"] = 0
  // console.log(params)
  const res = await addInterfaceInfo(params)
  if (res.code === 0) {
    ElMessage.success("新增成功")
    addVisible.value = false
    resetForm()
    await search()
  } else {
    ElMessage.error(res.message)
  }
}

const delSubmit = async (id: number) => {
  const params = {
    id: id
  }
  ElMessageBox.confirm('确定要删除该接口吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await deleteInterfaceInfo(params)
    if (res.code === 0) {
      ElMessage.success("删除成功")
      await search()
    } else {
      ElMessage.error("操作失败")
    }
  })
}

const onlineInterfaceInfoEvent = (id: number) => {
  ElMessageBox.confirm('确定要上线该接口吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const params = {
      id: id
    }
    const res = await onlineInterfaceInfo(params)
    if (res.code === 0) {
      ElMessage.success("上线成功")
      await search()
    } else {
      ElMessage.error("操作失败")
    }
  })
}

const offlineInterfaceInfoEvent = (id: number) => {
  ElMessageBox.confirm('确定要下线该接口吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const params = {
      id: id
    }
    const res = await offlineInterfaceInfo(params)
    if (res.code === 0) {
      ElMessage.success("下线成功")
      await search()
    } else {
      ElMessage.error("操作失败")
    }
  })
}

const currentPage = ref(1)

// 监听currentPage变化，重新请求数据
watch(currentPage, async (newVal) => {
  let params = ''
  searchKeyList.value.forEach((item) => {
    if (item.value !== null && item.value !== '') {
      params += item.key + '=' + item.value + '&'
    }
  })
  params += 'current=' + newVal
  const res = await listInterfaceInfoByPage(params)
  tableData.value = res.data
})
</script>

<template>
  <search-content :searchKeyList="searchKeyList" @resetForm="resetForm" @search="search"/>

  <br/>

  <el-card>
    <template #header>
      <div class="card-header">
        <span>查询表格</span>
        <div>
          <el-button type="primary" @click="addInfo">新增</el-button>
          <el-button type="success">发布</el-button>
          <el-button type="warning">下线</el-button>
          <el-button type="danger">删除</el-button>
        </div>
      </div>
    </template>
    <el-table :data="tableData.records" border stripe style="width: 100%" table-layout="fixed">
      <el-table-column type="selection" width="40"/>
      <el-table-column fixed label="接口名称" prop="name" width="150"/>
      <el-table-column label="描述" prop="description" show-overflow-tooltip width="120"/>
      <el-table-column label="请求方法" prop="method" show-overflow-tooltip width="120"/>
      <el-table-column label="Host" prop="host" show-overflow-tooltip width="200"/>
      <el-table-column label="Path" prop="path" show-overflow-tooltip width="200"/>
      <el-table-column label="请求参数" show-overflow-tooltip width="200">
        <template #default="scope">
          <vue-json-pretty :data="jsonFormatter(scope.row.requestParams)"/>
        </template>
      </el-table-column>
      <el-table-column label="请求头" show-overflow-tooltip width="310">
        <template #default="scope">
          <vue-json-pretty :data="jsonFormatter(scope.row.requestHeader)"/>
        </template>
      </el-table-column>
      <el-table-column label="响应头" show-overflow-tooltip width="310">
        <template #default="scope">
          <vue-json-pretty :data="jsonFormatter(scope.row.responseHeader)"/>
        </template>
      </el-table-column>
      <el-table-column :formatter="formatStatus" label="状态" prop="status" width="80">
        <template v-slot="{row}">
          <el-tag :type="row.status === 0? 'danger' :'success'" effect="plain">{{
              row.status === 0 ? '关闭' : '开启'
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button link size="small" type="primary" @click="editInfo(scope.row.id)">修改</el-button>
          <el-button v-if="scope.row.status === 0" link size="small" type="success"
                     @click="onlineInterfaceInfoEvent(scope.row.id)">发布
          </el-button>
          <el-button v-if="scope.row.status === 1" link size="small" type="warning"
                     @click="offlineInterfaceInfoEvent(scope.row.id)">下线
          </el-button>
          <el-button link size="small" type="danger" @click="delSubmit(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pageBox">
      <el-pagination v-model:current-page="currentPage" :total="parseInt(tableData.total)" background
                     layout="prev, pager, next"/>
    </div>
  </el-card>

  <DialogContent v-if="addVisible" :itemForm="addForm" :title="'新增接口信息'" @addSubmit="addSubmit"
                 @closeDialog="addVisible = false"/>

  <DialogContent v-if="editVisible" :itemForm="editForm" :title="'修改接口信息'" @addSubmit="editSubmit"
                 @closeDialog="editVisible = false"/>

</template>

<style scoped>
.card-header {
  width: 100%;
  display: inline-flex;
  justify-content: space-between;
}

.pageBox {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>