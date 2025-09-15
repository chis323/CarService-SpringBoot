package com.example.carins.service;

import com.example.carins.mapper.InsurancePolicyMapper;
import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Slf4j
public class InsurancePolicyService {

    @Autowired
    private final InsurancePolicyRepository insurancePolicyRepository;

    @Autowired
    private InsurancePolicyMapper insurancePolicyMapper;

    @Autowired
    private final CarService carService;

    private void validateDto(InsurancePolicyDto dto)
    {
        if(dto.carid()==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car is required"); //4xx fail
        }
        if(dto.startDate() == null || dto.endDate() == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and End Date is required"); //4xx fail
        }
        if(dto.endDate().isBefore(dto.startDate()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be before start date"); //4xx fail
        }
    }

    public InsurancePolicy addInsurance(InsurancePolicyDto dto) {
        validateDto(dto);

        Car car = carService.getcarById(dto.carid());

        InsurancePolicy policy = insurancePolicyMapper.toEntity(dto);
        policy.setCar(car);

        return insurancePolicyRepository.save(policy);
    }


    public InsurancePolicy updateInsurance(Long id, InsurancePolicyDto dto) {
        validateDto(dto);

        InsurancePolicy existing = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policy not found"));

        Car car = carService.getcarById(dto.carid());

       InsurancePolicy policy = insurancePolicyMapper.toEntity(dto);

        existing.setCar(car);
        existing.setProvider(policy.getProvider());
        existing.setStartDate(policy.getStartDate());
        existing.setEndDate(policy.getEndDate());

        return insurancePolicyRepository.save(existing);
    }
}
