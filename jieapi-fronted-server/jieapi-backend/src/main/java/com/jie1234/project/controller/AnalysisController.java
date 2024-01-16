package com.jie1234.project.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jie1234.jieapicommon.model.entity.InterfaceInfo;
import com.jie1234.jieapicommon.model.entity.UserInterfaceInfo;
import com.jie1234.project.annotation.AuthCheck;
import com.jie1234.project.common.BaseResponse;
import com.jie1234.project.common.ErrorCode;
import com.jie1234.project.common.ResultUtils;
import com.jie1234.project.exception.BusinessException;
import com.jie1234.project.mapper.UserInterfaceInfoMapper;
import com.jie1234.project.model.vo.InterfaceInfoVo;
import com.jie1234.project.service.InterfaceInfoService;
import com.jie1234.project.service.UserInterfaceInfoService;
import com.jie1234.project.service.impl.UserInterfaceInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVo>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjectMap = userInterfaceInfoList.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjectMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVo> interfaceInfoVoList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVo);
            int totalNum = interfaceInfoIdObjectMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVo.setTotalCount(totalNum);
            return interfaceInfoVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVoList);
    }

}
