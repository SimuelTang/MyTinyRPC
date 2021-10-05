package pers.simuel.trpc.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author simuel_tang
 * @Date 2021/10/5
 * @Time 16:19
 */
@Data
@AllArgsConstructor
public class Product implements Serializable {
    private String name;
    private Integer price;
}
