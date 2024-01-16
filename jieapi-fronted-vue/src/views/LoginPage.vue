<script setup lang="ts">
import {reactive, ref} from 'vue'
import {ArrowLeft, Lock, Unlock, User} from "@element-plus/icons-vue";
import {userLogin, userRegister} from "../api/user.ts";
import {useRouter} from "vue-router";
import {Action, ElMessage, ElMessageBox, ElNotification} from "element-plus";
import {useStore} from "vuex";
import store from "../store";

// do not use same name with ref
const form = reactive({
  userAccount: '',
  userPassword: ''
})

const router = useRouter()

const loading = ref(false)

const onSubmit = () => {
  // console.log('submit!')
  loading.value = true
  userLogin(form)
      .then(res => {
        loading.value = false
        // console.log(res)
        if (res.code === 0){
          router.push('/index')
          ElNotification.success({
            title: '登录成功',
            type: 'success',
            message: '欢迎使用Api开放平台'
          })
          store.commit('setUser', res.data)
        } else {
          ElNotification.error(res.message)
        }
      })
}

const toLogin = ref(true)

const changeLogin = () => {
  toLogin.value = !toLogin.value
}
const registerForm = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})


const toRegister = async () => {
  if (toLogin.value) {
    ElNotification.error({
      title: '注册失败',
      message: '请先切换到注册页面'
    })
    return
  }
  // 校验两次密码是否一致
  if (registerForm.userPassword !== registerForm.checkPassword) {
    ElNotification.error({
      title: '注册失败',
      message: '两次密码不一致'
    })
    return
  }
  loading.value = true
  const res = await userRegister(registerForm)
  if (res.code === 0) {
    loading.value = false
    ElNotification.success({
      title: '注册成功',
      message: '欢迎使用Api开放平台'
    })
    loading.value = false
    // 用弹框将accessKey和secretKey提示给用户
    ElMessageBox({
      title: '注册成功',
      message: `accessKey为：${res.data.accessKey}，<br/>secretKey为：${res.data.secretKey}，<br/>请妥善保管`,
      showCancelButton: false,
      dangerouslyUseHTMLString: true,
      confirmButtonText: '我已知晓',
      beforeClose: (action: Action, instance: any, done: any) => {
        // console.log(action, instance, done)
        if (action === 'confirm') {
          done()
        } else {
          done()
        }
      }
    }).then(() => {
      registerForm.userAccount = ''
      registerForm.userPassword = ''
      registerForm.checkPassword = ''
      changeLogin()
    })
  } else {
    ElNotification.error({
      title: '注册失败',
      message: res.message
    })
    loading.value = false
    registerForm.userAccount = ''
    registerForm.userPassword = ''
    registerForm.checkPassword = ''
  }
}
</script>

<template>
  <div class="back">
    <div class="white-back" v-if="toLogin">
      <div class="login-box">
        <h3 class="title">Jie-API开放平台</h3>
        <el-form :model="form" label-position="top" @keyup.enter.native="onSubmit">
          <el-form-item>
            <el-input v-model="form.userAccount" :prefix-icon="User" placeholder="请输入账号"/>
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.userPassword" type="password" show-password :prefix-icon="Lock" placeholder="请输入密码"/>
          </el-form-item>
          <el-form-item>
            <el-button :loading="loading" style="width: 100%" type="primary" @click="onSubmit">登录</el-button>
          </el-form-item>
          <el-form-item>
            没有账号？点击<a style="color: blue;cursor: pointer" @click="changeLogin">注册</a>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <div class="white-back" v-else>
      <div class="login-box" style="position: relative">
        <div  style="position: absolute;top: 10px;left: 10px;cursor: pointer;font-size: smaller;color: #666" @click="changeLogin"><el-icon><ArrowLeft /></el-icon>返回</div>
        <h3 class="title">Jie-API开放平台</h3>
        <el-form :model="registerForm" label-position="top" @keyup.enter.native="toRegister">
          <el-form-item>
            <el-input v-model="registerForm.userAccount" :prefix-icon="User" placeholder="请输入账号"/>
          </el-form-item>
          <el-form-item>
            <el-input v-model="registerForm.userPassword" type="password" show-password :prefix-icon="Unlock" placeholder="请输入密码"/>
          </el-form-item>
          <el-form-item>
            <el-input v-model="registerForm.checkPassword" type="password" show-password :prefix-icon="Lock" placeholder="请再次输入密码"/>
          </el-form-item>
          <el-form-item>
            <el-button :loading="loading" style="width: 100%" type="primary" @click="toRegister">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>

</template>

<style scoped>
.back {
  background-image: url('../assets/img/bac1.png');
  background-size: cover;
  background-position: center;
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
}

.white-back {
  background-color: rgba(255, 255, 255, 0.8);
  background-size: cover;
  background-position: center;
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  border-radius: 6px;
  background: #fff;
  width: 300px;
  padding: 40px 40px 15px 40px;
  box-shadow: 0 6px 15px rgba(36,37,38,.1);
}
.title{
  margin: 0 auto 30px auto;
  text-align: center;
  color: #707070;
}
</style>