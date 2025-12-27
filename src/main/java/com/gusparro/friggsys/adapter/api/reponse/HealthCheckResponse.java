package com.gusparro.friggsys.adapter.api.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HealthCheckResponse(
        @JsonProperty("application_name")
        String applicationName,

        @JsonProperty("application_version")
        String version,

        @JsonProperty("java_version")
        String java,

        @JsonProperty("environment")
        String environment,

        @JsonProperty("uptime")
        String uptime
) {}
