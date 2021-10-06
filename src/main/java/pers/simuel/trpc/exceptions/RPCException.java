package pers.simuel.trpc.exceptions;

/**
 * @Author simuel_tang
 * @Date 2021/10/6
 * @Time 15:06
 */
public class RPCException extends RuntimeException {
    public RPCException(RPCError error) {
        super(error.getMessage());
    }
}
