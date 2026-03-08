package com.malgn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 생성일자, 수정일자
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
