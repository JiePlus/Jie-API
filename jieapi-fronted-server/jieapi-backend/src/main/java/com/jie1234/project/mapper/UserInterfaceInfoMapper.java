package com.jie1234.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jie1234.jieapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author Jie
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-11-09 21:08:22
* @Entity com.jie1234.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




