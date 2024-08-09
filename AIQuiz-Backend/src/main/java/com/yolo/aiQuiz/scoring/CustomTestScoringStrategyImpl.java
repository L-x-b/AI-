package com.yolo.aiQuiz.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yolo.aiQuiz.model.dto.question.QuestionContentDTO;
import com.yolo.aiQuiz.model.entity.App;
import com.yolo.aiQuiz.model.entity.Question;
import com.yolo.aiQuiz.model.entity.ScoringResult;
import com.yolo.aiQuiz.model.entity.UserAnswer;
import com.yolo.aiQuiz.service.QuestionService;
import com.yolo.aiQuiz.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义测评类应用评分策略实现类
 */
@ScoringStrategyConfig(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategyImpl implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 1. 根据 id 查询到题目和题目信息
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, app.getId())
        );
        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, app.getId())
        );
        // 2. 统计用户每个选择对应的属性个数，如 I = 10 个， E = 5 个
        // 初始化一个对象，用于存储每个选项的计数
        HashMap<String, Integer> optionCount = new HashMap<>();

        List<QuestionContentDTO> questionContentDTOList = JSONUtil.toList(question.getQuestionContent(), QuestionContentDTO.class);
        // 遍历题目列表
        for (QuestionContentDTO questionContentDTO : questionContentDTOList) {
            // 遍历答案列表
            for (String answer : choices) {
                // 遍历题目中的选项
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    // 如果答案和选项的key匹配
                    if (option.getKey().equals(answer)) {
                        // 获取选项的result属性
                        String result = option.getResult();

                        // 在optionCount中增加计数
                        optionCount.put(result, optionCount.getOrDefault(result, 0) + 1);
                    }
                }
            }
        }
        // 3. 遍历每种评分结果，计算哪个结果的得分更高

        // 初始化最高分数和最高分数对应的评分结果
        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResultList.get(0);

        // 遍历评分结果列表
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(), String.class);
            // 计算当前评分结果的得分
            int score = resultProp.stream().mapToInt(prop -> optionCount.getOrDefault(prop, 0)).sum();

            // 如果当前评分结果的得分高于最高分数，则更新最高分数和最高分数对应的评分结果
            if (score > maxScore)
                maxScore = score;
            maxScoringResult = scoringResult;
        }

        // 4. 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        return userAnswer;
    }
}
