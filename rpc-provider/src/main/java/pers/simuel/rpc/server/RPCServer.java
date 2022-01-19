package pers.simuel.rpc.server;

public interface RPCServer {
    void start();
    
    <T> void publishService(Object service, Class<T> serviceClass);
}
