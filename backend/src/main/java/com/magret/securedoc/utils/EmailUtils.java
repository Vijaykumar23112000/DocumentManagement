package com.magret.securedoc.utils;

public class EmailUtils {
    public static String getEmailMessage(String name , String host , String key){
        return "Hello "+ name +",\n\nYour New Account has Been Created. Please Click on the link below to verify your account.\n\n"+
                getVerificationUrl(host , key)+"\n\nThe Support Team";
    }

    public static String getResetPasswordMessage(String name , String host , String key){
        return "Hello "+ name +",\n\nYour Account Password has Been Reset. Please Click on the link below to reset your password.\n\n"+
                getResetPasswordUrl(host , key)+"\n\nThe Support Team";
    }

    private static String getVerificationUrl(String host , String key){
        return host+"/verify/account?key="+key;
    }

    private static String getResetPasswordUrl(String host , String key){
        return host+"/verify/password?key="+key;
    }
}
