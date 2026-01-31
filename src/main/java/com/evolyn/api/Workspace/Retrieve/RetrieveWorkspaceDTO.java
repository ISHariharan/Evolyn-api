package com.evolyn.api.Workspace.Retrieve;

import jakarta.validation.constraints.NotEmpty;

public class RetrieveWorkspaceDTO {
    @NotEmpty(message = "UserId is empty")
    private String userId;

    public String getUserId(){
        return userId;
    }

    public void setString(String userId){
        this.userId = userId;
    }
}
