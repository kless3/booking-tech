package com.ems.importerservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ImportRequest(
    @NotNull
    UUID organizerUserId,

    @Min(1)
    @Max(100)
    Integer limit
) {
    public int normalizedLimit() {
        return limit == null ? 20 : limit;
    }
}
