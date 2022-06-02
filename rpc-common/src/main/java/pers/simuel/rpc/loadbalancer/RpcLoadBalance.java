package pers.simuel.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 用于规范子类的行为
 */
public interface RpcLoadBalance {
    Instance select(List<Instance> instanceList);
}
