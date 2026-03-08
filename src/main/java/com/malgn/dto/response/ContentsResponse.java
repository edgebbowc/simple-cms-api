package com.malgn.dto.response;

import com.malgn.domain.Contents;

import java.time.LocalDateTime;

public record ContentsResponse(
        Long id,
        String title,
        String description,
        Long viewCount,
        String createdBy,
        LocalDateTime createdDate,
        String lastModifiedBy,
        LocalDateTime lastModifiedDate
) {
    public static ContentsResponse from(Contents c) {
        return new ContentsResponse(
                c.getId(), c.getTitle(), c.getDescription(),
                c.getViewCount(), c.getCreatedBy(), c.getCreatedDate(),
                c.getLastModifiedBy(), c.getLastModifiedDate()
        );
    }
}
