package com.pauloricardo.alugaVeiculos.Models.Payments;

import com.pauloricardo.alugaVeiculos.DTOs.Payments.EnumPaymentMethod;
import com.pauloricardo.alugaVeiculos.DTOs.Payments.EnumPaymentStatus;
import com.pauloricardo.alugaVeiculos.Models.Rent.RentModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "payments")
@Getter @Setter
public class PaymentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_rent", nullable = false)
    private RentModel rent;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumPaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumPaymentStatus status;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
