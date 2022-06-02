package pers.simuel.rpc.loadbalancer.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import pers.simuel.rpc.loadbalancer.RpcLoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author simuel_tang
 * @Date 2022/5/1
 * @Desc
 */
public class RandomLoadBalance implements RpcLoadBalance {

    final List<Instance> validInstanceList = new ArrayList<>();

    // 普通轮询时采用的下标
    private final AtomicInteger index = new AtomicInteger();

    @Override
    public Instance select(List<Instance> instanceList) {
        int totalWeight = 0;
        int minWeight = Integer.MAX_VALUE;
        int maxWeight = 0;
        for (Instance instance : instanceList) {
            int currWeight = (int) instance.getWeight();
            totalWeight += instance.getWeight();
            minWeight = Math.min(minWeight, currWeight);
            maxWeight = Math.max(maxWeight, currWeight);
            if (currWeight > 0) {
                validInstanceList.add(instance);
            }
        }
        if (maxWeight > 0 && minWeight < maxWeight) {
            int randomIdx = (int) (Math.random() * maxWeight);
            return instanceList.get(randomIdx % totalWeight);
        }

        // 权重相等时采用普通轮询
        if (index.get() > validInstanceList.size()) {
            int val = index.get() % validInstanceList.size();
            index.set(val);
        }
        return validInstanceList.get(index.getAndIncrement());
    }


}

