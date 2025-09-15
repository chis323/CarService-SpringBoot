package com.example.carins.mapper;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.web.dto.InsurancePolicyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
public class InsurancePolicyMapper {

    public InsurancePolicy toEntity(InsurancePolicyDto dto) {
        return new  InsurancePolicy(null, dto.provider(), dto.startDate(), dto.endDate());
    }
}
