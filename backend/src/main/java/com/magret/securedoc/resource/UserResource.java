package com.magret.securedoc.resource;

import com.magret.securedoc.domain.Response;
import com.magret.securedoc.dtoRequest.UserRequest;
import com.magret.securedoc.service.UserService;
import com.magret.securedoc.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path = {"/user"})
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user , HttpServletRequest request){
        userService.createUser(user.getFirstName() , user.getLastName() , user.getEmail() , user.getPassword());
        String message = "Account Created .. Check Your Email To Enable Your Account";
        return ResponseEntity.created(getUri()).body(RequestUtils.getResponse(request , Collections.emptyMap() , message , HttpStatus.CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyUser(@RequestParam("key") String key , HttpServletRequest request){
        userService.verifyAccount(key);
        String message = "Account Verified .. ";
        return ResponseEntity.ok().body(RequestUtils.getResponse(request , Collections.emptyMap() , message , HttpStatus.OK));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest user){
        UsernamePasswordAuthenticationToken unAuthenticated = UsernamePasswordAuthenticationToken.unauthenticated(user.getEmail() , user.getPassword());
        Authentication authentication = authenticationManager.authenticate(unAuthenticated);
        return ResponseEntity.ok().body(Map.of("user",unAuthenticated));
    }

    private URI getUri() {
        return URI.create("");
    }
}
