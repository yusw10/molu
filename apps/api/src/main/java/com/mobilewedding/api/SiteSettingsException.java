package com.mobilewedding.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SiteSettingsException extends ResponseStatusException {
    public SiteSettingsException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
