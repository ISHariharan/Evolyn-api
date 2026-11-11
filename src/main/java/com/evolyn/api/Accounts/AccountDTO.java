package com.evolyn.api.Accounts;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** What the browser sends to create or edit an account. */
public class AccountDTO {

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotBlank(message = "Account name is required")
    @Size(max = 60, message = "Account name can be at most 60 characters")
    private String name;

    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "BANK|CREDIT_CARD|CASH|WALLET",
             message = "Account type must be BANK, CREDIT_CARD, CASH or WALLET")
    private String accountType;

    // Optional, but if given it must be exactly 4 digits. We never accept a full number.
    @Pattern(regexp = "^$|\\d{4}", message = "Last 4 digits must be exactly 4 numbers")
    private String last4;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getLast4() { return last4; }
    public void setLast4(String last4) { this.last4 = last4; }
}
