package cn.fighter3.controller;


import cn.fighter3.common.Result;
import cn.fighter3.dto.PushProcessDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description
 */
@RestController
@Slf4j
public class ReceiveProcessController {

    @PostMapping("/process/receive")
    public Result receiveProcess(@RequestBody PushProcessDTO pushProcessDTO) {
        //仅演示，不做任何业务处理，直接返回成功
        log.info(pushProcessDTO.getId() + " already got!");
        return Result.ok();
    }
}
