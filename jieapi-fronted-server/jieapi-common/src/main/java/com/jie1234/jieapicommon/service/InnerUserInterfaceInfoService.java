package com.jie1234.jieapicommon.service;


/**
* @author Jie
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-11-09 21:08:22
*/
public interface InnerUserInterfaceInfoService{



    /**
     * 调用接口统计
     *
     * @param interfaceInfoId 接口id
     * @param userId          用户id
     * @return 调用次数
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取当前用户对接口的剩余调用次数
     *
     * @param interfaceInfoId 接口id
     * @param userId 用户id
     * @return 剩余调用次数
     */
    Integer getRemainingCalls(long interfaceInfoId, long userId);
}
