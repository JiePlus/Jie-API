package com.jie1234.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jie1234.jieapicommon.model.entity.InterfaceInfo;
import com.jie1234.jieapicommon.model.entity.User;
import com.jie1234.jieapicommon.service.InnerInterfaceInfoService;
import com.jie1234.project.common.ErrorCode;
import com.jie1234.project.exception.BusinessException;
import com.jie1234.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String host, String path, String method) {
        if (StringUtils.isAnyBlank(host, path, method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("host", host).eq("path",path).eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
