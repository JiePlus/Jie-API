package com.jie1234.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jie1234.jieapicommon.model.entity.User;
import com.jie1234.jieapicommon.service.InnerUserService;
import com.jie1234.project.common.ErrorCode;
import com.jie1234.project.exception.BusinessException;
import com.jie1234.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accesskey) {
        if (StringUtils.isAnyBlank(accesskey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accesskey", accesskey);
        return userMapper.selectOne(queryWrapper);
    }
}
