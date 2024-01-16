package com.jie1234.jieapicommon.service;


import com.jie1234.jieapicommon.model.entity.InterfaceInfo;

/**
* @author Jie
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-11-06 22:39:15
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询接口是否存在（请求路径，请求方法，请求参数）
     * @param host 请求host
     * @param path 请求路径
     * @param method 请求方法
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String host, String path, String method);
}
