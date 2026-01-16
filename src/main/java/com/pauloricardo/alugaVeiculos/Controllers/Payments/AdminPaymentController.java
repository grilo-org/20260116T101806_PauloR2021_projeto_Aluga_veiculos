package com.pauloricardo.alugaVeiculos.Controllers.Payments;


import com.pauloricardo.alugaVeiculos.DTOs.API.ApiResponseDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Payments.*;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Services.Payments.AdminPaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/payments")
@Tag(name = "Aluga Veiculo - Rota Pagamento Admin",description = "Controlador de métodos da rota de pagamento do aluguel")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class AdminPaymentController {
    private final AdminPaymentService adminPaymentService;

    public AdminPaymentController(AdminPaymentService adminPaymentService) {
        this.adminPaymentService = adminPaymentService;
    }



    //Rota para Criar Pagamentos Via PIX
    @PostMapping("/pix")
    @Operation(summary = "Rota para criar pagamentos por PIX", description = "Cria pagamentos por PIX gerando um código para teste")
    @ApiResponse(responseCode ="201", description = "Pagamento criado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> payWithPix(
            @RequestBody PaymentRequestDTO dto
    ) {
        PaymentResponseDTO response = adminPaymentService.payRent(dto);
        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payment Made Successfully",
                        response

                )

        );
    }

    //Rota para Criar outros métodos de pagamento
    @PostMapping()
    @Operation(summary = "Rota para criar pagamentos por outros métodos", description = "Cria pagamentos por cartão de débito e dinheiro")
    @ApiResponse(responseCode ="201", description = "Pagamento criado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<PaymentResponseTotalDTO>>paymentRent(
            @RequestBody PaymentRequestTotalDTO dto
            ){
        PaymentResponseTotalDTO response = adminPaymentService.paymentRent(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseDTO<>(
                        HttpStatus.CREATED.value(),
                        "Payment created successfully",
                        response
                ));
    }

    //Lista para lista Todos os pagamentos
    @GetMapping
    @Operation(summary = "Rota para devolve os pagamentos realizados", description = "Devolve todos os pagamentos realizados dentro do sistema")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<Page<PaymentResponseDTO>>> findAll(
            Pageable pageable
    ) {
        Page<PaymentResponseDTO> response = adminPaymentService.findAll(pageable);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payments listed successfully",
                        response
                )
        );
    }

    //Listar Todos Por Id
    @GetMapping("/{id}")
    @Operation(summary = "Rota para devolve os pagamentos realizados", description = "Devolve todos os pagamentos realizados dentro do sistema")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>>  findByAllId(
            @PathVariable Integer id
    ){
        PaymentResponseDTO response = adminPaymentService.findByAllId(id);
        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payment found successfully",
                        response
                )
        );
    }

    //Rota para Editar Metodo de Pagamento e Status
    @PutMapping("update/{id}")
    @Operation(summary = "Rota para alterar os meio de pogamento e o status", description = "Consegue mudar os tipos de pagamentos e o status de cada pagamento")
    @ApiResponse(responseCode ="200", description = "Pagamento realizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Pagamento atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<PaymentResponseDTO>> updateMethodStatus(
            @PathVariable Integer id,
            @RequestBody @Valid PaymentRequestUpdateMethodStatusDTO dto
            )
    {
        PaymentResponseDTO response = adminPaymentService.updateMethodStatus(id,dto);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payment updated successfully",
                        response
                )
        );
    }

    //Rota para Deletar Pagamento
    @DeleteMapping("/{id}")
    @Operation(summary = "Rota para deletar o pagamento", description = "Deleta algum pagamento indevido")
    @ApiResponse(responseCode ="200", description = "Pagamento deletado com sucesso")
    @ApiResponse(responseCode ="204", description = "Pagamento deletado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<ApiResponseDTO<Void>>  deletePay(@PathVariable Integer id){
        adminPaymentService.deletePay(id);
        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        HttpStatus.OK.value(),
                        "Payment deleted successfully",
                        null
                )
        );
    }

}
