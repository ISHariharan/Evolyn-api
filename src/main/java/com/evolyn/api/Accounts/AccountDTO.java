package com.evolyn.api.Accounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
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

    // Optional: balance at the start of openingBalanceDate (for a credit card: amount owed).
    // Can be negative, e.g. an overdrawn account.
    @Digits(integer = 10, fraction = 2, message = "Opening balance can have at most 2 decimal places")
    private BigDecimal openingBalance;

    @PastOrPresent(message = "Opening balance date can't be in the future")
    private LocalDate openingBalanceDate;

    // Credit cards only
    @DecimalMin(value = "0", message = "Credit limit can't be negative")
    @Digits(integer = 10, fraction = 2, message = "Credit limit can have at most 2 decimal places")
    private BigDecimal creditLimit;

    @Min(value = 1, message = "Statement day must be between 1 and 28")
    @Max(value = 28, message = "Statement day must be between 1 and 28")
    private Integer statementDay;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getLast4() { return last4; }
    public void setLast4(String last4) { this.last4 = last4; }

    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }

    public LocalDate getOpeningBalanceDate() { return openingBalanceDate; }
    public void setOpeningBalanceDate(LocalDate openingBalanceDate) { this.openingBalanceDate = openingBalanceDate; }

    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }

    public Integer getStatementDay() { return statementDay; }
    public void setStatementDay(Integer statementDay) { this.statementDay = statementDay; }
}
