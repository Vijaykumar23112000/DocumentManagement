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
public class MyOwnAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService; // user we saved in memory

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var user = (UsernamePasswordAuthenticationToken) authentication; // user coming from request
        var userFromMemory = userDetailsService.loadUserByUsername((String)user.getPrincipal());
        var password = (String) user.getCredentials();
        if(password.equals(userFromMemory.getPassword())){
            return UsernamePasswordAuthenticationToken.authenticated(userFromMemory , "[password protected]" , userFromMemory.getAuthorities());
        }
        throw new BadCredentialsException("Unable to login user");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
