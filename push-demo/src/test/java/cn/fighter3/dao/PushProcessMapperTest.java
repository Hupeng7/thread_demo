package cn.fighter3.dao;

import cn.fighter3.entity.PushProcess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PushProcessMapperTest {
   @Autowired
   private PushProcessMapper pushProcessMapper;
    @Test
    void countPushRecordsByFlag() {
        System.out.println(pushProcessMapper.countPushRecordsByState(0));
    }

    @Test
    void findPushRecordsByFlagLimit() {
        List<PushProcess> pushProcesses=pushProcessMapper.findPushRecordsByStateLimit(0,0,500);
    }

}