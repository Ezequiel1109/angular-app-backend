package com.david.springboot.backend.backend_products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(BackendProductsApplicationTests.H2DataSourceConfiguration.class)
class BackendProductsApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {
	}

	@Test
	void usesInMemoryH2() throws SQLException {
		assertTrue(dataSource instanceof EmbeddedDatabase);
		try (var connection = dataSource.getConnection()) {
			assertEquals("H2", connection.getMetaData().getDatabaseProductName());
			assertTrue(connection.getMetaData().getURL().startsWith("jdbc:h2:mem:"));
		}
	}

	@TestConfiguration(proxyBeanMethods = false)
	static class H2DataSourceConfiguration {
		@Bean
		@Primary
		DataSource testDataSource() {
			return new EmbeddedDatabaseBuilder()
					.generateUniqueName(true)
					.setType(EmbeddedDatabaseType.H2)
					.build();
		}
	}

}
