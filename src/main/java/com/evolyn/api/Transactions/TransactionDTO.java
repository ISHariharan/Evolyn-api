package com.evolyn.api.Transactions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * What the browser sends to add or edit a transaction.
 * The annotations are the API layer's job: reject bad input here,
 * so the Data Service only ever receives clean data.
 */
public class TransactionDTO {

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotNull(message = "Account is required")
    private UUID accountId;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "EXPENSE|INCOME", message = "Type must be EXPENSE or INCOME")
    private String type;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "9999999999.99", message = "Amount is too large")
    @Digits(integer = 10, fraction = 2, message = "Amount can have at most 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate transactionDate;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category is too long")
    private String category;

    @NotBlank(message = "Merchant / description is required")
    @Size(max = 100, message = "Merchant / description can be at most 100 characters")
    private String merchant;

    @Size(max = 500, message = "Notes can be at most 500 characters")
    private String notes;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "UPI|CREDIT_CARD|DEBIT_CARD|NET_BANKING|CASH",
             message = "Payment method must be UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING or CASH")
    private String paymentMethod;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getAccountId() { return accountId; }
    public void setAccountId(UUID accountId) { this.accountId = accountId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMerchant() { return merchant; }
    public void setMerchant(String merchant) { this.merchant = merchant; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
