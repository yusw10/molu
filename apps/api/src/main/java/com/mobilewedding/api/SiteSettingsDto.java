package com.mobilewedding.api;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;

public class SiteSettingsDto {

    public record PublicResponse(JsonNode settings, boolean adminConfigured) {
    }

    public record VerifyRequest(@NotBlank String password) {
    }

    public record VerifyResponse(boolean success, boolean adminConfigured) {
    }

    public record SetupRequest(@NotBlank String password) {
    }

    public record SaveRequest(@NotBlank String password, JsonNode settings) {
    }
}
