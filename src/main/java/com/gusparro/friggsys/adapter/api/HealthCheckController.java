package com.gusparro.friggsys.adapter.api;

import com.gusparro.friggsys.adapter.api.response.HealthCheckResponse;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor

@RestController
@RequestMapping("${api.prefix}/health")
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    private final MeterRegistry meterRegistry;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.application.version}")
    private String version;

    @Value("${spring.application.java}")
    private String java;

    @Value("${spring.profiles.active}")
    private String environment;

    @GetMapping
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        logger.info("Health check requested");

        var formattedUptime = getFormattedUptime();
        var response = new HealthCheckResponse(applicationName, version, java, environment, formattedUptime);

        logger.debug("Health check response: application={}, version={}, uptime={}",
                applicationName, version, formattedUptime);

        return ResponseEntity.ok(response);
    }

    private String getFormattedUptime() {
        var uptime = meterRegistry.find("process.uptime").timeGauge();
        var uptimeSeconds = uptime != null ? uptime.value(TimeUnit.SECONDS) : 0;

        return formatSeconds((long) uptimeSeconds);
    }

    private String formatSeconds(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
