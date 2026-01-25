package com.evolyn.api.UserDetailsVerify;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDetailsVerifyValidation {
    @NotBlank(message= "Email Not Found")
    @NotNull(message= "Email Not Found")
    private String email;

    @NotBlank(message="Password Not Found")
    @NotNull(message="Password Not Found")
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}