package com.evolyn.api.Weight;

import jakarta.validation.constraints.NotNull;

/** { "shareWeightChange": true } : let your tribes see how your weight changes (never the weight itself) */
public class WeightSharingDTO {

    @NotNull(message = "Say whether to share your weight change")
    private Boolean shareWeightChange;

    public Boolean getShareWeightChange() { return shareWeightChange; }
    public void setShareWeightChange(Boolean shareWeightChange) { this.shareWeightChange = shareWeightChange; }
}
