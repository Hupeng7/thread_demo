package cn.fighter3.util;

import cn.fighter3.entity.PushProcess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class PushUtilTest {
    @Autowired
    private PushUtil pushUtil;

    @Test
    void sendRecord() {
        PushProcess pushProcess = new PushProcess();
        pushProcess.setId(5L);
        System.out.println(pushUtil.sendRecord(pushProcess));
    }
}