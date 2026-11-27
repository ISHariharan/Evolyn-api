package com.evolyn.api.Activities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * What the browser sends to log or edit an activity.
 * Calories are normally worked out by the Data Service from the activity's MET value
 * and your weight. enteredKcal is for people who have the number from a watch or a gym
 * machine: it's used instead, and tribes see that it was entered.
 */
public class ActivityDTO {

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotBlank(message = "Choose an activity")
    @Pattern(regexp = "\\d{5}", message = "Activity code must be 5 digits")
    private String activityCode;

    // India local time, e.g. 2026-11-22T18:30
    @NotNull(message = "Choose when you started")
    private LocalDateTime startedAt;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be between 1 minute and 24 hours")
    @Max(value = 1440, message = "Duration must be between 1 minute and 24 hours")
    private Integer durationMinutes;

    @DecimalMin(value = "0", message = "Distance must be between 0 and 1,000 km")
    @DecimalMax(value = "1000", message = "Distance must be between 0 and 1,000 km")
    @Digits(integer = 4, fraction = 2, message = "Distance can have at most 2 decimal places")
    private BigDecimal distanceKm;

    @Size(max = 500, message = "Notes can be at most 500 characters")
    private String notes;

    @Pattern(regexp = "TRIBES|PRIVATE", message = "Visibility must be TRIBES or PRIVATE")
    private String visibility;

    // true after the person confirmed an overlapping activity is a separate one
    private Boolean allowOverlap;

    // Calories burned, from a watch or a gym machine (empty = use the estimate)
    @Min(value = 1, message = "Calories burned must be between 1 and 5,000 kcal")
    @Max(value = 5000, message = "Calories burned must be between 1 and 5,000 kcal")
    private Integer enteredKcal;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getActivityCode() { return activityCode; }
    public void setActivityCode(String activityCode) { this.activityCode = activityCode; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public Boolean getAllowOverlap() { return allowOverlap; }
    public void setAllowOverlap(Boolean allowOverlap) { this.allowOverlap = allowOverlap; }

    public Integer getEnteredKcal() { return enteredKcal; }
    public void setEnteredKcal(Integer enteredKcal) { this.enteredKcal = enteredKcal; }
}
