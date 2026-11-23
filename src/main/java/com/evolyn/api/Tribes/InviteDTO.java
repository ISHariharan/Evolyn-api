package com.evolyn.api.Tribes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** Invite someone by Profile ID: { "profileId": "arun_k" } */
public class InviteDTO {

    @NotBlank(message = "Enter a Profile ID")
    @Pattern(regexp = "@?[A-Za-z0-9_]{3,20}", message = "Profile ID: 3 to 20 letters, numbers or _")
    private String profileId;

    public String getProfileId() { return profileId; }
    public void setProfileId(String profileId) { this.profileId = profileId; }
}
