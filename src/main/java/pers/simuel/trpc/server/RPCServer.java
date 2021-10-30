package pers.simuel.trpc.server;

public interface RPCServer {
    void start();

    <T> void publishService(Object service, Class<T> serviceClass);
}
