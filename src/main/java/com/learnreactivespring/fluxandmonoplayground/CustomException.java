package com.learnreactivespring.fluxandmonoplayground;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends Throwable {
    private String message;

    public CustomException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }
}
