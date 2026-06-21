package com.ems.importerservice.client;

import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.exception.UnsupportedEventSourceException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ExternalEventClientRegistry {
    private final Map<EventSource, ExternalEventClient> clients = new EnumMap<>(EventSource.class);

    public ExternalEventClientRegistry(List<ExternalEventClient> externalEventClients) {
        for (ExternalEventClient client : externalEventClients) {
            clients.put(client.source(), client);
        }
    }

    public ExternalEventClient get(EventSource source) {
        ExternalEventClient client = clients.get(source);
        if (client == null) {
            throw new UnsupportedEventSourceException(source);
        }
        return client;
    }
}
