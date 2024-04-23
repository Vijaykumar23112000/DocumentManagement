package com.magret.securedoc.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message){
        super(message);
    }
    public ApiException(){
        super(" Oops..An Error Occurred");
    }
}
