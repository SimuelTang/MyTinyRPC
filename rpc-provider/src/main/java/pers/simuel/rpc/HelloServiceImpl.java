package pers.simuel.rpc;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.service.HelloObject;
import pers.simuel.rpc.service.HelloService;

/**
 * @Author simuel_tang
 * @Date 2021/12/10
 * @Time 10:31
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(HelloObject helloObject) {
        log.info("收到来自{}的消息", helloObject.getId());
        return "Hello " + helloObject.getMessage();
    }
}
