package com.jie1234.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jie1234.jieapicommon.model.entity.User;
import com.jie1234.project.model.dto.user.UserKeyQueryRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author jie1234
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    UserKeyQueryRequest userRegister(String userAccount, String userPassword, String checkPassword);

    // 封装密码加密
    String encryptPassword(String userPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户忘记密码
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param password
     * @return 是否修改成功
     */
    boolean forgetPassword(String userAccount, String userPassword, String password, String checkPassword);
}
