package com.pauloricardo.alugaVeiculos.Models.Rent;

import com.pauloricardo.alugaVeiculos.Models.Payments.PaymentModel;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Models.Vehicles.VehicleModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rent")
@Getter @Setter
public class RentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rent")
    private Integer idRent;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "id_vehicle", nullable = false)
    private VehicleModel vehicle;

    @OneToMany(mappedBy = "rent", cascade = CascadeType.ALL)
    private List<PaymentModel> payments;

    @Column(name = "start_date",nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_value",precision = 10,scale = 2)
    private BigDecimal totalValue;

    @Column(name = "vehicle_removed",nullable = false)
    private  Boolean vehicleRemoved = false;

    @Column(name="vehicle_returned",nullable = false)
    private Boolean vehicleReturned = false;

    public RentModel() {
    }

    public RentModel(Integer idRent, UserModel user, VehicleModel vehicle, LocalDate startDate, LocalDate endDate, BigDecimal totalValue, Boolean vehicleRemoved, Boolean vehicleReturned) {
        this.idRent = idRent;
        this.user = user;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalValue = totalValue;
        this.vehicleRemoved = vehicleRemoved;
        this.vehicleReturned = vehicleReturned;
    }
}
