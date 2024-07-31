package com.yolo.aiQuiz.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum ReviewStatusEnum {

    REJECTED("拒绝", 2),

    APPROVED("通过", 1),

    PENDING_REVIEW("待审核", 0);


    private final String text;
    private final long value;

    ReviewStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return value列表
     */
    public static List<Long> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据value获取枚举
     *
     * @param value value
     * @return 枚举类型
     */
    public static ReviewStatusEnum getEnumByValue(long value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ReviewStatusEnum reviewStatusEnum : ReviewStatusEnum.values()) {
            if (reviewStatusEnum.value == value) {
                return reviewStatusEnum;
            }
        }
        return null;
    }
}
