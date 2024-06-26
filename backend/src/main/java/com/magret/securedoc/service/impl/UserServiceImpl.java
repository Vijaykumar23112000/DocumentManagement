package com.magret.securedoc.service.impl;

import com.magret.securedoc.cache.CacheStore;
import com.magret.securedoc.domain.RequestContext;
import com.magret.securedoc.entity.ConfirmationEntity;
import com.magret.securedoc.entity.CredentialsEntity;
import com.magret.securedoc.entity.RoleEntity;
import com.magret.securedoc.entity.UserEntity;
import com.magret.securedoc.enumeration.Authority;
import com.magret.securedoc.enumeration.EventType;
import com.magret.securedoc.enumeration.LoginType;
import com.magret.securedoc.event.UserEvent;
import com.magret.securedoc.exception.ApiException;
import com.magret.securedoc.repository.ConfirmationRepository;
import com.magret.securedoc.repository.CredentialRepository;
import com.magret.securedoc.repository.RoleRepository;
import com.magret.securedoc.repository.UserRepository;
import com.magret.securedoc.service.UserService;
import com.magret.securedoc.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
//    private final BCryptPasswordEncoder encoder;
    private final CacheStore<String , Integer> userCache;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        var userEntity = userRepository.save(createNewUser(firstName , lastName , email ));
        var credentialEntity = new CredentialsEntity(userEntity , password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity , EventType.REGISTRATION , Map.of("key",confirmationEntity.getKey())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(()->new ApiException("Role Not Found"));
    }

    @Override
    public void verifyAccount(String key){
        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    private UserEntity getUserEntityByEmail(String email) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return userByEmail.orElseThrow(()->new ApiException("User Not Found"));
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key).orElseThrow(()->new ApiException("Confirmation Key Not Found"));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email){
        var role = getRoleName(Authority.USER.name());
        return UserUtils.createUserEntity(firstName , lastName , email , role);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch(loginType){
            case LOGIN_ATTEMPT -> {
                if (userCache.get(userEntity.getEmail())==null){
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts()+1);
                userCache.put(userEntity.getEmail() , userEntity.getLoginAttempts());
                if(userCache.get(userEntity.getEmail())>5){
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(LocalDateTime.now());
                userCache.evict(userEntity.getEmail());
            }
            default -> {}
        }
        userRepository.save(userEntity);
    }
}
