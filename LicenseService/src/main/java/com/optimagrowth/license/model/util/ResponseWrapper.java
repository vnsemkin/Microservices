package com.optimagrowth.license.model.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Response Wrapper
 * @author ihuaylupo
 * @version
 * @since Jun 25, 2018
 */
@JsonInclude(NON_NULL)
public class ResponseWrapper {

    private Object data;
    private Object metadata;
    private List<ErrorMessage> errors;

    /**
     * @param data
     * @param metadata
     * @param errors
     */
    public ResponseWrapper(Object data, Object metadata, List<ErrorMessage> errors) {
        super();
        this.data = data;
        this.metadata = metadata;
        this.errors = errors;
    }
}
