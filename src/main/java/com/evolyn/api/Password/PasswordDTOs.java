package com.evolyn.api.Password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Request bodies for the password endpoints. Never log them: they hold passwords and recovery codes. */
public final class PasswordDTOs {

    private PasswordDTOs() {}

    /** Forgot password: { email, recoveryCode, password } */
    public static class Reset {
        @NotBlank(message = "Enter your email")
        @Size(max = 255, message = "Email is too long")
        private String email;
        @NotBlank(message = "Enter your recovery code")
        @Size(max = 40, message = "That doesn't look like a recovery code")
        private String recoveryCode;
        @NotBlank(message = "Enter a new password")
        @Size(min = 8, max = 72, message = "Password must be 8 to 72 characters")
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRecoveryCode() { return recoveryCode; }
        public void setRecoveryCode(String recoveryCode) { this.recoveryCode = recoveryCode; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /** Making a new recovery code needs your current password: { currentPassword } */
    public static class NewCode {
        @NotBlank(message = "Enter your current password")
        private String currentPassword;
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    }

    /** Change password while logged in: { currentPassword, newPassword } */
    public static class Change {
        @NotBlank(message = "Enter your current password")
        private String currentPassword;
        @NotBlank(message = "Enter a new password")
        @Size(min = 8, max = 72, message = "Password must be 8 to 72 characters")
        private String newPassword;
        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
