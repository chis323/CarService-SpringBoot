package com.example.carins.service;

import com.example.carins.mapper.InsuranceClaimMapper;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.CarHistoryDto;
import com.example.carins.web.dto.EventType;
import com.example.carins.web.dto.InsuranceClaimDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class InsuranceClaimService {

    @Autowired
    private final InsuranceClaimRepository claimRepository;

    @Autowired
    private final InsurancePolicyRepository policyRepository;

    @Autowired
    private final CarService carService;

    @Autowired
    private final InsuranceClaimMapper insuranceClaimMapper;

    private void validateDto(InsuranceClaimDto dto) {
        if (dto.claimDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Claim date is required");
        }
        if (dto.description() == null || dto.description().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }
        if (dto.amount() == null || dto.amount().doubleValue() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount is required and it must be positive");
        }
    }

    public InsuranceClaim addClaim(Long carId, InsuranceClaimDto dto) {
        validateDto(dto);
        InsuranceClaim claim = insuranceClaimMapper.toEntity(carId, dto);
        return claimRepository.save(claim);
    }

    public InsuranceClaim getClaim(Long carId, Long claimId) {
        return claimRepository.findById(claimId)
                .filter(c -> c.getCar().getId().equals(carId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Claim not found for car " + carId));
    }

    public List<CarHistoryDto> getCarHistory(Long carId) {
        carService.getcarById(carId);
        List<CarHistoryDto> carHistoryDtos = new ArrayList<>();
        claimRepository.findByCarId(carId)
                .forEach(claim -> carHistoryDtos.add(new CarHistoryDto(
                        EventType.InsuranceClaim, claim.getClaimDate(), claim.getDescription(), claim.getAmount()
                )));
        policyRepository.findByCarId(carId)
                .forEach(policy -> carHistoryDtos.add(new CarHistoryDto(
                        EventType.InsurancePolicy, policy.getStartDate(), policy.getProvider(), null
                )));
        carHistoryDtos.sort(Comparator.comparing(CarHistoryDto::date));
        return carHistoryDtos;
    }
}
