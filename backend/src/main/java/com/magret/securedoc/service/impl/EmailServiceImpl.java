package com.magret.securedoc.service.impl;

import com.magret.securedoc.exception.ApiException;
import com.magret.securedoc.service.EmailService;
import com.magret.securedoc.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    private static final String PASSWORD_RESET_REQUEST = "Password Reset Request";

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String email, String token) {
        try{
            log.error(host +" : " + fromEmail);
            var message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setText(EmailUtils.getEmailMessage(name , host , token));
            message.setTo(email);
            sender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("Unable to send email");
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setText(EmailUtils.getResetPasswordMessage(name,host,token));
            message.setTo(email);
            sender.send(message);
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
