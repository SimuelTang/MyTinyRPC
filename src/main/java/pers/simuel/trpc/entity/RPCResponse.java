package pers.simuel.trpc.entity;

import lombok.Data;
import pers.simuel.trpc.enumeration.ResponseCode;

import java.io.Serializable;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 16:35
 */
@Data
public class RPCResponse<T> implements Serializable {
    // 响应状态码
    private Integer statusCode;
    // 附加信息
    private String message;
    // 解析后的数据
    private T data;

    public static <T> RPCResponse<T> success(T data) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RPCResponse<T> failure(ResponseCode code) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
