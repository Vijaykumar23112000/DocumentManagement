package com.magret.securedoc.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req->req
                                .requestMatchers("/user/login")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        var mathew = User
                .withDefaultPasswordEncoder()
                .username("mathew")
                .password("{noop}mathew")
                .roles("USER")
                .build();
        var vj = User
                .withDefaultPasswordEncoder()
                .username("vj")
                .password("{noop}vj")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(List.of(mathew , vj));
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider(userDetailsService);
        return new ProviderManager(customAuthenticationProvider);
    }
}
