package com.magret.securedoc.utils;

public class EmailUtils {
    public static String getEmailMessage(String name , String host , String token){
        return "Hello "+ name +",\n\nYou're New Account has Been Created. Please Click on the link below to verify your account.\n\n"+
                getVerificationUrl(host , token)+"\n\nThe Support Team";
    }

    public static String getResetPasswordMessage(String name , String host , String token){
        return "Hello "+ name +",\n\nYou're Account Password has Been Reset. Please Click on the link below to reset your password.\n\n"+
                getResetPasswordUrl(host , token)+"\n\nThe Support Team";
    }

    private static String getVerificationUrl(String host , String token){
        return host+"/verify/account?token="+token;
    }

    private static String getResetPasswordUrl(String host , String token){
        return host+"/verify/password?token="+token;
    }
}
