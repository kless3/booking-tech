package com.ems.importerservice.config;

import com.ems.importerservice.domain.EventSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EventSourceConverter implements Converter<String, EventSource> {
    @Override
    public EventSource convert(String source) {
        try {
            return EventSource.valueOf(source.trim().replace("-", "_").toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Event source '" + source + "' is not supported");
        }
    }
}
