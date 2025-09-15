package com.example.carins;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CarInsuranceApplicationTests {

    @Autowired
    CarService service;

    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, "2024-06-01"));
        assertTrue(service.isInsuranceValid(1L, "2025-06-01"));
        assertFalse(service.isInsuranceValid(2L, "2025-02-01"));
    }
}
