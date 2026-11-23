package com.evolyn.api.Goals;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/** A month's target: { "target": 60000 } (estimated kcal) */
public class GoalDTO {

    @NotNull(message = "Enter a target")
    @DecimalMin(value = "1", message = "Target must be between 1 and 10,000,000 kcal")
    @DecimalMax(value = "10000000", message = "Target must be between 1 and 10,000,000 kcal")
    private BigDecimal target;

    public BigDecimal getTarget() { return target; }
    public void setTarget(BigDecimal target) { this.target = target; }
}
