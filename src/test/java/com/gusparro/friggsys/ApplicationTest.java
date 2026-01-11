package com.gusparro.friggsys;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Application Tests")
class ApplicationTest {

    @Test
    @DisplayName("Should have SpringBootApplication annotation")
    void shouldHaveSpringBootApplicationAnnotation() {
        var annotation = Application.class.getAnnotation(SpringBootApplication.class);

        assertNotNull(annotation);
    }

    @Test
    @DisplayName("Should have main method")
    void shouldHaveMainMethod() throws NoSuchMethodException {
        var mainMethod = Application.class.getDeclaredMethod("main", String[].class);

        assertNotNull(mainMethod);
        assertEquals(void.class, mainMethod.getReturnType());
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    @DisplayName("Should have package-private visibility for main method")
    void shouldHavePackagePrivateVisibilityForMainMethod() throws NoSuchMethodException {
        var mainMethod = Application.class.getDeclaredMethod("main", String[].class);

        assertFalse(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertFalse(java.lang.reflect.Modifier.isPrivate(mainMethod.getModifiers()));
        assertFalse(java.lang.reflect.Modifier.isProtected(mainMethod.getModifiers()));
    }

    @Test
    @DisplayName("Should call SpringApplication.run with correct class when main is invoked")
    void shouldCallSpringApplicationRunWithCorrectClassWhenMainIsInvoked() {
        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            var mockContext = mock(ConfigurableApplicationContext.class);
            springApplicationMock.when(() -> SpringApplication.run(Application.class, new String[]{}))
                    .thenReturn(mockContext);

            Application.main(new String[]{});

            springApplicationMock.verify(() -> SpringApplication.run(Application.class, new String[]{}));
        }
    }

    @Test
    @DisplayName("Should call SpringApplication.run with provided arguments")
    void shouldCallSpringApplicationRunWithProvidedArguments() {
        var args = new String[]{"--server.port=8081", "--spring.profiles.active=test"};

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            var mockContext = mock(ConfigurableApplicationContext.class);
            springApplicationMock.when(() -> SpringApplication.run(Application.class, args))
                    .thenReturn(mockContext);

            Application.main(args);

            springApplicationMock.verify(() -> SpringApplication.run(Application.class, args));
        }
    }

    @Test
    @DisplayName("Should call SpringApplication.run with empty arguments array")
    void shouldCallSpringApplicationRunWithEmptyArgumentsArray() {
        var emptyArgs = new String[]{};

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            var mockContext = mock(ConfigurableApplicationContext.class);
            springApplicationMock.when(() -> SpringApplication.run(Application.class, emptyArgs))
                    .thenReturn(mockContext);

            Application.main(emptyArgs);

            springApplicationMock.verify(() -> SpringApplication.run(Application.class, emptyArgs));
        }
    }

    @Test
    @DisplayName("Should call SpringApplication.run exactly once when main is called")
    void shouldCallSpringApplicationRunExactlyOnceWhenMainIsCalled() {
        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            var mockContext = mock(ConfigurableApplicationContext.class);
            springApplicationMock.when(() -> SpringApplication.run(eq(Application.class), any(String[].class)))
                    .thenReturn(mockContext);

            Application.main(new String[]{});

            springApplicationMock.verify(() -> SpringApplication.run(eq(Application.class), any(String[].class)), times(1));
        }
    }

    @Test
    @DisplayName("Should pass Application class as first parameter to SpringApplication.run")
    void shouldPassApplicationClassAsFirstParameterToSpringApplicationRun() {
        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            var mockContext = mock(ConfigurableApplicationContext.class);
            springApplicationMock.when(() -> SpringApplication.run(Application.class, new String[]{}))
                    .thenReturn(mockContext);

            Application.main(new String[]{});

            springApplicationMock.verify(() -> SpringApplication.run(eq(Application.class), any(String[].class)));
        }
    }

    @Test
    @DisplayName("Should handle multiple command line arguments correctly")
    void shouldHandleMultipleCommandLineArgumentsCorrectly() {
        var args = new String[]{
                "--spring.profiles.active=dev",
                "--server.port=9090",
                "--logging.level.root=DEBUG"
        };

        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            var mockContext = mock(ConfigurableApplicationContext.class);
            springApplicationMock.when(() -> SpringApplication.run(Application.class, args))
                    .thenReturn(mockContext);

            Application.main(args);

            springApplicationMock.verify(() -> SpringApplication.run(Application.class, args));
        }
    }

    @Test
    @DisplayName("Should be in correct package")
    void shouldBeInCorrectPackage() {
        assertEquals("com.gusparro.friggsys", Application.class.getPackage().getName());
    }

    @Test
    @DisplayName("Should have correct class name")
    void shouldHaveCorrectClassName() {
        assertEquals("Application", Application.class.getSimpleName());
    }

    @Test
    @DisplayName("Should be a concrete class")
    void shouldBeAConcreteClass() {
        assertFalse(java.lang.reflect.Modifier.isAbstract(Application.class.getModifiers()));
        assertFalse(java.lang.reflect.Modifier.isInterface(Application.class.getModifiers()));
    }

    @Test
    @DisplayName("Should have public visibility")
    void shouldHavePublicVisibility() {
        assertTrue(java.lang.reflect.Modifier.isPublic(Application.class.getModifiers()));
    }

    @Test
    @DisplayName("Should not be final class")
    void shouldNotBeFinalClass() {
        assertFalse(java.lang.reflect.Modifier.isFinal(Application.class.getModifiers()));
    }

    @Test
    @DisplayName("Should have default constructor")
    void shouldHaveDefaultConstructor() {
        var constructors = Application.class.getDeclaredConstructors();

        assertTrue(constructors.length > 0);
    }

    @Test
    @DisplayName("Should extend Object class")
    void shouldExtendObjectClass() {
        assertEquals(Object.class, Application.class.getSuperclass());
    }

    @Test
    @DisplayName("Should have main method with String array parameter")
    void shouldHaveMainMethodWithStringArrayParameter() throws NoSuchMethodException {
        var mainMethod = Application.class.getDeclaredMethod("main", String[].class);
        var parameterTypes = mainMethod.getParameterTypes();

        assertEquals(1, parameterTypes.length);
        assertEquals(String[].class, parameterTypes[0]);
    }

    @Test
    @DisplayName("Should have main method that accepts variable arguments")
    void shouldHaveMainMethodThatAcceptsVariableArguments() throws NoSuchMethodException {
        var mainMethod = Application.class.getDeclaredMethod("main", String[].class);

        assertNotNull(mainMethod);
        assertTrue(mainMethod.isVarArgs() || mainMethod.getParameterTypes()[0].isArray());
    }

    @Test
    @DisplayName("Should not implement any interfaces")
    void shouldNotImplementAnyInterfaces() {
        var interfaces = Application.class.getInterfaces();

        assertEquals(0, interfaces.length);
    }

    @Test
    @DisplayName("Should be instantiable")
    void shouldBeInstantiable() {
        assertDoesNotThrow(() -> new Application());
    }

    @Test
    @DisplayName("Should have SpringBootApplication annotation with default configuration")
    void shouldHaveSpringBootApplicationAnnotationWithDefaultConfiguration() {
        var annotation = Application.class.getAnnotation(SpringBootApplication.class);

        assertNotNull(annotation);
        assertArrayEquals(new Class<?>[0], annotation.exclude());
        assertArrayEquals(new String[0], annotation.excludeName());
    }

    @Test
    @DisplayName("Should be a valid Spring Boot application entry point")
    void shouldBeAValidSpringBootApplicationEntryPoint() {
        var hasSpringBootAnnotation = Application.class.isAnnotationPresent(SpringBootApplication.class);

        boolean hasMainMethod;
        try {
            var mainMethod = Application.class.getDeclaredMethod("main", String[].class);
            hasMainMethod = java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers());
        } catch (NoSuchMethodException e) {
            hasMainMethod = false;
        }

        assertTrue(hasSpringBootAnnotation);
        assertTrue(hasMainMethod);
    }

    @Test
    @DisplayName("Should not have any fields")
    void shouldNotHaveAnyFields() {
        var fields = Application.class.getDeclaredFields();

        assertEquals(0, fields.length);
    }

    @Test
    @DisplayName("Should be in the base package for component scanning")
    void shouldBeInTheBasePackageForComponentScanning() {
        var packageName = Application.class.getPackage().getName();

        assertTrue(packageName.startsWith("com.gusparro.friggsys"));
        assertEquals("com.gusparro.friggsys", packageName);
    }

    @Test
    @DisplayName("Should have correct fully qualified class name")
    void shouldHaveCorrectFullyQualifiedClassName() {
        assertEquals("com.gusparro.friggsys.Application", Application.class.getName());
    }

    @Test
    @DisplayName("Should be suitable for SpringApplication.run")
    void shouldBeSuitableForSpringApplicationRun() {
        var hasSpringBootAnnotation = Application.class.isAnnotationPresent(SpringBootApplication.class);
        var isPublicClass = java.lang.reflect.Modifier.isPublic(Application.class.getModifiers());

        assertTrue(hasSpringBootAnnotation);
        assertTrue(isPublicClass);
    }

}