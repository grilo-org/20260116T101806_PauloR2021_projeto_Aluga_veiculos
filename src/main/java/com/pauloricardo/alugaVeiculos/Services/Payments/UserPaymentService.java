package com.pauloricardo.alugaVeiculos.Services.Payments;

import com.pauloricardo.alugaVeiculos.DTOs.Payments.*;
import com.pauloricardo.alugaVeiculos.Exceptions.ResourceNotFoundException;
import com.pauloricardo.alugaVeiculos.Models.Payments.PaymentModel;
import com.pauloricardo.alugaVeiculos.Models.Rent.RentModel;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Repositorys.Payments.PaymentRepository;
import com.pauloricardo.alugaVeiculos.Repositorys.Rent.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserPaymentService {
    @Autowired
    RentRepository rentRepository;

    @Autowired
    PaymentRepository paymentRepository;

    //Pagamento com o PIX MOCK
    @Transactional
    public PaymentResponseDTO payRent(PaymentRequestDTO dto){
        RentModel rent = rentRepository.findById(dto.id_rent())
                .orElseThrow(() -> new ResourceNotFoundException("Rent NOt Found"));

        //Validação: Reserva Precisa estar Ativa
        if(rent.getVehicleReturned()){
            throw new RuntimeException("Cannot Pay A Finished Rent");
        }

        //Evitar Pagamento Duplicado
        if(paymentRepository.existsByRent(rent)){
            throw new RuntimeException("This Rent Is Already Paid");
        }

        //MOCK PIX
        String pixCode = "PIX-" + UUID.randomUUID();
        PaymentModel payment = new PaymentModel();
        payment.setRent(rent);
        payment.setAmount(dto.amount());
        payment.setMethod(EnumPaymentMethod.PIX);
        payment.setStatus(EnumPaymentStatus.PAID);
        payment.setTransactionCode(pixCode);
        payment.setPaymentDate(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        return new PaymentResponseDTO(
                payment.getId(),
                rent.getIdRent(),
                payment.getAmount(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                pixCode,
                payment.getPaymentDate()
        );

    }

    //Pagamento no Cartao Crédito / Débito / Dinheiro
    @Transactional
    public PaymentResponseTotalDTO paymentRent(PaymentRequestTotalDTO dto){
        RentModel rent = rentRepository.findById(dto.id_rent())
                .orElseThrow(() -> new ResourceNotFoundException("Rent NOt Found"));

        //Validação: Reserva Precisa estar Ativa
        if(rent.getVehicleReturned()){
            throw new RuntimeException("Cannot Pay A Finished Rent");
        }

        //Evitar Pagamento Duplicado
        if(paymentRepository.existsByRent(rent)){
            throw new RuntimeException("This Rent Is Already Paid");
        }

        if(dto.method() == EnumPaymentMethod.CREDIT_CARD){
            throw new ResourceNotFoundException("Credit Card Not Supported Yet");
        }



        PaymentModel payment = new PaymentModel();
        payment.setRent(rent);
        payment.setAmount(dto.amount());
        payment.setMethod(dto.method());
        payment.setStatus(dto.status());
        payment.setTransactionCode(dto.transactionCode());
        payment.setPaymentDate(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        return new PaymentResponseTotalDTO(
                payment.getId(),
                rent.getIdRent(),
                payment.getAmount(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                payment.getPaymentDate()
        );

    }

    @Transactional
    public PaymentResponseDTO getMyPaymentById(Integer id,UserModel loggedUser){

        PaymentModel payment = paymentRepository
                .findByIdAndUser(id, loggedUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment Not Found")
                );

        return toResponse(payment);

    }

    @Transactional(readOnly = true)
    public Page<PaymentResponseDTO> getMyPayments(
            Pageable pageable,
            UserModel loggedUser
    ) {
        return paymentRepository
                .findByUserId(pageable, loggedUser.getId())
                .map(this::toResponse);
    }

    private PaymentResponseDTO toResponse(PaymentModel pay) {
        return new PaymentResponseDTO(
                pay.getId(),
                pay.getRent().getIdRent(),
                pay.getAmount(),
                pay.getMethod().name(),
                pay.getStatus().name(),
                pay.getTransactionCode(),
                pay.getPaymentDate()
        );
    }


}
