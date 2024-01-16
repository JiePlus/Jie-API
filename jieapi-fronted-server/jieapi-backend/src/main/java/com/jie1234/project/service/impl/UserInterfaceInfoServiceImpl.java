package com.jie1234.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie1234.jieapicommon.model.entity.UserInterfaceInfo;

import com.jie1234.project.common.ErrorCode;
import com.jie1234.project.exception.BusinessException;
import com.jie1234.project.mapper.UserInterfaceInfoMapper;
import com.jie1234.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
* @author Jie
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-11-09 21:08:22
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = userInterfaceInfo.getInterfaceInfoId();

        // 创建时，所有参数必须非空
        if (add) {
            if (id <= 0 || userInterfaceInfo.getUserId() <= 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }

        if (userInterfaceInfo.getLeftNum() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用次数必须大于0");
        }

    }


    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 判断
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        }

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum +1");
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.gt("leftNum", 0);

        return this.update(updateWrapper);
    }

    @Override
    public Integer getRemainingCalls(long interfaceInfoId, long userId) {

        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);

        UserInterfaceInfo userInterfaceInfo = list(queryWrapper).get(0);

        if (userInterfaceInfo == null){
            return null;
        }
        return userInterfaceInfo.getLeftNum();
    }

    @Override
    public boolean saveBatch(Collection<com.jie1234.jieapicommon.model.entity.UserInterfaceInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<com.jie1234.jieapicommon.model.entity.UserInterfaceInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<com.jie1234.jieapicommon.model.entity.UserInterfaceInfo> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(com.jie1234.jieapicommon.model.entity.UserInterfaceInfo entity) {
        return false;
    }

    @Override
    public com.jie1234.jieapicommon.model.entity.UserInterfaceInfo getOne(Wrapper<UserInterfaceInfo> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<com.jie1234.jieapicommon.model.entity.UserInterfaceInfo> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<com.jie1234.jieapicommon.model.entity.UserInterfaceInfo> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }
}




