package com.pauloricardo.alugaVeiculos.Controllers.Payments;


import com.pauloricardo.alugaVeiculos.DTOs.API.ApiResponseDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Payments.*;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Services.Payments.UserPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/payments")
@Tag(name = "Aluga Veiculo - Rota Pagamento User",description = "Controlador de métodos da rota de pagamento do aluguel")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class UserPaymentController {

    @Autowired
    private UserPaymentService paymentService;

    @Operation(summary = "Rota para criar pagamentos por PIX", description = "Cria pagamentos por PIX gerando um código para teste")
    @ApiResponse(responseCode ="201", description = "Pagamento criado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    @PostMapping("/pix")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> payWithPix(
            @RequestBody PaymentRequestDTO dto
    ) {
        PaymentResponseDTO response = paymentService.payRent(dto);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payment made successfully",
                        response
                )
        );
    }


    @Operation(summary = "Rota para criar pagamentos por outros métodos", description = "Cria pagamentos por cartão de débito e dinheiro")
    @ApiResponse(responseCode ="201", description = "Pagamento criado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<PaymentResponseTotalDTO>> paymentRent(
            @RequestBody PaymentRequestTotalDTO dto
    ) {
        PaymentResponseTotalDTO response = paymentService.paymentRent(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseDTO<>(
                        HttpStatus.CREATED.value(),
                        "Payment created successfully",
                        response
                )
        );
    }


    @Operation(summary = "Rota para devolve os pagamentos realizados pelo próprio usuário logado", description = "Devolve todos os pagamentos realizados dentro do sistema")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    @GetMapping("/my")
    public ResponseEntity<ApiResponseDTO<Page<PaymentResponseDTO>>> myPayments(
            Pageable pageable,
            @AuthenticationPrincipal UserModel user
    ) {
        Page<PaymentResponseDTO> response =
                paymentService.getMyPayments(pageable, user);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payments listed successfully",
                        response
                )
        );
    }



    @GetMapping("/my/{id}")
    @Operation(summary = "Rota para devolve os pagamentos realizados pelo proprio usuário com Id de pagamento", description = "Devolve todos os pagamentos realizados dentro do sistema")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> getMyPayment(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserModel user
    ) {
        PaymentResponseDTO response =
                paymentService.getMyPaymentById(id, user);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payment found successfully",
                        response
                )
        );
    }



    @GetMapping("/search")
    @Operation(summary = "Rota para devolve os pagamentos via filtros", description = "Devolve todos os pagamentos realizados dentro do sistema")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<Page<PaymentResponseDTO>>> myPaymentsByMethod(
            Pageable pageable,
            @RequestParam(required = false) EnumPaymentMethod method,
            @AuthenticationPrincipal UserModel user
    ) {
        Page<PaymentResponseDTO> response =
                paymentService.getMyPayments(pageable, user);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payments filtered successfully",
                        response
                )
        );
    }
}