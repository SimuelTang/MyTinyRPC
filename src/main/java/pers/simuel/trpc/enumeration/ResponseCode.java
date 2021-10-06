package pers.simuel.trpc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    
    SUCCESS(200, "OK"),
    ERROR(500, "Error"),
    METHOD_NOT_FOUND(400, "Method not found");
    
    int code;
    String message;
    
}
