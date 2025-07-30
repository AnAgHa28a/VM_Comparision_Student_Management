package com.example.demo;

import io.micrometer.azuremonitor.AzureMonitorConfig;
import io.micrometer.azuremonitor.AzureMonitorMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

	private static final String INSTRUMENTATION_KEY = "32030477-8d9b-46bf-8f2d-f73e42938aa1";

	@Bean
	public MeterRegistry azureMeterRegistry() {
		AzureMonitorConfig azureMonitorConfig = new AzureMonitorConfig() {
			@Override
			public String instrumentationKey() {
				return INSTRUMENTATION_KEY;
			}

			@Override
			public String get(String key) {
				return null;
			}
		};
		return new AzureMonitorMeterRegistry(azureMonitorConfig, Clock.SYSTEM);
	}
}

