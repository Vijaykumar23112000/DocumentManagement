package com.magret.securedoc.service;

import com.magret.securedoc.entity.RoleEntity;
import com.magret.securedoc.enumeration.Authority;
import com.magret.securedoc.enumeration.LoginType;

public interface UserService {
    void createUser(String firstName , String lastName , String email , String password);
    RoleEntity getRoleName(String name);
    void verifyAccount(String key);

    void updateLoginAttempt(String email , LoginType loginType);
}
