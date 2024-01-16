/**
 * 接口信息查询请求参数
 */
export type interfaceInfoQueryRequest = {
    id?: number;
    name?: string;
    description?: string;
    method?: string;
    url?: string;
    requestParams?: string;
    requestHeader?: string;
    responseHeader?: string;
    status?: number;
    userId?: number;
};
