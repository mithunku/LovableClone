package com.codingShuttle.project.Lovable.dto.subscription;

public record PlanResponse(
        Long id,
        String name,
        //this price id wiil be shared to stripe when we buy a plan
        Integer maxProjects,//max projects that you can create
        Integer maxTokensPerDay,
        Integer maxPreviews,//Max previews that are available since preview involve running code and it utilizes the resources so we cannot be free
        Boolean unlimitedAi,
        String price
) {
}
