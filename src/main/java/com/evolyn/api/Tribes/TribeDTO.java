package com.evolyn.api.Tribes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Create or edit a tribe: { "name": "Handball Crew", "description": "..." } */
public class TribeDTO {

    @NotBlank(message = "Give the tribe a name")
    @Size(min = 2, max = 60, message = "Name must be 2 to 60 characters")
    private String name;

    @Size(max = 300, message = "Description can be at most 300 characters")
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
