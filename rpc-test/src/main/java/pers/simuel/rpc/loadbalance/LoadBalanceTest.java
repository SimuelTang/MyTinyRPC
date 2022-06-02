package pers.simuel.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import pers.simuel.rpc.loadbalancer.RpcLoadBalance;
import pers.simuel.rpc.loadbalancer.impl.RoundRobinLoadBalance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author simuel_tang
 * @Date 2022/6/2
 * @Desc
 */
public class LoadBalanceTest {
    public static void main(String[] args) {
        RpcLoadBalance loadBalance = new RoundRobinLoadBalance();
        Instance ins1 = new Instance();
        ins1.setWeight(5);
        ins1.setInstanceId("1");
        Instance ins2 = new Instance();
        ins2.setWeight(1);
        ins2.setInstanceId("2");
        Instance ins3 = new Instance();
        ins3.setWeight(1);
        ins3.setInstanceId("3");
        List<Instance> instanceList = Arrays.asList(ins1, ins2, ins3);
        for (int i = 0; i < 7; i++) {
            Instance currIns = loadBalance.select(instanceList);
            System.out.println(currIns);
        }
    }
}
