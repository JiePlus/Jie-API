package com.jie1234.project.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserKeyQueryRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
