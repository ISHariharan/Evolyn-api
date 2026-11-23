package com.evolyn.api.Weight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

/**
 * What the browser sends to save a weight.
 * recordedOn is optional (default: today in India). The Data Service checks it
 * isn't in the future, using India time, so a 1 am entry isn't refused because
 * the server's clock is on UTC.
 */
public class WeightDTO {

    @NotNull(message = "userId is required")
    private UUID userId;

    private LocalDate recordedOn;

    @NotNull(message = "Enter your weight in kg")
    @DecimalMin(value = "20", message = "Weight must be between 20 and 300 kg")
    @DecimalMax(value = "300", message = "Weight must be between 20 and 300 kg")
    @Digits(integer = 3, fraction = 2, message = "Weight can have at most 2 decimal places")
    private BigDecimal weightKg;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public LocalDate getRecordedOn() { return recordedOn; }
    public void setRecordedOn(LocalDate recordedOn) { this.recordedOn = recordedOn; }

    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
}
