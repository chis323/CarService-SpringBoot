package com.example.carins.web.dto;

import java.time.LocalDate;

public record InsurancePolicyDto(
        Long id,
        Long carid,
        String provider,
        LocalDate startDate,
        LocalDate endDate
) {
}