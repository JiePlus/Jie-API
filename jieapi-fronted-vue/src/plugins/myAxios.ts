import axios, {AxiosInstance} from "axios"
import {ElMessage} from "element-plus";

const isDev = process.env.NODE_ENV === 'development';

export const baseURL = isDev ? 'http://127.0.0.1:7529' : 'https://forum.chj0620.cn';

const myAxios: AxiosInstance = axios.create({
    baseURL: baseURL,
});

myAxios.defaults.withCredentials = true;

myAxios.interceptors.request.use(function (config) {
    console.log("我要发请求啦")
    return config;
}, function (error) {
    return Promise.reject(error);
});

myAxios.interceptors.response.use((response) => {
    console.log("接收到响应", response)
    return response.data;
}, function (error) {
    ElMessage.error({
        message: error.message,
    })
    if (error.code == 40100){
        window.location.href = '/';
    }
    return Promise.reject(error)
})

export default myAxios;