package pers.simuel.trpc.exceptions;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 21:59
 */

public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(String msg, Throwable e) {
        super(msg, e);
    }
}
