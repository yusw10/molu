package com.mobilewedding.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/site-settings")
@CrossOrigin(origins = "*")
public class SiteSettingsController {

    private final SiteSettingsService service;

    public SiteSettingsController(SiteSettingsService service) {
        this.service = service;
    }

    @GetMapping
    public SiteSettingsDto.PublicResponse getPublicSettings() {
        return service.getPublicSettings();
    }

    @PostMapping("/admin/verify")
    public SiteSettingsDto.VerifyResponse verify(@Valid @RequestBody SiteSettingsDto.VerifyRequest request) {
        return service.verify(request.password());
    }

    @PostMapping("/admin/setup")
    @ResponseStatus(HttpStatus.CREATED)
    public SiteSettingsDto.VerifyResponse setup(@Valid @RequestBody SiteSettingsDto.SetupRequest request) {
        return service.setupPassword(request.password());
    }

    @PutMapping("/admin")
    public SiteSettingsDto.PublicResponse save(@Valid @RequestBody SiteSettingsDto.SaveRequest request) {
        try {
            return service.saveSettings(request.password(), request.settings());
        } catch (IllegalArgumentException e) {
            throw new SiteSettingsException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
