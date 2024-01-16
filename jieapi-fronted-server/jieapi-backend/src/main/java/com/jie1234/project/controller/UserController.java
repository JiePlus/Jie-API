package com.jie1234.project.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.jie1234.jieapicommon.model.entity.User;
import com.jie1234.project.common.BaseResponse;
import com.jie1234.project.common.ErrorCode;
import com.jie1234.project.exception.BusinessException;
import com.jie1234.project.minio.MinioTemplate;
import com.jie1234.project.minio.OssFile;
import com.jie1234.project.model.dto.user.UserForgetPasswordRequest;
import com.jie1234.project.model.dto.user.*;
import com.jie1234.project.model.vo.UserVO;
import com.jie1234.project.common.DeleteRequest;
import com.jie1234.project.common.ResultUtils;
import com.jie1234.project.service.UserService;
import com.jie1234.project.utils.KeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 *
 * @author Jie
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private MinioTemplate minioTemplate;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<UserKeyQueryRequest> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        UserKeyQueryRequest result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        return ResultUtils.success(userVO);
    }

    /**
     * 忘记密码
     *
     * @param userForgetPasswordRequest
     * @return
     */
    @PostMapping("/forgetPassword")
    public BaseResponse<Boolean> forgetPassword(@RequestBody UserForgetPasswordRequest userForgetPasswordRequest, HttpServletRequest request) {
        if (userForgetPasswordRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userForgetPasswordRequest.getUserAccount();
        String userPassword = userForgetPasswordRequest.getUserPassword();
        String newPassword = userForgetPasswordRequest.getNewPassword();
        String checkPassword = userForgetPasswordRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userPassword, newPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!newPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        User loginUser = userService.getLoginUser(request);
        if (loginUser != null && StringUtils.isBlank(userAccount)) {
            userAccount = loginUser.getUserAccount();
        }
        boolean result = userService.forgetPassword(userAccount, userPassword, newPassword, checkPassword);
        return ResultUtils.success(result);
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        long id = userService.userRegister(user.getUserAccount(), user.getUserPassword(), user.getUserPassword()).getId();
        User user1 = new User();
        user1.setId(id);
        user1.setUserName(user.getUserName());
        user1.setUserAvatar(user.getUserAvatar());
        user1.setGender(user.getGender());

        boolean result = userService.updateById(user1);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userUpdateRequest.getId() == null) {
            User loginUser = userService.getLoginUser(request);
            if (loginUser == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            userUpdateRequest.setId(loginUser.getId());
        }

        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        // 如果修改了密码，需要重新加密
        if (StringUtils.isNotBlank(user.getUserPassword())) {
            user.setUserPassword(userService.encryptPassword(user.getUserPassword()));
        }
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserVO> getUserById(int id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        List<User> userList = userService.list(queryWrapper);
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }

    /**
     * 分页获取用户列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        long current = 1;
        long size = 10;
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 重置用户的 accessKey 和 secretKey
     *
     * @param request 请求
     * @return 新的accessKey 和 secretKey
     */
    @PostMapping("/restKey")
    public BaseResponse<UserKeyQueryRequest> restUserKey(HttpServletRequest request) {
        // 判断用户是否登录
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        String accessKey = KeyUtil.getAccessKey(user.getUserAccount());
        String secretKey = KeyUtil.getSecretKey(accessKey);

        // 更新用户的 accessKey 和 secretKey
        User user1 = new User();
        user1.setId(user.getId());
        user1.setAccessKey(accessKey);
        user1.setSecretKey(secretKey);
        boolean result = userService.updateById(user1);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        UserKeyQueryRequest keyQueryRequest = new UserKeyQueryRequest();
        keyQueryRequest.setId(user.getId());
        keyQueryRequest.setAccessKey(accessKey);
        keyQueryRequest.setSecretKey(secretKey);

        return ResultUtils.success(keyQueryRequest);
    }

    /**
     * 上传头像
     *
     * @param file 文件
     * @param bucketName 桶名
     * @return 返回 OSS 文件路径
     * @throws IOException 抛出异常
     */
    @PostMapping("/uploadAvatar")
    public BaseResponse<OssFile> uploadAvatar(HttpServletRequest request, MultipartFile file, String bucketName) throws IOException {
        // 判断用户是否登录
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String fileName = "api" + IdUtil.simpleUUID() + '.' + FileTypeUtil.getType(file.getInputStream());

        OssFile path = minioTemplate.putObject(file.getInputStream(), bucketName, fileName, "userAvatar", user.getId());

        return ResultUtils.success(path);
    }

    /**
     * 获取头像
     *
     * @param bucketName 桶名
     * @param fileName 文件名
     * @return 返回 OSS 文件路径
     */
    @GetMapping("/getAvatar")
    public BaseResponse<String> getAvatar(HttpServletRequest request, String bucketName, String fileName) {
        // 判断用户是否登录
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        if (fileName == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(minioTemplate.getPresignedObjectUrl(bucketName, fileName));
    }


    // endregion
}
