package com.ems.importerservice.api;

import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.dto.ImportRequest;
import com.ems.importerservice.dto.ImportRunResponse;
import com.ems.importerservice.dto.ImportedEventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "Imports", description = "External event import endpoints")
public interface ImportApi {
    @Operation(summary = "Import events from an external source")
    @ApiResponse(responseCode = "200", description = "Import completed")
    @ApiResponse(responseCode = "400", description = "Unsupported source or invalid request")
    ImportRunResponse importEvents(
        @Parameter(description = "External source") EventSource source,
        @Valid ImportRequest request
    );

    @Operation(summary = "Get imported events by source")
    @ApiResponse(responseCode = "200", description = "Imported events found")
    List<ImportedEventResponse> getImportedEvents(
        @Parameter(description = "External source") EventSource source
    );

    @Operation(summary = "Get imported event details")
    @ApiResponse(responseCode = "200", description = "Imported event found")
    @ApiResponse(responseCode = "404", description = "Imported event not found")
    ImportedEventResponse getImportedEvent(
        @Parameter(description = "Imported event id") UUID id
    );
}
