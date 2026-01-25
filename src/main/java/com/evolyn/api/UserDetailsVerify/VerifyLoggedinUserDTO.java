package com.evolyn.api.UserDetailsVerify;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class VerifyLoggedinUserDTO {
    @NotBlank
    @NotEmpty
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
