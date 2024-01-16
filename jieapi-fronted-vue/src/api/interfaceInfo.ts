import myAxios from "../plugins/myAxios.ts";

export const listTopInvokeInterfaceInfo = () => myAxios.get(`/api/analysis/top/interface/invoke`);


export const listInterfaceInfoByPage = (params) => myAxios.get(`/api/interfaceInfo/list/page?${params}`);

export const addInterfaceInfo = (params) => myAxios.post(`/api/interfaceInfo/add`, params);

export const onlineInterfaceInfo = (params) => myAxios.post(`/api/interfaceInfo/online`, params);

export const offlineInterfaceInfo = (params) => myAxios.post(`/api/interfaceInfo/offline`, params);

export const getInterfaceInfoById = (id) => myAxios.get(`/api/interfaceInfo/get?id=${id}`);

export const updateInterfaceInfo = (params) => myAxios.post(`/api/interfaceInfo/update`, params);

export const deleteInterfaceInfo = (params) => myAxios.post(`/api/interfaceInfo/delete`, params);

export const invokeInterfaceInfoUsingPOST = (params) => myAxios.post(`/api/interfaceInfo/invoke`, params);