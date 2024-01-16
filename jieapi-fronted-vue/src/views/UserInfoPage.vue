<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {forgetPassword, getLoginUser, restUserKeyApi, updateUser} from "../api/user.ts";
import {ElMessage, ElMessageBox, UploadProps} from "element-plus";
import getImgUrl from "../assets/js/utils.ts";
import store from "../store";
import myAxios, {baseURL} from "../plugins/myAxios.ts";
import {beforeImgUpload} from "../common/imgUtils.ts";

const user: any = ref(store.state.user)

const userImg = ref(store.state.user.userAvatar)
const getUserInfo = async () => {
  const res: any = await getLoginUser();

  if (res.code !== 0){
    ElMessage.error(res.message)
    return
  }

  store.commit('setUser', res.data)

  const userAvatar = store.state.user.userAvatar
  if (userAvatar) {
    getImgUrl(userAvatar).then(res => {
      userImg.value = res
    })
  }
}
const formatDate = (v: string) => {
  const date = new Date(v)
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return `${pad(year, 4)}-${pad(month)}-${pad(day)} ${pad(hour)}:${pad(minute)}:${pad(second)}`
}
const pad = (timeEl: any, total = 2, str = '0') => {
  return timeEl.toString().padStart(total, str)
}
const resetKey = () => {
  ElMessageBox.confirm('此操作将重置您的密钥，是否继续？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res: any = await restUserKeyApi(null);
    if (res.code !== 0) {
      ElMessage.error(res.message)
      return
    }
    // 给用户提示新的密钥
    ElMessageBox({
      title: '重置成功',
      message: `accessKey为：${res.data.accessKey}，<br/>secretKey为：${res.data.secretKey}，<br/>请妥善保管`,
      showCancelButton: false,
      dangerouslyUseHTMLString: true,
      confirmButtonText: '我已知晓',
    }).then(() => {
      getUserInfo()
    })
    getUserInfo()
  }).catch(() => {
    ElMessage.info('已取消重置')
  });
}
onMounted(() => {
  getUserInfo()
})

const dialogVisible = ref(false)
const form = ref({
  oldPassword: '',
  newPassword: '',
  checkPassword: ''
})
const handleClose = () => {
  dialogVisible.value = false
  form.value.oldPassword = ''
  form.value.newPassword = ''
  form.value.checkPassword = ''
}
const submitForm = async () => {
  // 弹出修改密码的弹框，输入旧密码和新密码，再次确认新密码，校验两次新密码是否一致，再调用接口修改密码
  // 校验两次新密码是否一致
  if (form.value.newPassword !== form.value.checkPassword) {
    ElMessage.error('两次新密码不一致')
    return
  }
  const param = {
    userPassword: form.value.oldPassword,
    newPassword: form.value.newPassword,
    checkPassword: form.value.checkPassword
  }
  const res: any = await forgetPassword(param);
  if (res.code !== 0) {
    ElMessage.error(res.message)
    return
  }
  dialogVisible.value = false
  ElMessage.success('修改成功')
}

const updateAvatarHandle: UploadProps['onSuccess'] = async (res) => {
  const userAvatar = res.data.ossFilePath
  const update_res: any = await updateUser({userAvatar})
  if (update_res.code !== 0){
    ElMessage.error(update_res.message);
    return;
  }
  ElMessage.success("更换头像成功");

  await getUserInfo();
}
const isUserNameEdit = ref(false)
const newUserName = ref('')
const enterEditUserName = () => {
  isUserNameEdit.value = true
  newUserName.value = user.value.userName
}
const updateUserName = async () => {
  if (newUserName.value === store.state.user.userName){
    isUserNameEdit.value = false
    return;
  }
  const res: any = await updateUser({userName: newUserName.value})
  if (res.code !== 0){
    ElMessage.error(res.message);
    return;
  }
  ElMessage.success("修改成功");
  isUserNameEdit.value = false
  await getUserInfo();
}

const newBaseUserInfo = ref({
  gender: ''
})

const updateBaseUserInfo = async () => {
  if (newBaseUserInfo.value.gender === store.state.user.gender){
    isUserBaseInfoEdit.value = false
    return;
  }
  const res: any = await updateUser(newBaseUserInfo.value)
  if (res.code !== 0){
    ElMessage.error(res.message);
    return;
  }
  ElMessage.success("修改成功");
  isUserBaseInfoEdit.value = false
  await getUserInfo();
}
const isUserBaseInfoEdit = ref(false)
const editInfo = () => {
  isUserBaseInfoEdit.value = true

  newBaseUserInfo.value.gender = store.state.user.gender.toString()

}

const loading = ref(false)

</script>

<template>
  <div style="display: flex;justify-content: space-between;margin-top: 70px;margin-bottom: 50px">
    <h1>您的信息</h1>
    <div class="modifyPass" style="display: flex;margin-right: 100px;height: 40px;cursor: pointer" @click="dialogVisible = true">
      <div class="iconBox">
        <svg t="1703076842917" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="17678" width="32" height="32"><path d="M630.646 348.503c-0.026 26.23 21.222 47.514 47.452 47.54s47.514-21.223 47.534-47.448v-0.092a47.493 47.493 0 1 0-94.986 0z" fill="currentColor" p-id="17679"></path><path d="M142.653 878.71V699.494l263.721-263.726-0.624-3.123a241.178 241.178 0 0 1-4.69-47.206c0-132.419 107.73-240.149 240.148-240.149 132.409 0 240.128 107.73 240.128 240.144 0 132.408-107.72 240.128-240.128 240.128-25.508 0-50.677-4.04-74.864-12.007l-7.808-2.575v2.483h-29.374v86.59h-85.499v86.574H357.08V878.7H142.653z m498.555-681.32c-103.69 0-188.047 84.357-188.047 188.043a188.32 188.32 0 0 0 4.675 41.748c3.84 16.901 0.727 30.095-9.519 40.335L194.755 721.065v105.544h110.213v-92.073h86.584v-86.574h85.494v-75.295c10.025-3.082 37.1-10.63 61.486-10.63 7.675 0 14.464 0.748 20.178 2.238 34.887 9.052 80.558 9.196 82.483 9.196 103.685 0 188.037-84.352 188.037-188.032 0.01-103.696-84.342-188.048-188.022-188.048z" fill="currentColor" p-id="17680"></path></svg>
      </div>
      <div style="font-size: small;display: flex;flex-direction: column;justify-content: space-evenly" >
        <p class="op" style="font-weight: bold;">更改密码</p>
        <p class="tip" >安全性</p>
      </div>
    </div>
  </div>
  <el-card class="user-avatar-info" v-if="user">
    <template #header>
      <div class="user-avatar" style="display: flex;">
        <el-avatar :size="150" :src="userImg"></el-avatar>
        <div class="avatar-op">
          <span>添加照片，个性化你的账户。 你的个人资料头像会显示在你使用的 API平台 Web界面上。</span>
          <div>
            <el-upload
                class="upload-demo"
                :with-credentials="true"
                :action="baseURL + '/api/user/uploadAvatar'"
                :show-file-list="false"
                :limit="1"
                :on-success="updateAvatarHandle"
                :before-upload="beforeImgUpload"
                :v-loading="true"
            >
              <el-button>更换头像</el-button>
            </el-upload>
            </div>
        </div>
      </div>
    </template>

    <div style="display: flex;justify-content: space-between">
      <el-descriptions style="margin: auto 0">
        <el-descriptions-item label="用户昵称">
          <span style="font-weight: bold" v-if="!isUserNameEdit">{{ user.userName }}</span>
          <el-input v-else v-model="newUserName" style="width: 200px"></el-input>
        </el-descriptions-item>
      </el-descriptions>
      <el-button text type="primary" @click="enterEditUserName" v-if="!isUserNameEdit">编辑昵称</el-button>
      <el-button text type="danger" @click="updateUserName" v-if="isUserNameEdit">保存</el-button>
    </div>
  </el-card>

  <el-card class="user-base-info" v-if="user">
    <template #header>
      <div class="card-header">
        <span>个人资料信息</span>
        <div>
          <el-button type="primary" text @click="editInfo" v-if="!isUserBaseInfoEdit">编辑个人资料信息</el-button>
          <el-button type="danger" text @click="updateBaseUserInfo" v-else>保存</el-button>
        </div>
      </div>
    </template>

    <el-descriptions :column="1" v-if="!isUserBaseInfoEdit">
      <el-descriptions-item label="性别"><span style="font-weight: bold">{{ user.gender === 0 ? '男':'女' }}</span></el-descriptions-item>
      <el-descriptions-item label="注册时间"><span style="font-weight: bold">{{ formatDate(user.createTime) }}</span></el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="1" v-else>
      <el-descriptions-item label="性别">
        <el-select v-model="newBaseUserInfo.gender" placeholder="请选择性别">
          <el-option label="男" value="0"></el-option>
          <el-option label="女" value="1"></el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="注册时间"><span style="font-weight: bold">{{ formatDate(user.createTime) }}</span></el-descriptions-item>
    </el-descriptions>
  </el-card>
  <el-card class="user-key-info" v-if="user">
    <template #header>
      <div class="card-header">
        <span>个人密钥信息</span>
        <div>
          <el-button type="primary" text @click="resetKey">重置密钥</el-button>
        </div>
      </div>
    </template>
    <el-descriptions :column="1">
      <el-descriptions-item label="accessKey"><span style="font-weight: bold">{{ store.state.user.accessKey }}</span></el-descriptions-item>
      <el-descriptions-item label="secretKey"><span style="font-weight: bold">{{ store.state.user.secretKey }}</span></el-descriptions-item>
    </el-descriptions>
  </el-card>

  <el-dialog title="修改密码" v-model="dialogVisible" width="30%" :before-close="handleClose">
    <el-form label-width="80px">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="form.oldPassword" type="password" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" type="password"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPassword">
        <el-input v-model="form.checkPassword" type="password"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
}
.card-header span {
  font-size: large;
  font-weight: bold;
}
.el-card {
  margin: 10px 0;
}
.avatar-op {
  color:#323130;
  width:400px;
  display: flex;
  flex-direction: column;
  margin: auto 20px;
  justify-content: space-evenly;
  height: 150px
}
.iconBox{
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #cfe5f6;
  color: #136fc9;
  transition: .1s;
  margin-right: 10px;;
}

.op {
  color: #323130;
  transition: .1s;
}
.tip {
  color: #605e5c;
  transition: .1s;
}
.modifyPass:hover .iconBox{
  background-color: #197ed0;
  color: #ffffff;
}
.modifyPass:hover .op,.modifyPass:hover .tip{
  color: #197ed0;
}
</style>