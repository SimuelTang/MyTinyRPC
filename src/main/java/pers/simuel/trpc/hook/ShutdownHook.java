package pers.simuel.trpc.hook;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.common.util.NacosUtil;

/**
 * @Author simuel_tang
 * @Date 2021/10/31
 * @Time 21:16
 */
@Slf4j
public class ShutdownHook {
    
    private ShutdownHook(){}
    
    private static class HookHolder {
        private static final ShutdownHook SHUTDOWN_HOOK = new ShutdownHook();
    }

    public static ShutdownHook getShutdownHook() {
        return HookHolder.SHUTDOWN_HOOK;
    }

    public void addClearAllHook() {
        log.info("关闭后将注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
        }));
    }
    
}
