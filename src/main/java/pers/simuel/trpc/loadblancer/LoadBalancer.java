package pers.simuel.trpc.loadblancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2021/10/31
 * @Time 21:47
 */
public interface LoadBalancer {
    // 通过均衡策略来选择服务
    Instance select(List<Instance> instances);
}
