package cn.fighter3.service.impl;

import cn.fighter3.service.PushProcessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

@SpringBootTest
public class PushProcessServiceImplTest {
    @Autowired
    private PushProcessService pushProcessService;

    /**
     * 测试多线程请求
     */
    //@Test
    void pushData() {
        try {
            pushProcessService.pushData();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}