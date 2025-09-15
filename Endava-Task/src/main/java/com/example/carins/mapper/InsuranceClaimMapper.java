package com.example.carins.mapper;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.InsuranceClaimDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InsuranceClaimMapper {
    @Autowired
    private final CarService carService;

    public InsuranceClaim toEntity(Long cardId, InsuranceClaimDto dto) {
        Car car = carService.getcarById(cardId);
        return new InsuranceClaim(car, dto.claimDate(), dto.description(), dto.amount());
    }
}
