package com.optimagrowth.license.model.util;


import lombok.Getter;
import lombok.Setter;

/**
 * Error message class
 * @author ihuaylupo
 * @version 1.0
 * @since Jun 25, 2018
 */
@Getter
@Setter
public class ErrorMessage {

    /**
     * @param message
     * @param code
     * @param detail
     */
    private String message;
    private String code;
    private String detail;


    public ErrorMessage(String message, String code, String detail) {
        super();
        this.message = message;
        this.code = code;
        this.detail = detail;
    }

    public ErrorMessage(String message, String detail) {
        super();
        this.message = message;
        this.detail = detail;
    }

    public ErrorMessage(String message) {
        this(message, "", "");
    }
}
