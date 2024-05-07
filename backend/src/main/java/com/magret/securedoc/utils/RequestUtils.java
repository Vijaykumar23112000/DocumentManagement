package com.magret.securedoc.utils;

import com.magret.securedoc.domain.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class RequestUtils {

    public static Response getResponse(HttpServletRequest request , Map<? , ?> data , String message , HttpStatus status){
        return new Response(LocalDateTime.now().toString() , status.value() , request.getRequestURI() , HttpStatus.valueOf(status.value()),  message , StringUtils.EMPTY , data);
    }
}
