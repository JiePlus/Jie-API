import myAxios from "../plugins/myAxios.ts";

export const checkLeftNum = (params: any) => myAxios.post('/api/userInterfaceInfo/check', params);

export const applyUserInterfaceInfo = (params: any) => myAxios.post('/api/userInterfaceInfo/apply', params);