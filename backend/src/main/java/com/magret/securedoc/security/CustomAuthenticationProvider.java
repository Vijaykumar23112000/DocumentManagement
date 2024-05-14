package com.magret.securedoc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = (UsernamePasswordAuthenticationToken) authentication;
        var userFromDb = userDetailsService.loadUserByUsername((String)user.getPrincipal());
        if(((String)user.getCredentials()).equals(userFromDb.getPassword())){
            return UsernamePasswordAuthenticationToken.authenticated(userFromDb , "[PASSWORD PROTECTED]" , userFromDb.getAuthorities());
        }
        throw new BadCredentialsException("Unable To Login User");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
