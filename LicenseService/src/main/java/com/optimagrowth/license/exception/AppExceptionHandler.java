package com.optimagrowth.license.exception;

import com.optimagrowth.license.model.util.ErrorMessage;
import com.optimagrowth.license.model.util.ResponseWrapper;
import com.optimagrowth.license.model.util.RestErrorList;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.Collections.singletonMap;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(LicenseNotFoundException.class)
    protected ResponseEntity<String> LicenseNotFoundException(LicenseNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * handleException - Handles all the Exception receiving a request, responseWrapper.
     *@param request
     *@param responseWrapper
     *@return ResponseEntity<ResponseWrapper>
     * @user ihuaylupo
     * @since 2018-09-12
     */
    @ExceptionHandler(value = { Exception.class })
    public @ResponseBody ResponseEntity<AddDefaultCharsetFilter.ResponseWrapper> handleException(HttpServletRequest request,
     AddDefaultCharsetFilter.ResponseWrapper responseWrapper){
        return ResponseEntity.ok(responseWrapper);
    }

    /**
     * handleIOException - Handles all the Authentication Exceptions of the application.
     *@param request
     *@param exception
     *@return ResponseEntity<ResponseWrapper>
     * @user ihuaylupo
     * @since 2018-09-12
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseWrapper> handleIOException(HttpServletRequest request, RuntimeException e){

        RestErrorList errorList = new RestErrorList(HttpStatus.NOT_ACCEPTABLE, new ErrorMessage(e.getMessage(), e.getMessage()));
        ResponseWrapper responseWrapper = new ResponseWrapper(null,
            singletonMap("status", HttpStatus.NOT_ACCEPTABLE), errorList);
        return ResponseEntity.ok(responseWrapper);
    }
}
