import myAxios from "../plugins/myAxios.ts";


/**
 * @description 用户相关接口
 */
export const userLogin = (params) => myAxios.post(`/api/user/login`,params);

export const userRegister = (params) => myAxios.post(`/api/user/register`,params);

export const userLogout = (params) => myAxios.post(`/api/user/logout`,params);

export const getLoginUser = () => myAxios.get(`/api/user/get/login`);

export const listUserByPage = (params) => myAxios.get(`/api/user/list/page?${params}`);

export const getUserById = (id) => myAxios.get(`/api/user/get?id=${id}`);

export const updateUser = (params) => myAxios.post(`/api/user/update`, params);

export const getAvatar = (fileName) => myAxios.get(`/api/user/getAvatar?fileName=${fileName}`);

export const addUser = (params) => myAxios.post(`/api/user/add`, params);

export const restUserKeyApi = (params) => myAxios.post(`/api/user/restKey`, params);

export const forgetPassword = (params) => myAxios.post(`/api/user/forgetPassword`, params);