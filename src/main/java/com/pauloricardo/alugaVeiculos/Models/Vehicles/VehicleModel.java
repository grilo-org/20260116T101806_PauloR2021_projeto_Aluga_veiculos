package com.pauloricardo.alugaVeiculos.Models.Vehicles;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Getter @Setter
public class VehicleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicles")
    private Integer id;

    @Column(length = 100)
    private String mark;

    @Column(length = 100, nullable = false)
    private String model;

    @Column(length = 10, nullable = false, unique = true)
    private String plate;

    @Column(name = "year_vehicle")
    private Integer yearVehicle;

    @Column(name = "daily_value",precision = 10,scale = 2)
    private BigDecimal dailyValue;

    private Boolean available = true;


    public VehicleModel() {
    }

    public VehicleModel(Integer id, String mark, String model, String plate, Integer yearVehicle, BigDecimal dailyValue, Boolean available) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.plate = plate;
        this.yearVehicle = yearVehicle;
        this.dailyValue = dailyValue;
        this.available = available;
    }

}
