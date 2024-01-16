<script lang="ts" setup>

const {searchKeyList} = defineProps({
  searchKeyList: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['search'])
const searchBtn = () => {
  emit('search', searchKeyList)
}

const resetForm = () => {
  console.log(searchKeyList)
  for (let i = 0; i < searchKeyList.length; i++) {
    searchKeyList[i].value = ''
  }
  searchBtn()
}

</script>

<template>
  <el-card>
    <el-form>
      <el-row :gutter="20">
        <el-col v-for="searchKey in searchKeyList" :span="6">
          <el-form-item :label="searchKey.name + ':'">
            <el-input v-if="searchKey.type ==='input'" v-model="searchKey.value" placeholder="请输入"/>
            <el-select v-if="searchKey.type ==='select'" v-model="searchKey.value" placeholder="请选择"
                       style="width: 100%">
              <el-option v-for="item in searchKey.options" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20"/>
        <el-col :span="2">
          <el-form-item style="float: right">
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-form-item style="float: right">
            <el-button type="primary" @click="searchBtn">查询</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

  </el-card>
</template>

<style scoped>

</style>