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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping(path = {"/user"})
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user , HttpServletRequest request){
        userService.createUser(user.getFirstName() , user.getLastName() , user.getEmail() , user.getPassword());
        String message = "Account Created .. Check Your Email To Enable Your Account";
        return ResponseEntity.created(getUri()).body(RequestUtils.getResponse(request , Collections.emptyMap() , message , HttpStatus.CREATED));
    }

    private URI getUri() {
        return URI.create("");
    }
}
