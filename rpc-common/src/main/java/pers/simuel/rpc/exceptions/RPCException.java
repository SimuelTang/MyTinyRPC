package pers.simuel.rpc.exceptions;

import pers.simuel.rpc.enums.RPCError;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 10:09
 */
public class RPCException extends Exception {
    public RPCException() {
    }

    public RPCException(String msg) {
        super(msg);
    }
    
    public RPCException(RPCError error) {
        super(error.getMessage());
    }
}
