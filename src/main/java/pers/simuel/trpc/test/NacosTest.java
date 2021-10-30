package pers.simuel.trpc.test;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

/**
 * @Author simuel_tang
 * @Date 2021/10/30
 * @Time 13:35
 */
public class NacosTest {
    public static void main(String[] args) throws NacosException {
        NamingService naming = NamingFactory.createNamingService("127.0.0.1:8848");
        // name ip port
        naming.registerInstance("nacos.test.3", "127.0.0.1", 8888, "TEST1");
        naming.registerInstance("nacos.test.3", "127.0.0.1", 9999, "DEFAULT");
        naming.registerInstance("nacos.test.4", "127.0.0.1", 9999, "DEFAULT");
        System.out.println(naming.getAllInstances("nacos.test.4"));
    }
}
