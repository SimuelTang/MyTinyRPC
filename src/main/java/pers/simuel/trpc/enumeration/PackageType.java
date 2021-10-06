package pers.simuel.trpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PackageType {
    REQUEST_PACK(0),
    RESPONSE_PACK(1);
    int code;
}
