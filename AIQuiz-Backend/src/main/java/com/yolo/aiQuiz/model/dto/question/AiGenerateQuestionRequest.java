package com.yolo.aiQuiz.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class AiGenerateQuestionRequest implements Serializable {

    private static final long serialVersionUID = 6032708807581926937L;

    /**
     * id
     */
    private Long appId;

    /**
     * 题目数
     */
    int questionNumber = 10;

    /**
     * 选项数
     */
    int optionNumber = 2;


}
