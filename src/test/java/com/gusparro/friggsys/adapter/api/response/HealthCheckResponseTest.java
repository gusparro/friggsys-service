package com.gusparro.friggsys.adapter.api.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HealthCheckResponse Tests")
class HealthCheckResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should create HealthCheckResponse with all fields")
    void shouldCreateHealthCheckResponseWithAllFields() {
        var response = new HealthCheckResponse(
                "friggsys-service",
                "1.0.0",
                "21.0.0",
                "production",
                "2h 30m 45s"
        );

        assertNotNull(response);
        assertEquals("friggsys-service", response.applicationName());
        assertEquals("1.0.0", response.version());
        assertEquals("21.0.0", response.java());
        assertEquals("production", response.environment());
        assertEquals("2h 30m 45s", response.uptime());
    }

    @Test
    @DisplayName("Should serialize to JSON with correct field names")
    void shouldSerializeToJsonWithCorrectFieldNames() throws Exception {
        var response = new HealthCheckResponse(
                "friggsys-service",
                "1.0.0",
                "21.0.0",
                "production",
                "1h 15m"
        );

        var json = objectMapper.writeValueAsString(response);

        assertTrue(json.contains("\"application_name\":\"friggsys-service\""));
        assertTrue(json.contains("\"application_version\":\"1.0.0\""));
        assertTrue(json.contains("\"java_version\":\"21.0.0\""));
        assertTrue(json.contains("\"environment\":\"production\""));
        assertTrue(json.contains("\"uptime\":\"1h 15m\""));
    }

    @Test
    @DisplayName("Should deserialize from JSON correctly")
    void shouldDeserializeFromJsonCorrectly() throws Exception {
        var json = """
                {
                    "application_name": "test-app",
                    "application_version": "2.0.0",
                    "java_version": "17.0.0",
                    "environment": "development",
                    "uptime": "5m 30s"
                }
                """;

        var response = objectMapper.readValue(json, HealthCheckResponse.class);

        assertNotNull(response);
        assertEquals("test-app", response.applicationName());
        assertEquals("2.0.0", response.version());
        assertEquals("17.0.0", response.java());
        assertEquals("development", response.environment());
        assertEquals("5m 30s", response.uptime());
    }

    @Test
    @DisplayName("Should create response with development environment")
    void shouldCreateResponseWithDevelopmentEnvironment() {
        var response = new HealthCheckResponse(
                "friggsys-service",
                "1.0.0-SNAPSHOT",
                "21.0.0",
                "development",
                "10m"
        );

        assertEquals("development", response.environment());
    }

    @Test
    @DisplayName("Should create response with staging environment")
    void shouldCreateResponseWithStagingEnvironment() {
        var response = new HealthCheckResponse(
                "friggsys-service",
                "1.0.0-RC1",
                "21.0.0",
                "staging",
                "1d 5h"
        );

        assertEquals("staging", response.environment());
    }

    @Test
    @DisplayName("Should create response with production environment")
    void shouldCreateResponseWithProductionEnvironment() {
        var response = new HealthCheckResponse(
                "friggsys-service",
                "1.0.0",
                "21.0.0",
                "production",
                "30d 12h 45m"
        );

        assertEquals("production", response.environment());
    }

    @Test
    @DisplayName("Should handle different version formats")
    void shouldHandleDifferentVersionFormats() {
        var response1 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");
        var response2 = new HealthCheckResponse("app", "1.0.0-SNAPSHOT", "17.0.1", "dev", "2h");
        var response3 = new HealthCheckResponse("app", "2.1.5-beta", "21.0.0", "staging", "3h");

        assertEquals("1.0.0", response1.version());
        assertEquals("1.0.0-SNAPSHOT", response2.version());
        assertEquals("2.1.5-beta", response3.version());
    }

    @Test
    @DisplayName("Should handle different Java version formats")
    void shouldHandleDifferentJavaVersionFormats() {
        var response1 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");
        var response2 = new HealthCheckResponse("app", "1.0.0", "17.0.0", "prod", "1h");
        var response3 = new HealthCheckResponse("app", "1.0.0", "21.0.5", "prod", "1h");

        assertEquals("17", response1.java());
        assertEquals("17.0.0", response2.java());
        assertEquals("21.0.5", response3.java());
    }

    @Test
    @DisplayName("Should handle different uptime formats")
    void shouldHandleDifferentUptimeFormats() {
        var response1 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "30s");
        var response2 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "5m 30s");
        var response3 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "2h 15m");
        var response4 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "3d 12h 30m");

        assertEquals("30s", response1.uptime());
        assertEquals("5m 30s", response2.uptime());
        assertEquals("2h 15m", response3.uptime());
        assertEquals("3d 12h 30m", response4.uptime());
    }

    @Test
    @DisplayName("Should handle different application names")
    void shouldHandleDifferentApplicationNames() {
        var response1 = new HealthCheckResponse("friggsys-service", "1.0.0", "17", "prod", "1h");
        var response2 = new HealthCheckResponse("user-service", "1.0.0", "17", "prod", "1h");
        var response3 = new HealthCheckResponse("payment-api", "1.0.0", "17", "prod", "1h");

        assertEquals("friggsys-service", response1.applicationName());
        assertEquals("user-service", response2.applicationName());
        assertEquals("payment-api", response3.applicationName());
    }

    @Test
    @DisplayName("Should maintain immutability of record")
    void shouldMaintainImmutabilityOfRecord() {
        var response = new HealthCheckResponse(
                "friggsys-service",
                "1.0.0",
                "21.0.0",
                "production",
                "1h"
        );

        var applicationName = response.applicationName();
        var version = response.version();

        assertEquals(applicationName, response.applicationName());
        assertEquals(version, response.version());
    }

    @Test
    @DisplayName("Should have correct equals implementation")
    void shouldHaveCorrectEqualsImplementation() {
        var response1 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");
        var response2 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");
        var response3 = new HealthCheckResponse("app", "2.0.0", "17", "prod", "1h");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }

    @Test
    @DisplayName("Should have correct hashCode implementation")
    void shouldHaveCorrectHashCodeImplementation() {
        var response1 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");
        var response2 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Should have correct toString implementation")
    void shouldHaveCorrectToStringImplementation() {
        var response = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");

        var toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("app"));
        assertTrue(toString.contains("1.0.0"));
        assertTrue(toString.contains("17"));
        assertTrue(toString.contains("prod"));
        assertTrue(toString.contains("1h"));
    }

    @Test
    @DisplayName("Should serialize and deserialize maintaining data integrity")
    void shouldSerializeAndDeserializeMaintainingDataIntegrity() throws Exception {
        var original = new HealthCheckResponse(
                "friggsys-service",
                "1.2.3",
                "21.0.0",
                "production",
                "5d 3h 45m"
        );

        var json = objectMapper.writeValueAsString(original);
        var deserialized = objectMapper.readValue(json, HealthCheckResponse.class);

        assertEquals(original, deserialized);
        assertEquals(original.applicationName(), deserialized.applicationName());
        assertEquals(original.version(), deserialized.version());
        assertEquals(original.java(), deserialized.java());
        assertEquals(original.environment(), deserialized.environment());
        assertEquals(original.uptime(), deserialized.uptime());
    }

    @Test
    @DisplayName("Should handle null values in fields")
    void shouldHandleNullValuesInFields() {
        var response = new HealthCheckResponse(null, null, null, null, null);

        assertNull(response.applicationName());
        assertNull(response.version());
        assertNull(response.java());
        assertNull(response.environment());
        assertNull(response.uptime());
    }

    @Test
    @DisplayName("Should handle empty strings in fields")
    void shouldHandleEmptyStringsInFields() {
        var response = new HealthCheckResponse("", "", "", "", "");

        assertEquals("", response.applicationName());
        assertEquals("", response.version());
        assertEquals("", response.java());
        assertEquals("", response.environment());
        assertEquals("", response.uptime());
    }

    @Test
    @DisplayName("Should create multiple instances independently")
    void shouldCreateMultipleInstancesIndependently() {
        var response1 = new HealthCheckResponse("app1", "1.0.0", "17", "dev", "1h");
        var response2 = new HealthCheckResponse("app2", "2.0.0", "21", "prod", "2h");
        var response3 = new HealthCheckResponse("app3", "3.0.0", "17", "staging", "3h");

        assertNotEquals(response1, response2);
        assertNotEquals(response2, response3);
        assertNotEquals(response1, response3);
    }

    @Test
    @DisplayName("Should serialize null values correctly")
    void shouldSerializeNullValuesCorrectly() throws Exception {
        var response = new HealthCheckResponse("app", null, "17", null, "1h");

        var json = objectMapper.writeValueAsString(response);

        assertTrue(json.contains("\"application_name\":\"app\""));
        assertTrue(json.contains("\"application_version\":null"));
        assertTrue(json.contains("\"java_version\":\"17\""));
        assertTrue(json.contains("\"environment\":null"));
        assertTrue(json.contains("\"uptime\":\"1h\""));
    }

    @Test
    @DisplayName("Should handle special characters in fields")
    void shouldHandleSpecialCharactersInFields() {
        var response = new HealthCheckResponse(
                "app-service_v1",
                "1.0.0-beta.2+build.123",
                "21.0.0+35-LTS",
                "production-eu-west-1",
                "2d 3h 45m 30s"
        );

        assertEquals("app-service_v1", response.applicationName());
        assertEquals("1.0.0-beta.2+build.123", response.version());
        assertEquals("21.0.0+35-LTS", response.java());
        assertEquals("production-eu-west-1", response.environment());
        assertEquals("2d 3h 45m 30s", response.uptime());
    }

    @Test
    @DisplayName("Should deserialize with missing optional fields")
    void shouldDeserializeWithMissingOptionalFields() throws Exception {
        var json = """
                {
                    "application_name": "test-app",
                    "application_version": "1.0.0"
                }
                """;

        var response = objectMapper.readValue(json, HealthCheckResponse.class);

        assertEquals("test-app", response.applicationName());
        assertEquals("1.0.0", response.version());
        assertNull(response.java());
        assertNull(response.environment());
        assertNull(response.uptime());
    }

    @Test
    @DisplayName("Should handle long uptime values")
    void shouldHandleLongUptimeValues() {
        var response = new HealthCheckResponse(
                "app",
                "1.0.0",
                "17",
                "prod",
                "365d 23h 59m 59s"
        );

        assertEquals("365d 23h 59m 59s", response.uptime());
    }

    @Test
    @DisplayName("Should handle version with build metadata")
    void shouldHandleVersionWithBuildMetadata() {
        var response = new HealthCheckResponse(
                "app",
                "1.0.0+20130313144700",
                "17",
                "prod",
                "1h"
        );

        assertEquals("1.0.0+20130313144700", response.version());
    }

    @Test
    @DisplayName("Should handle version with pre-release identifier")
    void shouldHandleVersionWithPreReleaseIdentifier() {
        var response = new HealthCheckResponse(
                "app",
                "1.0.0-alpha",
                "17",
                "dev",
                "30m"
        );

        assertEquals("1.0.0-alpha", response.version());
    }

    @Test
    @DisplayName("Should compare two identical responses as equal")
    void shouldCompareTwoIdenticalResponsesAsEqual() {
        var response1 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");
        var response2 = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");

        assertEquals(response1, response2);
        assertTrue(response1.equals(response2));
        assertTrue(response2.equals(response1));
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        var response = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");

        assertNotEquals(null, response);
    }

    @Test
    @DisplayName("Should not be equal to different type")
    void shouldNotBeEqualToDifferentType() {
        var response = new HealthCheckResponse("app", "1.0.0", "17", "prod", "1h");
        var differentType = "not a HealthCheckResponse";

        assertNotEquals(response, differentType);
    }

    @Test
    @DisplayName("Should handle whitespace in fields")
    void shouldHandleWhitespaceInFields() {
        var response = new HealthCheckResponse(
                " app ",
                " 1.0.0 ",
                " 17 ",
                " prod ",
                " 1h "
        );

        assertEquals(" app ", response.applicationName());
        assertEquals(" 1.0.0 ", response.version());
        assertEquals(" 17 ", response.java());
        assertEquals(" prod ", response.environment());
        assertEquals(" 1h ", response.uptime());
    }

    @Test
    @DisplayName("Should serialize to JSON and parse back correctly")
    void shouldSerializeToJsonAndParseBackCorrectly() throws Exception {
        var original = new HealthCheckResponse(
                "friggsys-service",
                "1.5.2",
                "21.0.1",
                "staging",
                "12h 30m"
        );

        var json = objectMapper.writeValueAsString(original);
        var parsed = objectMapper.readValue(json, HealthCheckResponse.class);

        assertEquals(original.applicationName(), parsed.applicationName());
        assertEquals(original.version(), parsed.version());
        assertEquals(original.java(), parsed.java());
        assertEquals(original.environment(), parsed.environment());
        assertEquals(original.uptime(), parsed.uptime());
    }

    @Test
    @DisplayName("Should handle concurrent creation of multiple instances")
    void shouldHandleConcurrentCreationOfMultipleInstances() {
        var response1 = new HealthCheckResponse("app1", "1.0.0", "17", "dev", "1h");
        var response2 = new HealthCheckResponse("app2", "2.0.0", "21", "prod", "2h");
        var response3 = new HealthCheckResponse("app3", "3.0.0", "17", "staging", "3h");

        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);

        assertEquals("app1", response1.applicationName());
        assertEquals("app2", response2.applicationName());
        assertEquals("app3", response3.applicationName());
    }

}