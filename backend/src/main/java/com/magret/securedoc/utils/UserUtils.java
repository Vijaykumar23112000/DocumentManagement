package com.magret.securedoc.utils;

import com.magret.securedoc.entity.RoleEntity;
import com.magret.securedoc.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserUtils {
    public static UserEntity createUserEntity(String firstName , String lastName , String email , RoleEntity role){
        return UserEntity
                .builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(LocalDateTime.now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .mfa(false)
                .enabled(false)
                .loginAttempts(0)
                .qrCodeSecret(StringUtils.EMPTY)
                .phone(StringUtils.EMPTY)
                .bio(StringUtils.EMPTY)
                .imageUrl("http://cdn-icons-png.flaticon.com/512/149/149071.png")
                .role(role)
                .build();
    }
}
