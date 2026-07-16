package com.codingShuttle.project.Lovable.dto.usage;

public record UsageTodayResponse(
        Integer tokenUsed,
        Integer tokensLimit,
        Integer previewsRunning,//gives previews that we are currently running
        Integer previewsLimit
) {
}
