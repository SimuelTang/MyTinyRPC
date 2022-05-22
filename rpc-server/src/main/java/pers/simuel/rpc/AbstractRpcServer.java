package pers.simuel.rpc;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.rpc.annotations.RpcService;
import pers.simuel.rpc.annotations.RpcServiceScan;
import pers.simuel.rpc.enums.RPCError;
import pers.simuel.rpc.exceptions.RPCException;
import pers.simuel.rpc.utils.ReflectUtil;

import java.util.Set;

/**
 * @Author simuel_tang
 * @Date 2022/5/21
 * @Time 15:53
 */
@Slf4j
public abstract class AbstractRpcServer implements RPCServer {
    public void scanServices() {
        //启动类的栈帧处于最顶部
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        //检测启动类是否开启了自动扫描
        try {
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(RpcServiceScan.class)) {
                log.error("启动类缺少@ServiceScan注解");
                throw new RPCException(RPCError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RPCException(RPCError.UNKNOWN_ERROR);
        }
        //通过自动扫描配置的值查找RpcService处于哪个包下
        String basePackage = startClass.getAnnotation(RpcServiceScan.class).value();
        if("".equals(basePackage)) {
            //没有配置待扫描的包时就进行全包扫描
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        //找到当前包下的所有类
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        //查找作为服务提供者的类
        for(Class<?> clazz : classSet) {
            if(clazz.isAnnotationPresent(RpcService.class)) {
                String serviceName = clazz.getAnnotation(RpcService.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建{}时有错误发生", clazz);
                    continue;
                }
                //定义RpcService注解时，name默认为空串
                if("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> oneInterface: interfaces){
                        publishService(obj, oneInterface);
                    }
                } else {
                    publishService(obj, clazz);
                }
            }
        }
    }
}
