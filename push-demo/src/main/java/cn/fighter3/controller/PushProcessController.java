package cn.fighter3.controller;

import cn.fighter3.result.Result;
import cn.fighter3.service.PushProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description
 */
@RestController
public class PushProcessController {
    @Autowired
    private PushProcessService pushProcessService;

    private Logger logger= LoggerFactory.getLogger(PushProcessController.class);

    /**
     * 推送数据接口
     * @return
     */
    @GetMapping("/push")
    public Result pushData() {
        try {
            pushProcessService.pushData();
        } catch (ExecutionException e) {
           logger.error("推送异常",e);
        } catch (InterruptedException e) {
            logger.error("推送异常",e);
        }
        return Result.ok();
    }
}
