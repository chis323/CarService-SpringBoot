package com.example.carins.web;

import com.example.carins.model.InsuranceClaim;
import com.example.carins.service.InsuranceClaimService;
import com.example.carins.web.dto.CarHistoryDto;
import com.example.carins.web.dto.InsuranceClaimDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cars")
public class InsuranceClaimController {

    @Autowired
    private InsuranceClaimService insuranceClaimService;

    @PostMapping("{carId}/claims")
    public ResponseEntity<InsuranceClaim> registerNewClaim(@PathVariable Long carId, @RequestBody InsuranceClaimDto dto) {
        InsuranceClaim claim = insuranceClaimService.addClaim(carId, dto);
        return ResponseEntity
                .created(URI.create("/api/cars/" + carId + "/claims/" + claim.getId()))
                .body(claim);
    }

    @GetMapping("{carId}/claims/{claimId}")
    public ResponseEntity<InsuranceClaim> getClaim(@PathVariable Long carId, @PathVariable Long claimId) {
        InsuranceClaim claim = insuranceClaimService.getClaim(carId, claimId);
        return ResponseEntity.ok(claim);
    }

    @GetMapping("{carId}/history")
    public ResponseEntity<List<CarHistoryDto>> getClaimHistory(@PathVariable Long carId) {
        List<CarHistoryDto> history = insuranceClaimService.getCarHistory(carId);
        return ResponseEntity.ok(history);
    }
}
