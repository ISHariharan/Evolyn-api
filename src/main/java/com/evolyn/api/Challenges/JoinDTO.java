package com.evolyn.api.Challenges;

import jakarta.validation.constraints.Pattern;

/** { "team": "A" } for team challenges; empty otherwise. */
public class JoinDTO {

    @Pattern(regexp = "[AaBb]", message = "Pick team A or team B")
    private String team;

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
}
