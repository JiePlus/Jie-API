package com.jie1234.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jie1234.jieapicommon.model.entity.UserInterfaceInfo;
import com.jie1234.jieapicommon.service.InnerUserInterfaceInfoService;
import com.jie1234.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public Integer getRemainingCalls(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.getRemainingCalls(interfaceInfoId, userId);
    }
}
