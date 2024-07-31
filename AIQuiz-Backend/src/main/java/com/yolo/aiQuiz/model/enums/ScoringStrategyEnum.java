package com.yolo.aiQuiz.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用类型枚举类
 *
 * @author Yolo
 */

@Getter
public enum ScoringStrategyEnum {
    CUSTOM("自定义", 0),
    AI("AI", 1);

    private final String text;

    private final int value;

    ScoringStrategyEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return value列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据value获取枚举
     *
     * @param value value
     * @return 枚举类型
     */
    public static ScoringStrategyEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ScoringStrategyEnum appTypeEnum : ScoringStrategyEnum.values()) {
            if (appTypeEnum.value == value) {
                return appTypeEnum;
            }
        }
        return null;
    }

}
