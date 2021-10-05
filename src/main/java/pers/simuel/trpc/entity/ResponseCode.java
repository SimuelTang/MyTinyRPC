package pers.simuel.trpc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    
    SUCCESS(200, "OK"),
    ERROR(500, "Error");
    
    int code;
    String message;
    
}
