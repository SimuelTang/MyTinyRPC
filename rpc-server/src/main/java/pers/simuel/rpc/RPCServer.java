package pers.simuel.rpc;

public interface RPCServer {
    void start();
    
    <T> void publishService(Object service, Class<T> serviceClass);
}
