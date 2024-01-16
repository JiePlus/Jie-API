<script lang="ts" setup>
import {onMounted, ref} from 'vue'
import {Plus} from "@element-plus/icons-vue";
import {ElMessage, UploadProps} from "element-plus";
import {getAvatar} from "../api/user.ts";
import {baseURL} from "../plugins/myAxios.ts";
import {beforeImgUpload} from "../common/imgUtils.ts";

const {itemForm, title} = defineProps({
  itemForm: {
    type: Array,
  },
  title: {
    type: String,
  }
})

const visible = ref(true)

// 从父组件接收方法
const emit = defineEmits(['closeDialog', 'addSubmit'])
// 关闭弹窗
const closeDialog = () => {
  visible.value = false
  emit('closeDialog')
}
// 提交表单
const submit = () => {
  formatToValue()
  // console.log(itemForm)
  emit('addSubmit', itemForm)
  closeDialog()
}

function formatValue() {
  // for循环遍历itemForm，取出其中type=“select”的对象，将其value值按照options中的value对应的label进行替换
  for (let i = 0; i < itemForm.length; i++) {
    if (itemForm[i].type === 'select') {
      for (let j = 0; j < itemForm[i].options.length; j++) {
        if (itemForm[i].options[j].value == itemForm[i].value) {
          itemForm[i].value = itemForm[i].options[j].label
        }
      }
    }
  }
}

function formatToValue() {
  // for循环遍历itemForm，取出其中type=“select”的对象，将其value值按照options中的value对应的label进行替换
  for (let i = 0; i < itemForm.length; i++) {
    if (itemForm[i].type === 'select') {
      for (let j = 0; j < itemForm[i].options.length; j++) {
        if (itemForm[i].options[j].label == itemForm[i].value) {
          itemForm[i].value = itemForm[i].options[j].value
        }
      }
    }
  }
}

// 当接收到父组件传来的itemForm时，将其中的value值进行格式化
onMounted(() => {
  formatValue()
  // 如果传来的itemForm中有type=“upload”的对象，将其value值进行格式化
  for (let i = 0; i < itemForm.length; i++) {
    if (itemForm[i].type === 'upload') {
      getImgUrl(itemForm[i].value)
    }
  }
})
const imageUrl = ref('')

function isValidImageType(type: string): boolean {
  return type.startsWith('image/') && ['image/jpeg', 'image/png'].includes(type);
}

const handleAvatarSuccess: UploadProps['onSuccess'] = (res) => {
  getImgUrl(res.data.ossFilePath);
  itemForm.find(item => item.type === 'upload').value = res.data.ossFilePath
}

const getImgUrl = (ossFilePath) => {
  // 如果是空值，直接返回
  if (ossFilePath === null || ossFilePath === '') {
    return
  }
  // 检测是否是file开头
  if (!ossFilePath.startsWith('file')) {
    imageUrl.value = ossFilePath
    return
  }
  // 把ossFilePath的所有/替换成%2F
  ossFilePath = ossFilePath.replace(/\//g, '%2F')
  getAvatar(ossFilePath).then((res) => {
    imageUrl.value = res.data
  })
}
</script>

<template>
  <el-dialog v-model="visible" :title="title" @close="closeDialog">
    <template #default>
      <el-form>
        <template v-for="item in itemForm">
          <el-form-item :label="item.name + ' :'">
            <el-input v-if="item.type ==='input'" v-model="item.value" placeholder="请输入"></el-input>
            <el-select v-if="item.type ==='select'" v-model="item.value" placeholder="请选择"
                       style="width: 100%">
              <el-option v-for="option in item.options" :key="option.value" :label="option.label"
                         :value="option.value"></el-option>
            </el-select>
            <el-upload
                v-if="item.type ==='upload'"
                v-model="item.value"
                :before-upload="beforeImgUpload"
                :on-success="handleAvatarSuccess"
                :show-file-list="false"
                :with-credentials="true"
                :action="baseURL +'/api/user/uploadAvatar'"
                class="avatar-uploader"
            >
              <img v-if="imageUrl" :src="imageUrl" alt="" class="avatar"/>
              <el-icon v-else class="avatar-uploader-icon">
                <Plus/>
              </el-icon>
            </el-upload>
          </el-form-item>
        </template>
      </el-form>
    </template>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="submit">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>


</template>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>