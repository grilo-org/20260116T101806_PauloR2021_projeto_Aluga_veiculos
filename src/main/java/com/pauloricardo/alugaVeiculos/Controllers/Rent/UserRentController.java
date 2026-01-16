package com.pauloricardo.alugaVeiculos.Controllers.Rent;

import com.pauloricardo.alugaVeiculos.DTOs.Rent.RentRequestDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Rent.RentRequestUpdateDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Rent.RentResponseDTO;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Models.Rent.RentModel;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Services.Rent.UserRentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/rent/my")
@Tag(name = "Aluga Veiculo - Rota de Agendamento User",description = "Controlador de métodos da rota de agendamento do aluguel")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class UserRentController {

    @Autowired
    UserRentService userRentService;

    //Rota para Criar Agendamento
    @PostMapping()
    @Operation(summary = "Rota para criar o agendamento", description = "Cria o agendamento do veículo")
    @ApiResponse(responseCode ="201", description = "Agendamento criado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<RentResponseDTO> createRent(
            @RequestBody @Valid RentRequestDTO dto,
            @AuthenticationPrincipal UserModel loggedUser){
        System.out.println(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRentService.createRent(dto,loggedUser));

    }

    //Rota para Listar Minhas Reservas
    @GetMapping()
    @Operation(summary = "Rota para retornar todos os agendamentos do sistema", description = "Retorna todos os agendamentos")
    @ApiResponse(responseCode ="201", description = "Retorn realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<RentModel>> getMyRents(){
        return ResponseEntity.ok(userRentService.getMyRents());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Rota para retornar todos os agendamentos do sistema por ID de agendamento", description = "Retorna todos os agendamentos")
    @ApiResponse(responseCode ="201", description = "Retorn realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<RentResponseDTO> updateMyRent(
            @PathVariable Integer id,
            @RequestBody @Valid RentRequestUpdateDTO dto){
        return ResponseEntity.ok(userRentService.updateMyRent(id,dto));
    }

    //Rota para Retirar Veiculo
    @PutMapping("/removed/{id}")
    @Operation(summary = "Rota para mudar o status do veículo para retirado", description = "Consegue mudar o status do veículo como retirado")
    @ApiResponse(responseCode ="200", description = "Agendamento atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Agendamento atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<Void> updateRentRemovedVehicle(@PathVariable Integer id){
        userRentService.updateRentRemovedVehicle(id);
        return ResponseEntity
                .noContent()
                .header("Vehicle Sucessully Removed")
                .build();
    }

    //Rota para Retornar Veiculo a Concessionária
    @PutMapping("/returned/{id}")
    @Operation(summary = "Rota para retornar o veiculo para um possivel agendamento", description = "Muda o status do veiculo para possivel agendamento")
    @ApiResponse(responseCode ="200", description = "Agendamento atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Agendamento atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<Void> updateReturnedVehicle(@PathVariable Integer id){
        userRentService.updateRentReturnedVehicle(id);
        return ResponseEntity.noContent()
                .header("Vehicle Successfully Returned")
                .build();

    }

    //Deleta Reserva
    @DeleteMapping("/{id}")
    @Operation(summary = "Rota para deletar o agendamento", description = "Deleta o agendamento do veículo")
    @ApiResponse(responseCode ="200", description = "Agendamento deletado com sucesso")
    @ApiResponse(responseCode ="204", description = "Agendamento deletado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<Void> deleteRent(@PathVariable Integer id){
        userRentService.deleteRent(id);

        return  ResponseEntity.noContent()
                .header("Delete Rent Sucessfully")
                .build();
    }
}
