package com.stctest.anycompmarketplace.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.cors.CorsUtils;

import com.stctest.anycompmarketplace.enums.ErrorCode;
import com.stctest.anycompmarketplace.exception.CustomFailureException;
import com.stctest.anycompmarketplace.response.BaseResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

/**
 * Global exception handling is mainly to convert it into a unified error message and return it to the front end.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles return of missing parameters
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse handleMissingParamsException(HttpServletRequest request, HttpServletResponse response,
                                                     MissingServletRequestParameterException ex) {
        BaseResponse responseBody = new BaseResponse(ErrorCode.PARAMS_MISSING);
        responseBody.setMsg("Missing Parameters: " + ex.getParameterName());
        handleCors(request, response);
        return responseBody;
    }

    /**
     * Handles return of enum type parameters
     */
    @ExceptionHandler(ConversionFailedException.class)
    public BaseResponse handleConversionFailedException(HttpServletRequest request, HttpServletResponse response,
                                                        ConversionFailedException ex) {
        BaseResponse responseBody = new BaseResponse(ErrorCode.PARAMS_ENUM_CONVERT_FAIL);
        responseBody.setMsg("Conversion Failed: " + ex.getMessage());
        handleCors(request, response);
        return responseBody;
    }
    /**
     * Handles responses for unsupported request methods
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse handleMethodNotAllowedException(HttpServletRequest request, HttpServletResponse response,
                                                        HttpRequestMethodNotSupportedException ex) throws Exception {
        handleCors(request, response);
        return new BaseResponse(ErrorCode.METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(JSONException.class)
    public Object handleUnhandleException(HttpServletRequest request, HttpServletResponse response, JSONException ex) {
        handleCors(request, response);
        return new BaseResponse<>(ErrorCode.JSON_ERROR);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public BaseResponse handleAuthorizationDeniedException(HttpServletRequest request, HttpServletResponse response,
                                                        AuthorizationDeniedException ex) {
        BaseResponse responseBody = new BaseResponse(ErrorCode.ACCESS_DENIED);
        responseBody.setMsg(ex.getMessage());
        handleCors(request, response);
        return responseBody;
    }

    /**
     * Handle all API exceptions
     */
    @ExceptionHandler(CustomFailureException.class)
    @ResponseBody
    public BaseResponse handle(HttpServletRequest request, HttpServletResponse response, CustomFailureException exception) {
        handleCors(request, response);
        return new BaseResponse(exception.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public Object handleUnhandleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        handleCors(request, response);
        return new BaseResponse<>(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * Handle cross-domain
     */
    private static void handleCors(HttpServletRequest request, HttpServletResponse response) {
        if (CorsUtils.isCorsRequest(request) && StringUtils.isBlank(response.getHeader("Vary"))) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Vary", "Origin");
        }
    }

}
