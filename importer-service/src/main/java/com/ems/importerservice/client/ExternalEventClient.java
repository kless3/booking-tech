package com.ems.importerservice.client;

import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.dto.ExternalEvent;
import java.util.List;

public interface ExternalEventClient {
    EventSource source();

    List<ExternalEvent> fetchEvents(int limit);
}
