package com.example.carins;

import com.example.carins.model.Car;
import com.example.carins.model.Owner;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarInsuranceApplicationTests {

    @InjectMocks
    private CarService service;

    @Mock
    private CarRepository carRepository;

    @Mock
    private InsurancePolicyRepository policyRepository;

    @Test
    void insuranceValidityBasic() {
        Car car1 = new Car("ASDASF82633A", "Dacia", "Logan", 2022,
                new Owner("Kis Raul", "kisraul@email.com"));
        Car car2 = new Car("1234567", "Honda", "Civic", 2019,
                new Owner("Vasiu Bogdan", "bogdan.vasiu@email.com"));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car2));

        when(policyRepository.existsActiveOnDate(1L, LocalDate.parse("2024-06-01"))).thenReturn(true);
        when(policyRepository.existsActiveOnDate(1L, LocalDate.parse("2025-06-01"))).thenReturn(true);
        when(policyRepository.existsActiveOnDate(2L, LocalDate.parse("2025-02-01"))).thenReturn(false);

        assertTrue(service.isInsuranceValid(1L, "2024-06-01"));
        assertTrue(service.isInsuranceValid(1L, "2025-06-01"));
        assertFalse(service.isInsuranceValid(2L, "2025-02-01"));
    }

    @Test
    void carNotFound() {
        when(carRepository.findById(999L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(999L, "2025-06-01"));
        assertEquals("404 NOT_FOUND \"Car with id 999 not found\"", ex.getMessage());
    }

    @Test
    void yearTooEarly() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, "1949-06-01"));
        assertTrue(ex.getMessage().contains("Year 1949 is outside supported range"));
    }

    @Test
    void yearTooLate() {
        int nextYearPlus = java.time.Year.now().getValue() + 2;
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, nextYearPlus + "-06-01"));
        assertTrue(ex.getMessage().contains("outside supported range"));
    }

    @Test
    void monthTooLow() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, "2025-00-10"));
        assertTrue(ex.getMessage().contains("Month 0 is invalid"));
    }

    @Test
    void monthTooHigh() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, "2025-13-10"));
        assertTrue(ex.getMessage().contains("Month 13 is invalid"));
    }

    @Test
    void dayTooLow() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, "2025-06-00"));
        assertTrue(ex.getMessage().contains("Day 0 is invalid"));
    }

    @Test
    void dayTooHighForMonth() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, "2025-02-45"));
        assertTrue(ex.getMessage().contains("Day 45 is invalid"));
    }

    @Test
    void invalidFormat() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, "06-01-2025"));
        assertTrue(ex.getMessage().contains("Invalid date format"));
    }

    @Test
    void nullCarId() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(null, "2025-06-01"));
        assertTrue(ex.getMessage().contains("Car Id and Date are required"));
    }

    @Test
    void nullDate() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(1L, null));
        assertTrue(ex.getMessage().contains("Car Id and Date are required"));
    }
}
