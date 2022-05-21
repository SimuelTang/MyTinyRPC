package pers.simuel.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author simuel_tang
 * @Date 2022/5/20
 * @Time 19:19
 */
@AllArgsConstructor
public enum SerializerEnum {

    JDK_TYPE(0, "JDK"),
    JSON_TYPE(1, "JSON"),
    PROTOBUF_TYPE(2, "PROTOBUF"),
    HESSIAN_TYPE(3, "HESSIAN"),
    KRYO_TYPE(4, "KRYO");
    
    final int type;
    final String desc;
    
    
}
