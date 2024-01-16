package com.jie1234.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * IdRequest
 *
 * @author Jie
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}