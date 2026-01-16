package com.pauloricardo.alugaVeiculos.Repositorys.Payments;

import com.pauloricardo.alugaVeiculos.DTOs.Payments.EnumPaymentMethod;
import com.pauloricardo.alugaVeiculos.Models.Payments.PaymentModel;
import com.pauloricardo.alugaVeiculos.Models.Rent.RentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentModel, Integer> {
    boolean existsByRent(RentModel rent);

    Page<PaymentModel> findAll(Pageable pageable);

    @Query("""
        SELECT p
        FROM PaymentModel p
        JOIN p.rent r
        JOIN r.user u
        WHERE u.id = :userId
        ORDER BY p.paymentDate DESC
    """)
    Page<PaymentModel> findByUserId(
            Pageable pageable,
            @Param("userId") Integer userId
    );

    @Query("""
        SELECT p
        FROM PaymentModel p
        JOIN p.rent r
        JOIN r.user u
        WHERE p.id = :paymentId
          AND u.id = :userId
    """)
    Optional<PaymentModel> findByIdAndUser(
            @Param("paymentId") Integer paymentId,
            @Param("userId") Integer userId
    );

    @Query("""
        SELECT p
        FROM PaymentModel p
        JOIN p.rent r
        JOIN r.user u
        WHERE u.id = :userId
            AND (:method IS NULL OR p.method = :method)
        ORDER BY p.paymentDate DESC
    """)
    Page<PaymentModel> findMyPayments(
            Pageable pageable,
            @Param("method") EnumPaymentMethod method,
            @Param("userId") Integer userId
    );
}
