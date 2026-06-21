package com.ems.importerservice.exception;

import com.ems.importerservice.domain.EventSource;

public class UnsupportedEventSourceException extends RuntimeException {
    public UnsupportedEventSourceException(EventSource source) {
        super("Event source '" + source + "' is not supported");
    }
}
