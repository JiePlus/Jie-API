package com.jie1234.jieapicommon.service;


import com.jie1234.jieapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author jie
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户密钥（accessKey、secretKey）
     * @param accesskey
     * @return
     */
    User getInvokeUser(String accesskey);
}
