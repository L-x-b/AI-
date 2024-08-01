package com.yolo.aiQuiz.scoring;

import com.yolo.aiQuiz.model.entity.App;
import com.yolo.aiQuiz.model.entity.UserAnswer;

import java.util.List;


/**
 * 评分策略
 */
public interface ScoringStrategy {

    UserAnswer doScore(List<String> choices, App app) throws Exception;

}
