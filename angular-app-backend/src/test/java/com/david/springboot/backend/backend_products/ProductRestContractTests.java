package com.david.springboot.backend.backend_products;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.david.springboot.backend.backend_products.entities.Product;
import com.david.springboot.backend.backend_products.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(BackendProductsApplicationTests.H2DataSourceConfiguration.class)
class ProductRestContractTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void productsCollectionUsesHal() throws Exception {
        mockMvc.perform(get("/products").accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/hal+json")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @Transactional
    void productResourceExposesIdAndHalLinks() throws Exception {
        Product product = new Product();
        product.setName("Test product");
        product.setPrice(100L);
        product.setQuantity(2L);
        product.setDescription("Contract test");
        Long id = productRepository.save(product).getId();

        mockMvc.perform(get("/products/{id}", id).accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test product"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.description").value("Contract test"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void productsAllowsConfiguredOrigin() throws Exception {
        mockMvc.perform(options("/products")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }
}
