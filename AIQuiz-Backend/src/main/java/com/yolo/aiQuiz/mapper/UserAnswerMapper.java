package com.yolo.aiQuiz.mapper;

import com.yolo.aiQuiz.model.dto.statistic.AppAnswerCountDTO;
import com.yolo.aiQuiz.model.dto.statistic.AppAnswerResultCountDTO;
import com.yolo.aiQuiz.model.entity.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Yolo
 * @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
 * @createDate 2024-07-30 17:15:03
 * @Entity com.yolo.aiQuiz.model.entity.UserAnswer
 */
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    @Select("select appId, count(userId) as answerCount from user_answer\n" +
            "    group by appId order by answerCount desc limit 10;")
    List<AppAnswerCountDTO> doAppAnswerCount();

    List<AppAnswerResultCountDTO> doAppAnswerResultCount(Long appId);

}




