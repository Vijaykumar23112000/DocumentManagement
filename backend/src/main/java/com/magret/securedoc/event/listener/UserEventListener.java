package com.magret.securedoc.event.listener;

import com.magret.securedoc.event.UserEvent;
import com.magret.securedoc.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event){
        switch(event.getType()){
            case REGISTRATION -> emailService.sendNewAccountEmail(event.getUser().getFirstName() , event.getUser().getEmail() , (String)event.getData().get("key"));
            case RESETPASSWORD -> emailService.sendPasswordResetEmail(event.getUser().getFirstName() , event.getUser().getEmail() , (String)event.getData().get("key"));
            default -> {}
        }
    }

}
