package com.magret.securedoc.service;

import com.magret.securedoc.entity.RoleEntity;
import com.magret.securedoc.enumeration.Authority;

public interface UserService {
    void createUser(String firstName , String lastName , String email , String password);
    RoleEntity getRoleName(String name);
    void verifyAccount(String key);
}
