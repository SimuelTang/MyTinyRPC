package pers.simuel.rpc.protocol;

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
public class RPCResponse<T> implements Serializable {
    // 响应状态码
    private Integer statusCode;
    // 附加信息
    private String message;
    // 解析后的数据
    private T data;

    public static <T> RPCResponse<T> success(T data) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(ResponseStatus.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RPCResponse<T> failure(ResponseStatus status) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setStatusCode(status.getCode());
        response.setMessage(status.getMessage());
        return response;
    }

}
