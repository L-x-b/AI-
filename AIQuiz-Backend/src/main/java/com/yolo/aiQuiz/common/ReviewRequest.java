package com.yolo.aiQuiz.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 审核请求
 */
@Data
public class ReviewRequest implements Serializable {


    private static final long serialVersionUID = -7020768971792408188L;
    /**
     * id
     */
    private Long id;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;
}
