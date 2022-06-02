package pers.simuel.rpc.loadbalancer.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import pers.simuel.rpc.loadbalancer.RpcLoadBalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author simuel_tang
 * @Date 2022/5/1
 * @Desc 加权轮询算法
 */
public class RoundRobinLoadBalance implements RpcLoadBalance {

    // 普通轮询时采用的下标
    private final AtomicInteger index = new AtomicInteger();

    // 记录每个实例的源权重信息
    private final Map<Instance, Integer> insWeightMap = new HashMap<>();

    final List<Instance> validInstanceList = new ArrayList<>();

    @Override
    public Instance select(List<Instance> instanceList) {
        int length = instanceList.size();
        int maxWeight = 0;
        int minWeight = Integer.MAX_VALUE;
        int weightSum = 0;

        // 查找最大和最小权重
        for (Instance instance : instanceList) {
            int weight = (int) instance.getWeight();
            maxWeight = Math.max(maxWeight, weight);
            minWeight = Math.min(minWeight, weight);
            if (weight > 0) {
                validInstanceList.add(instance);
                weightSum += weight;
            }
        }

        // 权重不相等时的轮询方式
        if (maxWeight > 0 && minWeight < maxWeight) {
            Instance maxIns = null;
            maxWeight = 0;
            for (Instance instance : instanceList) {
                int preWeight = insWeightMap.getOrDefault(instance, 0);
                int currWeight = preWeight + (int) instance.getWeight();
                if (currWeight > maxWeight) {
                    maxWeight = currWeight;
                    maxIns = instance;
                }
                insWeightMap.put(instance, currWeight);
            }
            insWeightMap.put(maxIns, maxWeight - weightSum);
            return maxIns;
        }

        // 权重相等时采用普通轮询
        if (index.get() > validInstanceList.size()) {
            int val = index.get() % validInstanceList.size();
            index.set(val);
        }
        return validInstanceList.get(index.getAndIncrement());
    }
}
