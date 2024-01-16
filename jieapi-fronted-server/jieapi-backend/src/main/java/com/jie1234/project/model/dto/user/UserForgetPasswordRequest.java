package com.jie1234.project.model.dto.user;

import lombok.Data;

/**
 * 用户忘记密码请求
 */
@Data
public class UserForgetPasswordRequest {

    /**
     * 用户账户
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 校验密码
     */
    private String checkPassword;

}
