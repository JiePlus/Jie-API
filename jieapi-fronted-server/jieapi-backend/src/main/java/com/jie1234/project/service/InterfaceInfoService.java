package com.jie1234.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jie1234.jieapicommon.model.entity.InterfaceInfo;

/**
* @author Jie
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-11-06 22:39:15
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add 是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
