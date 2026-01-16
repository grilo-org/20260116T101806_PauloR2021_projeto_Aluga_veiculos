package com.pauloricardo.alugaVeiculos.Controllers.Rent;

import com.pauloricardo.alugaVeiculos.DTOs.Rent.*;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Services.Rent.AdminRentService;
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
@RequestMapping("/admin/rent")
@Tag(name = "Aluga Veiculo - Rota de Agendamento Admin",description = "Controlador de métodos da rota de agendamento do aluguel")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class AdminRentController {
    @Autowired
    AdminRentService adminRentService;

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
                .body(adminRentService.createRent(dto,loggedUser));

    }

    //Rota para Listar Todos os Agendamentos
    @GetMapping()
    @Operation(summary = "Rota para retornar todos os agendamentos do sistema", description = "Retorna todos os agendamentos")
    @ApiResponse(responseCode ="201", description = "Retorn realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<RentResponseDTO>> findByAll(){
        return ResponseEntity.ok(adminRentService.FindByAllRent());
    }

    //Rota para Listar Todos os Agendamentos Por Id De Agendamento
    @GetMapping("/id/{id}")
    @Operation(summary = "Rota para retornar todos os agendamentos do sistema por ID de agendamento", description = "Retorna todos os agendamentos")
    @ApiResponse(responseCode ="201", description = "Retorn realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<RentResponseDTO> findByAllIdUser(@PathVariable Integer id)
    {return ResponseEntity.ok(adminRentService.findByIdRent(id));}

    //Rota para Listar Todos os Agedamentos Por ID do Usuário
    @GetMapping("/user")
    @Operation(summary = "Rota para retornar todos os agendamentos do sistema por usuário", description = "Retorna todos os agendamentos")
    @ApiResponse(responseCode ="201", description = "Retorn realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<RentResponseQueryUserVehicleDTO>> findByRentIdUser(
            @RequestParam(required = false) Integer id
    ){
        return ResponseEntity.ok(adminRentService.findByRentIdUser(id));
    }

    //Rota para Listar Todos os Agendamentos Por Id de Veiculo
    @GetMapping("/vehicle")
    @Operation(summary = "Rota para retornar todos os agendamentos do sistema por ID de veiculo", description = "Retorna todos os agendamentos")
    @ApiResponse(responseCode ="201", description = "Retorn realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<RentResponseQueryUserVehicleDTO>> findByRentIdVehicle(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String plate
    ){
        return ResponseEntity.ok(adminRentService.findByRentIdVehicle(id,plate));
    }

    //Rota para Retirar Veiculo
    @PutMapping("/removed/{id}")
    @Operation(summary = "Rota para mudar o status do veículo para retirado", description = "Consegue mudar o status do veículo como retirado")
    @ApiResponse(responseCode ="200", description = "Agendamento atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Agendamento atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<Void> updateRentRemovedVehicle(@PathVariable Integer id){
        adminRentService.updateRentRemovedVehicle(id);
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
        adminRentService.updateRentReturnedVehicle(id);
        return ResponseEntity.noContent()
                .header("Vehicle Successfully Returned")
                .build();

    }

    //Rota Para Alterar O Agendamento do Aluguel
    @PutMapping("/{id}")
    @Operation(summary = "Rota para alterar o agendamento já existente", description = "Muda o status do veiculo para possivel agendamento")
    @ApiResponse(responseCode ="200", description = "Agendamento atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Agendamento atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<RentResponseDTO> updateRent(
            @PathVariable Integer id,
            @RequestBody @Valid RentRequestUpdateDTO dto,
            @AuthenticationPrincipal UserModel loggedUser){
        return ResponseEntity.ok(adminRentService.updateRent(id,dto,loggedUser));
    }

    //Rota para Deleta Aluguel
    @DeleteMapping("/{id}")
    @Operation(summary = "Rota para deletar o agendamento", description = "Deleta o agendamento do veículo")
    @ApiResponse(responseCode ="200", description = "Agendamento deletado com sucesso")
    @ApiResponse(responseCode ="204", description = "Agendamento deletado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<Void> deleteRent(@PathVariable Integer id){
        adminRentService.deleteRent(id);
        return  ResponseEntity.noContent()
                .header("Delete Rent Sucessfully")
                .build();
    }



}
