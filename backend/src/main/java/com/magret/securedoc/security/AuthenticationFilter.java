package com.magret.securedoc.security;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magret.securedoc.domain.ApiAuthentication;
import com.magret.securedoc.dtoRequest.LoginRequest;
import com.magret.securedoc.enumeration.LoginType;
import com.magret.securedoc.service.JwtService;
import com.magret.securedoc.service.UserService;
import com.magret.securedoc.utils.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationFilter(AuthenticationManager authenticationManager , UserService userService, JwtService jwtService) {
        super(new AntPathRequestMatcher("/user/login", POST.name()) , authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
                throws AuthenticationException, IOException, ServletException
    {
        try {
            var user = new ObjectMapper()
                    .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE , true)
                    .readValue(request.getInputStream() , LoginRequest.class);
            userService.updateLoginAttempt(user.getEmail() , LoginType.LOGIN_ATTEMPT);
            var authentication = ApiAuthentication.unauthenticated(user.getEmail() , user.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        } catch(Exception exception){
            log.error(exception.getMessage());
            RequestUtils.handleErrorResponse(request , response , exception);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication)
                throws IOException, ServletException
    {
        super.successfulAuthentication(request, response, chain, authentication);
    }
}
