package pers.simuel.trpc.server.impl;

import lombok.extern.slf4j.Slf4j;
import pers.simuel.trpc.common.Product;
import pers.simuel.trpc.common.SaleService;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 19:44
 */
@Slf4j
public class SaleServiceImpl implements SaleService {
    @Override
    public String buy(Product product) {
        log.info("服务端接收到购买请求，正在处理...");
        if ("apple".equals(product.getName())) {
            log.info("服务端处理完毕...");
            return "完成对苹果的购买，已花费" + product.getPrice();
        } else {
            return "无当前商品，购买失败";
        }
    }
}
