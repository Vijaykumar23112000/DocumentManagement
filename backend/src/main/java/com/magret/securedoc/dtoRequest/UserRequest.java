package com.magret.securedoc.dtoRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @NotEmpty(message = "Oops...First Name Cannot Be Empty Or Null")
    private String firstName;

    @NotEmpty(message = "Oops...Last Name Cannot Be Empty Or Null")
    private String lastName;

    @NotEmpty(message = "Oops...Email Cannot Be Empty Or Null")
    @Email(message = "Oops...Invalid Email Address")
    private String email;

    @NotEmpty(message = "Oops...Password Cannot Be Empty Or Null")
    private String password;

    private String bio;

    private String phone;
}