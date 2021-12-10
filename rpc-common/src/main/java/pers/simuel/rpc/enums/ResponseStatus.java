package pers.simuel.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 10:50
 */
@AllArgsConstructor
public enum ResponseStatus {
    SUCCESS("调用方法成功", 200),
    Failure("调用方法失败", 500),
    METHOD_NOT_FOUND("找不到该方法", 404);

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

    String msg;
    int code;
}
