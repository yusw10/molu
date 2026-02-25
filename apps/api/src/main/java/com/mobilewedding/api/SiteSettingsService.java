package com.mobilewedding.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class SiteSettingsService {

    private static final int SETTINGS_ID = 1;

    private final SiteSettingsRepository repository;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SiteSettingsService(SiteSettingsRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public SiteSettingsDto.PublicResponse getPublicSettings() {
        SiteSettingsEntity entity = getOrCreate();
        return new SiteSettingsDto.PublicResponse(parsePayload(entity.getPayload()), hasPassword(entity));
    }

    public SiteSettingsDto.VerifyResponse verify(String password) {
        SiteSettingsEntity entity = getOrCreate();
        boolean configured = hasPassword(entity);
        boolean success = configured && passwordEncoder.matches(password, entity.getAdminPasswordHash());
        return new SiteSettingsDto.VerifyResponse(success, configured);
    }

    public SiteSettingsDto.VerifyResponse setupPassword(String password) {
        SiteSettingsEntity entity = getOrCreate();
        if (!hasPassword(entity)) {
            entity.setAdminPasswordHash(passwordEncoder.encode(password));
            entity.setUpdatedAt(OffsetDateTime.now());
            repository.save(entity);
        }
        return new SiteSettingsDto.VerifyResponse(true, true);
    }

    public SiteSettingsDto.PublicResponse saveSettings(String password, JsonNode settings) {
        SiteSettingsEntity entity = getOrCreate();
        if (!hasPassword(entity) || !passwordEncoder.matches(password, entity.getAdminPasswordHash())) {
            throw new IllegalArgumentException("Invalid admin password");
        }

        JsonNode safeNode = settings == null ? objectMapper.createObjectNode() : settings;
        entity.setPayload(safeNode.toString());
        entity.setUpdatedAt(OffsetDateTime.now());
        repository.save(entity);

        return new SiteSettingsDto.PublicResponse(parsePayload(entity.getPayload()), true);
    }

    private SiteSettingsEntity getOrCreate() {
        return repository.findById(SETTINGS_ID).orElseGet(() -> {
            SiteSettingsEntity entity = new SiteSettingsEntity();
            entity.setId(SETTINGS_ID);
            entity.setPayload("{}");
            entity.setUpdatedAt(OffsetDateTime.now());
            return repository.save(entity);
        });
    }

    private boolean hasPassword(SiteSettingsEntity entity) {
        return entity.getAdminPasswordHash() != null && !entity.getAdminPasswordHash().isBlank();
    }

    private JsonNode parsePayload(String payload) {
        try {
            return objectMapper.readTree(payload == null || payload.isBlank() ? "{}" : payload);
        } catch (Exception e) {
            return objectMapper.createObjectNode();
        }
    }
}
