package com.david.springboot.backend.backend_products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;

class DatasourceConfigurationTest {

    private PropertySourcesPropertyResolver resolver(Map<String, Object> variables) throws IOException {
        var sources = new MutablePropertySources();
        sources.addLast(new MapPropertySource("test-environment", variables));
        sources.addLast(new PropertiesPropertySource(
                "application", PropertiesLoaderUtils.loadAllProperties("application.properties")));
        return new PropertySourcesPropertyResolver(sources);
    }

    @Test
    void resolvesDatasourceSettingsFromEnvironment() throws IOException {
        var properties = resolver(Map.of(
                "DB_URL", "jdbc:mysql://localhost:3306/sample",
                "DB_USERNAME", "sample-user",
                "DB_PASSWORD", "sample-value"));

        assertEquals("jdbc:mysql://localhost:3306/sample",
                properties.getRequiredProperty("spring.datasource.url"));
        assertEquals("sample-user",
                properties.getRequiredProperty("spring.datasource.username"));
        assertEquals("sample-value",
                properties.getRequiredProperty("spring.datasource.password"));
    }

    @Test
    void rejectsMissingDatasourcePassword() throws IOException {
        var properties = resolver(Map.of(
                "DB_URL", "jdbc:mysql://localhost:3306/sample",
                "DB_USERNAME", "sample-user"));

        assertThrows(IllegalArgumentException.class,
                () -> properties.getRequiredProperty("spring.datasource.password"));
    }
}
