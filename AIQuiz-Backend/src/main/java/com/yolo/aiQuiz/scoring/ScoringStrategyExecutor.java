package com.yolo.aiQuiz.scoring;

import com.yolo.aiQuiz.common.ErrorCode;
import com.yolo.aiQuiz.exception.BusinessException;
import com.yolo.aiQuiz.model.entity.App;
import com.yolo.aiQuiz.model.entity.UserAnswer;
import com.yolo.aiQuiz.model.enums.AppTypeEnum;
import com.yolo.aiQuiz.model.enums.ScoringStrategyEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评分策略执行器
 */
@Service
public class ScoringStrategyExecutor {

    @Resource
    private List<ScoringStrategy> scoringStrategyList;

    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        if (appType == null || scoringStrategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，为找到匹配的策略");
        }
        // 根据注解获取策略
        for (ScoringStrategy strategy : scoringStrategyList) {
            if (strategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)) {
                ScoringStrategyConfig scoringStrategyConfig = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                if (scoringStrategyConfig.appType() == appType && scoringStrategyConfig.scoringStrategy() == scoringStrategy) {
                    return strategy.doScore(choices, app);
                }
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，为找到匹配的策略");
    }

}
