package com.evolyn.api.Goals;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Your daily calorie goal: { "kcal": 400 } */
public class DailyGoalDTO {

    @NotNull(message = "Enter a daily goal")
    @Min(value = 50, message = "Daily goal must be between 50 and 5000 kcal")
    @Max(value = 5000, message = "Daily goal must be between 50 and 5000 kcal")
    private Integer kcal;

    public Integer getKcal() { return kcal; }
    public void setKcal(Integer kcal) { this.kcal = kcal; }
}
