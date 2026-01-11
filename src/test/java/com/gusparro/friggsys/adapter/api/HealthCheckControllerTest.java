package com.gusparro.friggsys.adapter.api;

import com.gusparro.friggsys.adapter.api.response.HealthCheckResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.TimeGauge;
import io.micrometer.core.instrument.search.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HealthCheckController Tests")
class HealthCheckControllerTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Search search;

    @Mock
    private TimeGauge timeGauge;

    @InjectMocks
    private HealthCheckController controller;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(controller, "applicationName", "friggsys");
        ReflectionTestUtils.setField(controller, "version", "1.0.0");
        ReflectionTestUtils.setField(controller, "java", "21");
        ReflectionTestUtils.setField(controller, "environment", "test");
    }

    @Test
    @DisplayName("Should return OK status when health check is called")
    void shouldReturnOkStatusWhenHealthCheckIsCalled() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return health check response with correct application name")
    void shouldReturnHealthCheckResponseWithCorrectApplicationName() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("friggsys", response.getBody().applicationName());
    }

    @Test
    @DisplayName("Should return health check response with correct version")
    void shouldReturnHealthCheckResponseWithCorrectVersion() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("1.0.0", response.getBody().version());
    }

    @Test
    @DisplayName("Should return health check response with correct Java version")
    void shouldReturnHealthCheckResponseWithCorrectJavaVersion() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("21", response.getBody().java());
    }

    @Test
    @DisplayName("Should return health check response with correct environment")
    void shouldReturnHealthCheckResponseWithCorrectEnvironment() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("test", response.getBody().environment());
    }

    @Test
    @DisplayName("Should return health check response with all fields populated")
    void shouldReturnHealthCheckResponseWithAllFieldsPopulated() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("friggsys", response.getBody().applicationName());
        assertEquals("1.0.0", response.getBody().version());
        assertEquals("21", response.getBody().java());
        assertEquals("test", response.getBody().environment());
        assertEquals("00:00:00", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should return formatted uptime when meter registry has uptime gauge")
    void shouldReturnFormattedUptimeWhenMeterRegistryHasUptimeGauge() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(3661.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("01:01:01", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should return zero uptime when time gauge is null")
    void shouldReturnZeroUptimeWhenTimeGaugeIsNull() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("00:00:00", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime correctly for less than one minute")
    void shouldFormatUptimeCorrectlyForLessThanOneMinute() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(45.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("00:00:45", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime correctly for exactly one minute")
    void shouldFormatUptimeCorrectlyForExactlyOneMinute() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(60.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("00:01:00", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime correctly for exactly one hour")
    void shouldFormatUptimeCorrectlyForExactlyOneHour() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(3600.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("01:00:00", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime correctly for multiple hours")
    void shouldFormatUptimeCorrectlyForMultipleHours() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(7325.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("02:02:05", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime correctly for typical application uptime")
    void shouldFormatUptimeCorrectlyForTypicalApplicationUptime() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(86400.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("24:00:00", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime with leading zeros for single digit values")
    void shouldFormatUptimeWithLeadingZerosForSingleDigitValues() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(3665.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("01:01:05", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime correctly for maximum values per component")
    void shouldFormatUptimeCorrectlyForMaximumValuesPerComponent() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(359999.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("99:59:59", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should call meter registry to find process uptime")
    void shouldCallMeterRegistryToFindProcessUptime() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        controller.healthCheck();

        verify(meterRegistry, times(1)).find("process.uptime");
    }

    @Test
    @DisplayName("Should call time gauge to get uptime value when available")
    void shouldCallTimeGaugeToGetUptimeValueWhenAvailable() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(100.0);

        controller.healthCheck();

        verify(timeGauge, times(1)).value(TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Should return response entity with non-null body")
    void shouldReturnResponseEntityWithNonNullBody() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response);
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should work with different application configurations")
    void shouldWorkWithDifferentApplicationConfigurations() {
        ReflectionTestUtils.setField(controller, "applicationName", "my-service");
        ReflectionTestUtils.setField(controller, "version", "2.5.3");
        ReflectionTestUtils.setField(controller, "java", "17");
        ReflectionTestUtils.setField(controller, "environment", "production");

        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(1234.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("my-service", response.getBody().applicationName());
        assertEquals("2.5.3", response.getBody().version());
        assertEquals("17", response.getBody().java());
        assertEquals("production", response.getBody().environment());
        assertEquals("00:20:34", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should handle zero uptime value")
    void shouldHandleZeroUptimeValue() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(0.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("00:00:00", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format uptime for very large hour values")
    void shouldFormatUptimeForVeryLargeHourValues() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(3723661.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("1034:21:01", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should return health check response of correct type")
    void shouldReturnHealthCheckResponseOfCorrectType() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(null);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertInstanceOf(HealthCheckResponse.class, response.getBody());
    }

    @Test
    @DisplayName("Should handle typical startup scenario with minimal uptime")
    void shouldHandleTypicalStartupScenarioWithMinimalUptime() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(5.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("00:00:05", response.getBody().uptime());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should format seconds correctly for edge case of 59 seconds")
    void shouldFormatSecondsCorrectlyForEdgeCaseOf59Seconds() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(59.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("00:00:59", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should format minutes correctly for edge case of 59 minutes")
    void shouldFormatMinutesCorrectlyForEdgeCaseOf59Minutes() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(3599.0);

        var response = controller.healthCheck();

        assertNotNull(response.getBody());
        assertEquals("00:59:59", response.getBody().uptime());
    }

    @Test
    @DisplayName("Should maintain consistency across multiple health check calls")
    void shouldMaintainConsistencyAcrossMultipleHealthCheckCalls() {
        when(meterRegistry.find("process.uptime")).thenReturn(search);
        when(search.timeGauge()).thenReturn(timeGauge);
        when(timeGauge.value(TimeUnit.SECONDS)).thenReturn(1000.0);

        var response1 = controller.healthCheck();
        var response2 = controller.healthCheck();

        assertNotNull(response1.getBody());
        assertEquals(response1.getBody().applicationName(), response2.getBody().applicationName());
        assertEquals(response1.getBody().version(), response2.getBody().version());
        assertEquals(response1.getBody().java(), response2.getBody().java());
        assertEquals(response1.getBody().environment(), response2.getBody().environment());
    }

}