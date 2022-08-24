package cn.fighter3.result;

/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description
 */

import java.io.Serializable;

/**
 * @Author 三分恶
 * @Date 2021/3/5
 * @Description
 */
public class Result implements Serializable {
    private Integer code;
    private String message;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result ok() {
        return new Result(200, "推送成功");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
