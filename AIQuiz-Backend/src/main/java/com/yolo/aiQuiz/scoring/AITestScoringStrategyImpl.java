package com.yolo.aiQuiz.scoring;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yolo.aiQuiz.manager.AiManager;
import com.yolo.aiQuiz.model.dto.question.QuestionAnswerDTO;
import com.yolo.aiQuiz.model.dto.question.QuestionContentDTO;
import com.yolo.aiQuiz.model.entity.App;
import com.yolo.aiQuiz.model.entity.Question;
import com.yolo.aiQuiz.model.entity.UserAnswer;
import com.yolo.aiQuiz.model.vo.QuestionVO;
import com.yolo.aiQuiz.service.QuestionService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自定义测评类应用评分策略实现类
 */
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AITestScoringStrategyImpl implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private AiManager aiManager;

//    @Resource
//    private RedissonClient redissonClient;

    /**
     * AI 评分系统消息
     */
    private static final String AI_TEST_SCORING_SYSTEM_MESSAGE = "你是一位严谨的判题专家，我会给你如下信息：\n" +
            "```\n" +
            "应用名称，\n" +
            "【【【应用描述】】】，\n" +
            "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" +
            "```\n" +
            "\n" +
            "请你根据上述信息，按照以下步骤来对用户进行评价：\n" +
            "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" +
            "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" +
            "```\n" +
            "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" +
            "```\n" +
            "3. 返回格式必须为 JSON 对象";

    private static final String AI_ANSWER_LOCK = "AI_ANSWER_LOCK";

    /**
     * AI 评分结果本地缓存
     */
    private final Cache<String, String> answerCacheMap =
            Caffeine.newBuilder().initialCapacity(1024)
                    // 缓存 5 分钟移除
                    .expireAfterAccess(1L, TimeUnit.DAYS)
                    .build();


    private String buildCacheKey(Long appId, String choicesStr) {
        return DigestUtil.md5Hex(appId + ":" + choicesStr);
    }

    private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentDTOList, List<String> choices) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        ArrayList<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContentDTOList.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContentDTOList.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        userMessage.append(JSONUtil.toJsonStr(questionAnswerDTOList));
        return userMessage.toString();


    }

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();
        String jsonStr = JSONUtil.toJsonStr(choices);

        // 1. 查询缓存
        String key = buildCacheKey(appId, jsonStr);
        String answerJson = answerCacheMap.getIfPresent(key);
        // 命中缓存则直接返回结果
        if (StrUtil.isNotBlank(answerJson)) {
            UserAnswer userAnswer = JSONUtil.toBean(answerJson, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(jsonStr);
            return userAnswer;

        }

        // 定义锁
//        RLock lock = redissonClient.getLock(AI_ANSWER_LOCK + key);
//        try {
        // 竞争分布式锁，等待 3 秒,15 秒自动释放
//            boolean res = lock.tryLock(3, 15, TimeUnit.SECONDS);
//        if (res) {
        // 抢到分布式锁，才能执行AI操作

        // 2. 根据id查询到题目
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
        // 3. 调用 AI 获取结果
        // 封装 Prompt
        String userMessage = getAiTestScoringUserMessage(app, questionContent, choices);
        // AI 生成
        String result = aiManager.doSyncStableRequest(AI_TEST_SCORING_SYSTEM_MESSAGE, userMessage);
        // 处理结果
        int start = result.indexOf('{');
        int end = result.lastIndexOf('}');
        String json = result.substring(start, end + 1).replaceAll("\n\n", "");
        // 4. 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(jsonStr);
        // 将结果保存到缓存中
        answerCacheMap.put(key, JSONUtil.toJsonStr(userAnswer));
        return userAnswer;
//        }
//        } finally {
//            if (lock != null && lock.isLocked()) {
//                if (lock.isHeldByCurrentThread()) {
//                    lock.unlock();
//                }
//            }
//        }
//        return null;
    }
}
