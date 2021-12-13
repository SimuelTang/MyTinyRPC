package pers.simuel.rpc.exceptions;

/**
 * @Author simuel_tang
 * @Date 2021/12/12
 * @Time 10:48
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
    
    public SerializeException(String msg, Exception e) {
        super(msg, e);
    }
}
