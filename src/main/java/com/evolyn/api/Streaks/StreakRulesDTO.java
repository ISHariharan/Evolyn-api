package com.evolyn.api.Streaks;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** A tribe's streak rules: { "minMinutes": 20, "memberPercent": 50 } */
public class StreakRulesDTO {

    @NotNull(message = "Enter the minimum minutes")
    @Min(value = 1, message = "Minimum length must be 1 to 240 minutes")
    @Max(value = 240, message = "Minimum length must be 1 to 240 minutes")
    private Integer minMinutes;

    @NotNull(message = "Enter the % of members needed")
    @Min(value = 1, message = "Members needed must be 1 to 100%")
    @Max(value = 100, message = "Members needed must be 1 to 100%")
    private Integer memberPercent;

    public Integer getMinMinutes() { return minMinutes; }
    public void setMinMinutes(Integer minMinutes) { this.minMinutes = minMinutes; }

    public Integer getMemberPercent() { return memberPercent; }
    public void setMemberPercent(Integer memberPercent) { this.memberPercent = memberPercent; }
}
