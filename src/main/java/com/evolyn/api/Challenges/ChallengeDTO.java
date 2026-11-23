package com.evolyn.api.Challenges;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Create or edit a challenge. target is kcal (COLLECTIVE, INDIVIDUAL_TARGET) or
 * days (CONSISTENCY); COMPETITIVE and TEAM have none. The Data Service checks
 * the rules that depend on the type and the dates.
 */
public class ChallengeDTO {

    @NotBlank(message = "Give the challenge a name")
    @Size(min = 3, max = 80, message = "Name must be 3 to 80 characters")
    private String name;

    @Size(max = 500, message = "Description can be at most 500 characters")
    private String description;

    @NotBlank(message = "Choose a challenge type")
    @Pattern(regexp = "COMPETITIVE|COLLECTIVE|INDIVIDUAL_TARGET|CONSISTENCY|TEAM", message = "Unknown challenge type")
    private String type;

    @NotNull(message = "Choose the start date")
    private LocalDate startDate;

    @NotNull(message = "Choose the end date")
    private LocalDate endDate;

    @DecimalMin(value = "1", message = "Target must be at least 1")
    @DecimalMax(value = "10000000", message = "Target is too large")
    private BigDecimal target;

    @Min(value = 1, message = "Minimum length must be 1 to 240 minutes")
    @Max(value = 240, message = "Minimum length must be 1 to 240 minutes")
    private Integer minMinutes;

    @Pattern(regexp = "\\d{2}", message = "Unknown activity type")
    private String headingCode;

    private Boolean publish;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getTarget() { return target; }
    public void setTarget(BigDecimal target) { this.target = target; }
    public Integer getMinMinutes() { return minMinutes; }
    public void setMinMinutes(Integer minMinutes) { this.minMinutes = minMinutes; }
    public String getHeadingCode() { return headingCode; }
    public void setHeadingCode(String headingCode) { this.headingCode = headingCode; }
    public Boolean getPublish() { return publish; }
    public void setPublish(Boolean publish) { this.publish = publish; }
}
