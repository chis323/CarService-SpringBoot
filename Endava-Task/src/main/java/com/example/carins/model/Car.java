package com.example.carins.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 32)
    private String vin;

    private String make;
    private String model;
    private int yearOfManufacture;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Owner owner;

    public Car(String vin, String make, String model, int yearOfManufacture, Owner owner) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.yearOfManufacture = yearOfManufacture;
        this.owner = owner;
    }
}
