<script lang="ts" setup>

import {deleteInterfaceInfo} from "../../api/interfaceInfo.ts";
import {onMounted, ref, watch} from "vue";
import 'vue-json-pretty/lib/styles.css'
import {ElMessage, ElMessageBox} from "element-plus";
import DialogContent from "../../components/DialogContent.vue";
import SearchContent from "../../components/SearchContent.vue";
import {deepCopy} from "../../common/utils.ts";
import {addUser, getUserById, listUserByPage, updateUser} from "../../api/user.ts";
import getImgUrl from "../../assets/js/utils.ts";

const tableData = ref([])

const jsonFormatter = (str: string) => {
  try {
    return eval("(" + str + ")")
  } catch (e) {
    return str
  }
}

// 性别格式化
const formatStatus = (row) => {
  if (row.gender === 0) {
    return '男'
  } else if (row.gender === 1) {
    return '女'
  }
}

// 用户类型格式化
const formatRole = (row) => {
  if (row.userRole === 'admin') {
    return '管理员'
  } else if (row.userRole === 'user') {
    return '普通用户'
  }
}

const baseData = [
  {
    name: "用户账号",
    key: "userAccount",
    value: null,
    type: 'input',
  },
  {
    name: "用户昵称",
    key: "userName",
    value: null,
    type: 'input',
  },
  {
    name: "用户头像",
    key: "userAvatar",
    value: null,
    type: 'upload',
  },
  {
    name: "性别",
    key: "gender",
    value: null,
    type: 'select',
    options: [
      {
        label: '男',
        value: '0'
      },
      {
        label: '女',
        value: '1'
      }
    ]
  },
  {
    name: "用户密码",
    key: "userPassword",
    value: null,
    type: 'input',
  },
  {
    name: "用户类型",
    key: "userRole",
    value: null,
    type: 'select',
    options: [
      {
        label: '管理员',
        value: 'admin'
      },
      {
        label: '普通用户',
        value: 'user'
      }
    ]
  }
]

const searchKeyList = ref(deepCopy(baseData, ['userAvatar', 'userPassword']))

const addForm = ref(deepCopy(baseData, ["userRole"]))

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
  const res = await listUserByPage(params)
  // 将传过来的数据中的用户头像进行格式化
  for (let i = 0; i < res.data.records.length; i++) {
    res.data.records[i].userAvatar = await getImgUrl(res.data.records[i].userAvatar)
  }
  tableData.value = res.data
}

onMounted(async () => {
  const res = await listUserByPage(null)
  // 将传过来的数据中的用户头像进行格式化
  for (let i = 0; i < res.data.records.length; i++) {
    res.data.records[i].userAvatar = await getImgUrl(res.data.records[i].userAvatar)
  }
  tableData.value = res.data
})

const addVisible = ref(false)
const addInfo = () => {
  addVisible.value = true;
}

const editVisible = ref(false)

let editForm = ref(deepCopy(baseData, ['userRole']))

const editId = ref(0)
const editInfo = async (id: number) => {
  const res = await getUserById(id)
  res.data.userPassword = "******"
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
  const res = await updateUser(params)
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
  const res = await addUser(params)
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
  const res = await listUserByPage(params)
  console.log(res)
  // 将传过来的数据中的用户头像进行格式化
  for (let i = 0; i < res.data.records.length; i++) {
    res.data.records[i].userAvatar = await getImgUrl(res.data.records[i].userAvatar)
  }
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
          <el-button type="danger">删除</el-button>
        </div>
      </div>
    </template>
    <el-table :data="tableData.records" border stripe style="width: 100%" table-layout="fixed">
      <el-table-column type="selection" width="40"/>
      <el-table-column align="center" label="用户账号" prop="userAccount" width="300"/>
      <el-table-column align="center" label="用户昵称" prop="userName" width="300"/>
      <el-table-column align="center" label="用户头像" prop="userAvatar" width="300">
        <template #default="scope">
          <el-image :src="scope.row.userAvatar" fit="cover" lazy style="width: 100px; height: 100px"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户性别" prop="gender" width="300">
        <template #default="scope">
          {{ formatStatus(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户类型" prop="userRole" width="265">
        <template #default="scope">
          <el-tag v-if="scope.row.userRole === 'admin'" type="danger">{{ formatRole(scope.row) }}</el-tag>
          <el-tag v-if="scope.row.userRole === 'user'">{{ formatRole(scope.row) }}</el-tag>
        </template>
      </el-table-column>


      <el-table-column align="center" fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button link size="small" type="primary" @click="editInfo(scope.row.id)">修改</el-button>
          <el-button link size="small" type="danger" @click="delSubmit(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pageBox">
      <el-pagination v-model:current-page="currentPage" :total="parseInt(tableData.total)" background
                     layout="prev, pager, next"/>
    </div>
  </el-card>

  <DialogContent v-if="addVisible" :itemForm="addForm" :title="'新增用户信息'" @addSubmit="addSubmit"
                 @closeDialog="addVisible = false"/>

  <DialogContent v-if="editVisible" :itemForm="editForm" :title="'修改用户信息'" @addSubmit="editSubmit"
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