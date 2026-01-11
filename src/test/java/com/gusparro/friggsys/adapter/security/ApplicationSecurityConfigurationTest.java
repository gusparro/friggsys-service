package com.gusparro.friggsys.adapter.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationSecurityConfiguration Tests")
class ApplicationSecurityConfigurationTest {

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private DefaultSecurityFilterChain defaultSecurityFilterChain;

    @InjectMocks
    private ApplicationSecurityConfiguration securityConfiguration;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(securityConfiguration, "uri", "/api/v1");
    }

    @Test
    @DisplayName("Should create SecurityFilterChain successfully")
    void shouldCreateSecurityFilterChainSuccessfully() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        var result = securityConfiguration.filterChain(httpSecurity);

        assertNotNull(result);
        assertEquals(defaultSecurityFilterChain, result);
    }

    @Test
    @DisplayName("Should disable CSRF protection")
    void shouldDisableCsrfProtection() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        verify(httpSecurity, times(1)).csrf(any());
    }

    @Test
    @DisplayName("Should disable CORS protection")
    void shouldDisableCorsProtection() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        verify(httpSecurity, times(1)).cors(any());
    }

    @Test
    @DisplayName("Should configure authorization to permit all requests")
    void shouldConfigureAuthorizationToPermitAllRequests() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        verify(httpSecurity, times(1)).authorizeHttpRequests(any());
    }

    @Test
    @DisplayName("Should configure headers with frame options disabled")
    void shouldConfigureHeadersWithFrameOptionsDisabled() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        verify(httpSecurity, times(1)).headers(any());
    }

    @Test
    @DisplayName("Should call build on HttpSecurity")
    void shouldCallBuildOnHttpSecurity() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        verify(httpSecurity, times(1)).build();
    }

    @Test
    @DisplayName("Should configure all security settings in correct order")
    void shouldConfigureAllSecuritySettingsInCorrectOrder() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        var inOrder = inOrder(httpSecurity);
        inOrder.verify(httpSecurity).csrf(any());
        inOrder.verify(httpSecurity).cors(any());
        inOrder.verify(httpSecurity).authorizeHttpRequests(any());
        inOrder.verify(httpSecurity).headers(any());
        inOrder.verify(httpSecurity).build();
    }

    @Test
    @DisplayName("Should return SecurityFilterChain from build method")
    void shouldReturnSecurityFilterChainFromBuildMethod() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        var result = securityConfiguration.filterChain(httpSecurity);

        assertInstanceOf(SecurityFilterChain.class, result);
    }

    @Test
    @DisplayName("Should not return null SecurityFilterChain")
    void shouldNotReturnNullSecurityFilterChain() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        var result = securityConfiguration.filterChain(httpSecurity);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Should configure security with permissive settings")
    void shouldConfigureSecurityWithPermissiveSettings() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        verify(httpSecurity).csrf(any());
        verify(httpSecurity).cors(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).headers(any());
    }

    @Test
    @DisplayName("Should handle Exception from HttpSecurity configuration")
    void shouldHandleExceptionFromHttpSecurityConfiguration() throws Exception {
        when(httpSecurity.csrf(any())).thenThrow(new RuntimeException("Configuration error"));

        assertThrows(RuntimeException.class, () -> {
            securityConfiguration.filterChain(httpSecurity);
        });
    }

    @Test
    @DisplayName("Should call each configuration method exactly once")
    void shouldCallEachConfigurationMethodExactlyOnce() throws Exception {
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

        securityConfiguration.filterChain(httpSecurity);

        verify(httpSecurity, times(1)).csrf(any());
        verify(httpSecurity, times(1)).cors(any());
        verify(httpSecurity, times(1)).authorizeHttpRequests(any());
        verify(httpSecurity, times(1)).headers(any());
        verify(httpSecurity, times(1)).build();
    }

    @Test
    @DisplayName("Should configure authorizeHttpRequests to permit all requests")
    void shouldConfigureAuthorizeHttpRequestsToPermitAllRequests() throws Exception {
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRegistry =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer =
                    invocation.getArgument(0);
            customizer.customize(authRegistry);
            return httpSecurity;
        });
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(authRegistry.anyRequest()).thenReturn(authorizedUrl);
        when(authorizedUrl.permitAll()).thenReturn(authRegistry);

        securityConfiguration.filterChain(httpSecurity);

        verify(authRegistry, times(1)).anyRequest();
        verify(authorizedUrl, times(1)).permitAll();
    }

    @Test
    @DisplayName("Should configure headers to disable frame options")
    void shouldConfigureHeadersToDisableFrameOptions() throws Exception {
        HeadersConfigurer<HttpSecurity> headersConfigurer = mock(HeadersConfigurer.class);
        HeadersConfigurer<HttpSecurity>.FrameOptionsConfig frameOptionsConfig =
                mock(HeadersConfigurer.FrameOptionsConfig.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenAnswer(invocation -> {
            Customizer<HeadersConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(headersConfigurer);
            return httpSecurity;
        });
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(headersConfigurer.frameOptions(any())).thenReturn(headersConfigurer);

        securityConfiguration.filterChain(httpSecurity);

        verify(headersConfigurer, times(1)).frameOptions(any());
    }

    @Test
    @DisplayName("Should call anyRequest in authorization configuration")
    void shouldCallAnyRequestInAuthorizationConfiguration() throws Exception {
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRegistry =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer =
                    invocation.getArgument(0);
            customizer.customize(authRegistry);
            return httpSecurity;
        });
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(authRegistry.anyRequest()).thenReturn(authorizedUrl);
        when(authorizedUrl.permitAll()).thenReturn(authRegistry);

        securityConfiguration.filterChain(httpSecurity);

        verify(authRegistry).anyRequest();
    }

    @Test
    @DisplayName("Should call permitAll on anyRequest")
    void shouldCallPermitAllOnAnyRequest() throws Exception {
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRegistry =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer =
                    invocation.getArgument(0);
            customizer.customize(authRegistry);
            return httpSecurity;
        });
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(authRegistry.anyRequest()).thenReturn(authorizedUrl);
        when(authorizedUrl.permitAll()).thenReturn(authRegistry);

        securityConfiguration.filterChain(httpSecurity);

        verify(authorizedUrl).permitAll();
    }

    @Test
    @DisplayName("Should configure frame options in headers")
    void shouldConfigureFrameOptionsInHeaders() throws Exception {
        HeadersConfigurer<HttpSecurity> headersConfigurer = mock(HeadersConfigurer.class);
        HeadersConfigurer<HttpSecurity>.FrameOptionsConfig frameOptionsConfig =
                mock(HeadersConfigurer.FrameOptionsConfig.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenAnswer(invocation -> {
            Customizer<HeadersConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(headersConfigurer);
            return httpSecurity;
        });
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(headersConfigurer.frameOptions(any())).thenAnswer(frameInvocation -> {
            Customizer<HeadersConfigurer<HttpSecurity>.FrameOptionsConfig> frameCustomizer =
                    frameInvocation.getArgument(0);
            frameCustomizer.customize(frameOptionsConfig);
            return headersConfigurer;
        });
        when(frameOptionsConfig.disable()).thenReturn(headersConfigurer);

        securityConfiguration.filterChain(httpSecurity);

        verify(frameOptionsConfig, times(1)).disable();
    }

    @Test
    @DisplayName("Should apply authorization customizer to auth registry")
    void shouldApplyAuthorizationCustomizerToAuthRegistry() throws Exception {
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRegistry =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer =
                    invocation.getArgument(0);
            assertNotNull(customizer);
            customizer.customize(authRegistry);
            return httpSecurity;
        });
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(authRegistry.anyRequest()).thenReturn(authorizedUrl);
        when(authorizedUrl.permitAll()).thenReturn(authRegistry);

        securityConfiguration.filterChain(httpSecurity);

        verify(authRegistry, times(1)).anyRequest();
        verify(authorizedUrl, times(1)).permitAll();
    }

    @Test
    @DisplayName("Should apply headers customizer to headers configurer")
    void shouldApplyHeadersCustomizerToHeadersConfigurer() throws Exception {
        HeadersConfigurer<HttpSecurity> headersConfigurer = mock(HeadersConfigurer.class);
        HeadersConfigurer<HttpSecurity>.FrameOptionsConfig frameOptionsConfig =
                mock(HeadersConfigurer.FrameOptionsConfig.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenAnswer(invocation -> {
            Customizer<HeadersConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            assertNotNull(customizer);
            customizer.customize(headersConfigurer);
            return httpSecurity;
        });
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(headersConfigurer.frameOptions(any())).thenAnswer(frameInvocation -> {
            Customizer<HeadersConfigurer<HttpSecurity>.FrameOptionsConfig> frameCustomizer =
                    frameInvocation.getArgument(0);
            frameCustomizer.customize(frameOptionsConfig);
            return headersConfigurer;
        });
        when(frameOptionsConfig.disable()).thenReturn(headersConfigurer);

        securityConfiguration.filterChain(httpSecurity);

        verify(headersConfigurer, times(1)).frameOptions(any());
        verify(frameOptionsConfig, times(1)).disable();
    }

    @Test
    @DisplayName("Should configure authorization with anyRequest and permitAll in sequence")
    void shouldConfigureAuthorizationWithAnyRequestAndPermitAllInSequence() throws Exception {
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authRegistry =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl =
                mock(AuthorizeHttpRequestsConfigurer.AuthorizedUrl.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenAnswer(invocation -> {
            Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer =
                    invocation.getArgument(0);
            customizer.customize(authRegistry);
            return httpSecurity;
        });
        when(httpSecurity.headers(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(authRegistry.anyRequest()).thenReturn(authorizedUrl);
        when(authorizedUrl.permitAll()).thenReturn(authRegistry);

        securityConfiguration.filterChain(httpSecurity);

        var inOrder = inOrder(authRegistry, authorizedUrl);
        inOrder.verify(authRegistry).anyRequest();
        inOrder.verify(authorizedUrl).permitAll();
    }

    @Test
    @DisplayName("Should configure headers with frameOptions and disable in sequence")
    void shouldConfigureHeadersWithFrameOptionsAndDisableInSequence() throws Exception {
        HeadersConfigurer<HttpSecurity> headersConfigurer = mock(HeadersConfigurer.class);
        HeadersConfigurer<HttpSecurity>.FrameOptionsConfig frameOptionsConfig =
                mock(HeadersConfigurer.FrameOptionsConfig.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.cors(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.headers(any())).thenAnswer(invocation -> {
            Customizer<HeadersConfigurer<HttpSecurity>> customizer = invocation.getArgument(0);
            customizer.customize(headersConfigurer);
            return httpSecurity;
        });
        when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);
        when(headersConfigurer.frameOptions(any())).thenAnswer(frameInvocation -> {
            Customizer<HeadersConfigurer<HttpSecurity>.FrameOptionsConfig> frameCustomizer =
                    frameInvocation.getArgument(0);
            frameCustomizer.customize(frameOptionsConfig);
            return headersConfigurer;
        });
        when(frameOptionsConfig.disable()).thenReturn(headersConfigurer);

        securityConfiguration.filterChain(httpSecurity);

        var inOrder = inOrder(headersConfigurer, frameOptionsConfig);
        inOrder.verify(headersConfigurer).frameOptions(any());
        inOrder.verify(frameOptionsConfig).disable();
    }

    @Test
    @DisplayName("Should have Configuration annotation")
    void shouldHaveConfigurationAnnotation() {
        var annotation = ApplicationSecurityConfiguration.class.getAnnotation(Configuration.class);

        assertNotNull(annotation);
    }

    @Test
    @DisplayName("Should have EnableWebSecurity annotation")
    void shouldHaveEnableWebSecurityAnnotation() {
        var annotation = ApplicationSecurityConfiguration.class.getAnnotation(EnableWebSecurity.class);

        assertNotNull(annotation);
    }

    @Test
    @DisplayName("Should be a public class")
    void shouldBeAPublicClass() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ApplicationSecurityConfiguration.class.getModifiers()));
    }

    @Test
    @DisplayName("Should not be a final class")
    void shouldNotBeAFinalClass() {
        assertFalse(java.lang.reflect.Modifier.isFinal(ApplicationSecurityConfiguration.class.getModifiers()));
    }

    @Test
    @DisplayName("Should have filterChain method")
    void shouldHaveFilterChainMethod() throws NoSuchMethodException {
        var filterChainMethod = ApplicationSecurityConfiguration.class.getDeclaredMethod(
                "filterChain",
                HttpSecurity.class
        );

        assertNotNull(filterChainMethod);
    }

    @Test
    @DisplayName("Should have filterChain method with Bean annotation")
    void shouldHaveFilterChainMethodWithBeanAnnotation() throws NoSuchMethodException {
        var filterChainMethod = ApplicationSecurityConfiguration.class.getDeclaredMethod(
                "filterChain",
                HttpSecurity.class
        );
        var beanAnnotation = filterChainMethod.getAnnotation(org.springframework.context.annotation.Bean.class);

        assertNotNull(beanAnnotation);
    }

    @Test
    @DisplayName("Should have filterChain method returning SecurityFilterChain")
    void shouldHaveFilterChainMethodReturningSecurityFilterChain() throws NoSuchMethodException {
        var filterChainMethod = ApplicationSecurityConfiguration.class.getDeclaredMethod(
                "filterChain",
                HttpSecurity.class
        );

        assertEquals(SecurityFilterChain.class, filterChainMethod.getReturnType());
    }

    @Test
    @DisplayName("Should have filterChain method that is public")
    void shouldHaveFilterChainMethodThatIsPublic() throws NoSuchMethodException {
        var filterChainMethod = ApplicationSecurityConfiguration.class.getDeclaredMethod(
                "filterChain",
                HttpSecurity.class
        );

        assertTrue(java.lang.reflect.Modifier.isPublic(filterChainMethod.getModifiers()));
    }

    @Test
    @DisplayName("Should have filterChain method that throws Exception")
    void shouldHaveFilterChainMethodThatThrowsException() throws NoSuchMethodException {
        var filterChainMethod = ApplicationSecurityConfiguration.class.getDeclaredMethod(
                "filterChain",
                HttpSecurity.class
        );
        var exceptionTypes = filterChainMethod.getExceptionTypes();

        assertEquals(1, exceptionTypes.length);
        assertEquals(Exception.class, exceptionTypes[0]);
    }

    @Test
    @DisplayName("Should have uri field with Value annotation")
    void shouldHaveUriFieldWithValueAnnotation() throws NoSuchFieldException {
        var uriField = ApplicationSecurityConfiguration.class.getDeclaredField("uri");
        var valueAnnotation = uriField.getAnnotation(org.springframework.beans.factory.annotation.Value.class);

        assertNotNull(valueAnnotation);
        assertEquals("${api.prefix}", valueAnnotation.value());
    }

    @Test
    @DisplayName("Should have uri field of type String")
    void shouldHaveUriFieldOfTypeString() throws NoSuchFieldException {
        var uriField = ApplicationSecurityConfiguration.class.getDeclaredField("uri");

        assertEquals(String.class, uriField.getType());
    }

    @Test
    @DisplayName("Should have uri field with private access")
    void shouldHaveUriFieldWithPrivateAccess() throws NoSuchFieldException {
        var uriField = ApplicationSecurityConfiguration.class.getDeclaredField("uri");

        assertTrue(java.lang.reflect.Modifier.isPrivate(uriField.getModifiers()));
    }

    @Test
    @DisplayName("Should be in correct package")
    void shouldBeInCorrectPackage() {
        assertEquals("com.gusparro.friggsys.adapter.security",
                ApplicationSecurityConfiguration.class.getPackage().getName());
    }

    @Test
    @DisplayName("Should have correct class name")
    void shouldHaveCorrectClassName() {
        assertEquals("ApplicationSecurityConfiguration",
                ApplicationSecurityConfiguration.class.getSimpleName());
    }

    @Test
    @DisplayName("Should extend Object class")
    void shouldExtendObjectClass() {
        assertEquals(Object.class, ApplicationSecurityConfiguration.class.getSuperclass());
    }

    @Test
    @DisplayName("Should not implement any interfaces")
    void shouldNotImplementAnyInterfaces() {
        var interfaces = ApplicationSecurityConfiguration.class.getInterfaces();

        assertEquals(0, interfaces.length);
    }

    @Test
    @DisplayName("Should have default constructor")
    void shouldHaveDefaultConstructor() {
        assertDoesNotThrow(() -> new ApplicationSecurityConfiguration());
    }

    @Test
    @DisplayName("Should be instantiable")
    void shouldBeInstantiable() {
        var instance = new ApplicationSecurityConfiguration();

        assertNotNull(instance);
        assertInstanceOf(ApplicationSecurityConfiguration.class, instance);
    }

    @Test
    @DisplayName("Should have only one public method")
    void shouldHaveOnlyOnePublicMethod() {
        var publicMethods = ApplicationSecurityConfiguration.class.getDeclaredMethods();
        long publicMethodCount = java.util.Arrays.stream(publicMethods)
                .filter(method -> java.lang.reflect.Modifier.isPublic(method.getModifiers()))
                .count();

        assertEquals(1, publicMethodCount);
    }

    @Test
    @DisplayName("Should have only one field")
    void shouldHaveOnlyOneField() {
        var fields = ApplicationSecurityConfiguration.class.getDeclaredFields();

        assertEquals(1, fields.length);
        assertEquals("uri", fields[0].getName());
    }

    @Test
    @DisplayName("Should be a Spring configuration class")
    void shouldBeASpringConfigurationClass() {
        var hasConfigurationAnnotation = ApplicationSecurityConfiguration.class
                .isAnnotationPresent(Configuration.class);
        var hasEnableWebSecurityAnnotation = ApplicationSecurityConfiguration.class
                .isAnnotationPresent(EnableWebSecurity.class);

        assertTrue(hasConfigurationAnnotation);
        assertTrue(hasEnableWebSecurityAnnotation);
    }

    @Test
    @DisplayName("Should have uri field that can be set")
    void shouldHaveUriFieldThatCanBeSet() {
        var configuration = new ApplicationSecurityConfiguration();

        ReflectionTestUtils.setField(configuration, "uri", "/api/test");
        var uriValue = ReflectionTestUtils.getField(configuration, "uri");

        assertEquals("/api/test", uriValue);
    }

    @Test
    @DisplayName("Should be a concrete class")
    void shouldBeAConcreteClass() {
        assertFalse(java.lang.reflect.Modifier.isAbstract(
                ApplicationSecurityConfiguration.class.getModifiers()));
    }

    @Test
    @DisplayName("Should not be an interface")
    void shouldNotBeAnInterface() {
        assertFalse(ApplicationSecurityConfiguration.class.isInterface());
    }

    @Test
    @DisplayName("Should be suitable as Spring Security configuration")
    void shouldBeSuitableAsSpringSecurityConfiguration() {
        var hasConfiguration = ApplicationSecurityConfiguration.class
                .isAnnotationPresent(Configuration.class);
        var hasEnableWebSecurity = ApplicationSecurityConfiguration.class
                .isAnnotationPresent(EnableWebSecurity.class);
        var isPublic = java.lang.reflect.Modifier.isPublic(
                ApplicationSecurityConfiguration.class.getModifiers());

        assertTrue(hasConfiguration);
        assertTrue(hasEnableWebSecurity);
        assertTrue(isPublic);
    }

}