package pers.simuel.trpc.client;

import pers.simuel.trpc.entity.RPCRequest;

public interface RPCClient {
    Object sendRequest(RPCRequest rpcRequest);
}
