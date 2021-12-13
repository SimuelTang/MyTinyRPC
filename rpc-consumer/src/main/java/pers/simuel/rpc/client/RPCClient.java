package pers.simuel.rpc.client;

import pers.simuel.rpc.protocol.RPCRequest;

public interface RPCClient {
    Object sendRequest(RPCRequest request);
}
