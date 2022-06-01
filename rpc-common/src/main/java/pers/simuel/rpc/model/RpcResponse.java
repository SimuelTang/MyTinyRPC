package pers.simuel.rpc.model;

import lombok.Data;
import pers.simuel.rpc.enums.ResponseStatus;

import java.io.Serializable;

/**
 * 响应类：只要包含成功与否以及对应的响应消息即可
 *
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 10:44
 */
@Data
public class RpcResponse<T> implements Serializable {
    private String requestId;
    // 响应状态码
    private Integer statusCode;
    // 附加信息
    private String message;
    // 解析后的数据
    private T data;

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseStatus.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> failure(ResponseStatus status) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(status.getCode());
        response.setMessage(status.getMessage());
        return response;
    }

}
