package com.evolyn.api.Categories;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** What the browser sends to create or edit a category. */
public class CategoryDTO {

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name can be at most 50 characters")
    private String name;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "EXPENSE|INCOME", message = "Type must be EXPENSE or INCOME")
    private String type;

    // A Boxicons class like "bx bx-restaurant": letters, digits and dashes only
    @NotBlank(message = "Icon is required")
    @Pattern(regexp = "^bx[sl]? bx[sl]?-[a-z0-9-]{1,40}$", message = "Icon must be a Boxicons class, e.g. bx bx-restaurant")
    private String icon;

    @NotBlank(message = "Colour is required")
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Colour must look like #f97316")
    private String color;

    @Size(max = 120, message = "Description can be at most 120 characters")
    private String description;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
