package pers.simuel.rpc.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author simuel_tang
 * @Date 2022/1/23
 * @Time 13:59
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
        log.info("[服务端关闭后将注销所有服务]");
        Runtime.getRuntime().addShutdownHook(new Thread(RegistryUtil::clearRegistry));
    }
}
