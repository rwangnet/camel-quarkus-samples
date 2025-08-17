package com.example.hotelscheduler.infrastructure.observability;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ObservabilityConfig {
	@Bean
	MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(Environment env) {
		return registry -> {
			String appName = env.getProperty("spring.application.name", "hotel-scheduler");
			registry.config().commonTags("service", appName);
		};
	}
}