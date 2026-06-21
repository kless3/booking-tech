package com.ems.importerservice.exception;

import java.util.UUID;

public class ImportedEventNotFoundException extends RuntimeException {
    public ImportedEventNotFoundException(UUID id) {
        super("Imported event with id '" + id + "' was not found");
    }
}
