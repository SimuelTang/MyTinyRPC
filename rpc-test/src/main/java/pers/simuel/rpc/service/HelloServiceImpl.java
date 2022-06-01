package pers.simuel.rpc.service;


import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.annotations.RpcService;

@RpcService
@Slf4j
public class HelloServiceImpl implements HelloService {

    public HelloServiceImpl() {

    }

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello " + person.getFirstName() + " " + person.getLastName();
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " is " + age;
    }

    @Override
    public String sayHello(HelloObject helloObject) {
        log.info("收到来自{}的消息", helloObject.getId());
        return "Hello " + helloObject.getMessage();
    }
}
