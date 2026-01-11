package com.gusparro.friggsys.adapter.api;

import com.gusparro.friggsys.adapter.api.response.HealthCheckResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor

@RestController
@RequestMapping("${api.prefix}/health")
@Tag(name = "Health Check", description = "Application health and status monitoring")
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

    @Operation(
            summary = "Check application health",
            description = "Returns application status including name, version, Java version, active environment, and uptime"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Application is running",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HealthCheckResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Erro interno",
                                    value = """
                                            {
                                              "timestamp": "2026-01-11T10:30:00",
                                              "status": 500,
                                              "error": "Internal Server Error",
                                              "message": "Error retrieving uptime information",
                                              "path": "/api/v1/health"
                                            }
                                            """
                            )
                    )
            )
    })
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
