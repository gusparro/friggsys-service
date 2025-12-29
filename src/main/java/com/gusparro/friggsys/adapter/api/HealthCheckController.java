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

import static org.apache.catalina.manager.StatusTransformer.formatSeconds;

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
        var formattedUptime = getFormattedUptime();
        var response = new HealthCheckResponse(applicationName, version, java, environment, formattedUptime);

        return ResponseEntity.ok(response);
    }

    private String getFormattedUptime() {
        var uptime = meterRegistry.find("process.uptime").timeGauge();
        var uptimeSeconds = uptime != null ? uptime.value(TimeUnit.SECONDS) : 0;

        return formatSeconds((long) uptimeSeconds);
    }

}
