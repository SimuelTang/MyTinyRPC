package pers.simuel.rpc.client;

import pers.simuel.rpc.model.RpcRequest;

public interface RPCClient {
    Object sendRequest(RpcRequest request);
}
