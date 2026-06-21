package com.ems.importerservice.dto;

import com.ems.importerservice.domain.EventSource;
import java.util.List;

public record ImportRunResponse(
    EventSource source,
    int imported,
    int skipped,
    int failed,
    List<ImportedEventResponse> records
) {
}
