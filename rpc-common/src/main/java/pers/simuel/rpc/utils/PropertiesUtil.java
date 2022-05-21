package pers.simuel.rpc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author simuel_tang
 * @Date 2022/5/20
 * @Time 19:08
 */
@Slf4j
public class PropertiesUtil {
    public static String getProperty() {
        try {
            Properties prop = new Properties();
            InputStream in = PropertiesUtil.class.getResourceAsStream("serializer.properties");
            prop.load(in);
            return prop.getProperty("serializer");
        } catch (IOException e) {
            log.error("读取配置文件发生错误", e);
            return null;
        }
    }
}
