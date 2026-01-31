package com.evolyn.api.Workspace.Creation;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public class WorkspaceCreationDTO {
    @NotNull(message = "UserId is required")
    private UUID userId;

    @NotNull(message = "Workspace details are required")
    @jakarta.validation.Valid
    private Object workspaceDetails;

    public void setUserId(UUID userUuid){
        this.userId = userUuid;
    }

    public UUID getUserId(){
        return userId;
    }

    public void setWorkspaceDetails(Object workspaceDetails){
        this.workspaceDetails = workspaceDetails;
    }
    public Object getWorkspaceDetails(){
        return workspaceDetails;
    }
}
