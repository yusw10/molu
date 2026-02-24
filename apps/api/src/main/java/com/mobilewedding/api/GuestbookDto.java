package com.mobilewedding.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class GuestbookDto {

    public record CreateRequest(
            @NotBlank @Size(max = 40) String name,
            @NotBlank @Size(max = 400) String message,
            boolean attending
    ) {}

    public record Response(
            UUID id,
            String name,
            String message,
            boolean attending,
            OffsetDateTime createdAt
    ) {}
}
