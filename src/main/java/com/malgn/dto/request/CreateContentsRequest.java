package com.malgn.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateContentsRequest(
        @NotBlank @Size(max = 10) String title,
        String description
) {}
