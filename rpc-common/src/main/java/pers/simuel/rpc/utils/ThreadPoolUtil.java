package pers.simuel.rpc.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    
    private static final Long KEEP_ALIVE_TIME = 60L;
    
    public static ThreadPoolExecutor makeServerThreadPool(final String serviceName, int corePoolSize, int maxPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "netty-rpc-" + serviceName + "-" + r.hashCode()),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
