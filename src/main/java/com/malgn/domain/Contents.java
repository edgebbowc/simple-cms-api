package com.malgn.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long viewCount = 0L;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false, length = 10)
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Column(length = 10)
    private String lastModifiedBy;

    @Builder
    public Contents(String title, String description, String createdBy, String lastModifiedBy) {
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public void update(String title, String description, String modifiedBy) {
        this.title = title;
        this.description = description;
        this.lastModifiedBy = modifiedBy;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
