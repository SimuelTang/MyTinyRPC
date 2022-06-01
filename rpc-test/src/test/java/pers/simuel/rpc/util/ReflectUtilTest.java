package pers.simuel.rpc.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import pers.simuel.rpc.utils.ReflectUtil;

import javax.swing.*;
import java.util.Set;

/**
 * @Author simuel_tang
 * @Date 2022/5/21
 * @Time 11:18
 */
@Slf4j
public class ReflectUtilTest {
    public static void main(String[] args) {
        String stackTrace = ReflectUtil.getStackTrace();
        log.info("current stack trace:{}", stackTrace);
        Set<Class<?>> classes = ReflectUtil.getClasses("pers.simuel.rpc");
    }
}
