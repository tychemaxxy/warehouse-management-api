package com.shop.warehouse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WarehouseApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldPreventSellingMoreThanAvailableStock()
            throws Exception {

        MvcResult productResult = mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Test Shirt",
                      "description": "Integration test product"
                    }
                    """)
        )
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode productJson = objectMapper.readTree(
            productResult.getResponse().getContentAsString()
        );

        long productId = productJson.get("id").asLong();

        MvcResult variantResult = mockMvc.perform(
            post("/api/products/" + productId + "/variants")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "sku": "TEST-SHIRT-001",
                      "size": "M",
                      "color": "Black",
                      "price": 75000,
                      "stock": 5
                    }
                    """)
        )
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode variantJson = objectMapper.readTree(
            variantResult.getResponse().getContentAsString()
        );

        long variantId = variantJson.get("id").asLong();

        mockMvc.perform(
            post("/api/variants/" + variantId + "/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "quantity": 3
                    }
                    """)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.remainingStock").value(2));

        mockMvc.perform(
            post("/api/variants/" + variantId + "/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "quantity": 3
                    }
                    """)
        )
            .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/products/" + productId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.variants[0].stock").value(2));
    }
}