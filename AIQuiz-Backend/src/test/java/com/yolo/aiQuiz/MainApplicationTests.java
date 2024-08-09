package com.yolo.aiQuiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yolo.aiQuiz.manager.AiManager;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private AiManager aiManager;

    @Test
    void contextLoads() {
//        String string = aiManager.doSyncRequest(null, "1+2等于几", null);
        String string = aiManager.doSyncRequest("1+2等于几", null, null);
        System.out.println(string);

    }


}
