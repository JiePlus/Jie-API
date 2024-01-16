package com.jie1234.project.model.vo;

import com.jie1234.jieapicommon.model.entity.InterfaceInfo;
import lombok.Data;

/**
 * 接口信息封装视图
 */
@Data
public class InterfaceInfoVo extends InterfaceInfo {

    /**
     * 调用次数
     */
    private Integer totalCount;

    private static final long serialVersionUID = 1L;
}
