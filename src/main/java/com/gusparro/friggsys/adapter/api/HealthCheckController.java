package com.gusparro.friggsys.adapter.api;

import com.gusparro.friggsys.adapter.api.reponse.HealthCheckResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping("${api.prefix}/health")
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

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
        var response = new HealthCheckResponse(applicationName, version, java, environment);

        return ResponseEntity.ok(response);
    }

}
