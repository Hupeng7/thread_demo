package cn.fighter3.service;

import java.util.concurrent.ExecutionException;

/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description
 */
public interface PushProcessService {
    void pushData() throws ExecutionException, InterruptedException;
}
