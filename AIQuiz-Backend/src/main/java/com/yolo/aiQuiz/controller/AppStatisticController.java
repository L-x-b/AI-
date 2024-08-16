package com.yolo.aiQuiz.controller;

import com.yolo.aiQuiz.common.BaseResponse;
import com.yolo.aiQuiz.common.ErrorCode;
import com.yolo.aiQuiz.common.ResultUtils;
import com.yolo.aiQuiz.exception.ThrowUtils;
import com.yolo.aiQuiz.mapper.UserAnswerMapper;
import com.yolo.aiQuiz.model.dto.statistic.AppAnswerCountDTO;
import com.yolo.aiQuiz.model.dto.statistic.AppAnswerResultCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {

    @Resource
    private UserAnswerMapper userAnswerMapper;

    /**
     * 热门应用回答统计
     * @return
     */
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
    }


    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
    }

}
