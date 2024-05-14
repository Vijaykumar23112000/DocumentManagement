package com.magret.securedoc.dtoRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotEmpty(message = "Oops...Email Cannot Be Empty Or Null")
    @Email(message = "Oops...Invalid Email Address")
    private String email;

    @NotEmpty(message = "Oops...Password Cannot Be Empty Or Null")
    private String password;
}
