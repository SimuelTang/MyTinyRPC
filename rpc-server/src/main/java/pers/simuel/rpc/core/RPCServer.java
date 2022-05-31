package pers.simuel.rpc.core;

public interface RPCServer {
    void start();
    
    <T> void publishService(Object service, Class<T> serviceClass);
}
