package com.pauloricardo.alugaVeiculos.Controllers.Vehicles;


import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleAdminUpdateAvailableDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleRequestDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleResponseDTO;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Services.Vehicles.AdminVehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/vehicle")
@Tag(name = "Aluga Veiculo - Rota de Cadastro de Veiculos - Admin",description = "Controlador de métodos para cadastrar veículos")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class AdminVehicleController {

    @Autowired
    AdminVehicleService adminVehicleService;


    //Rota para Listar todos os Carros
    @GetMapping()
    @Operation(summary = "Rota para retornar todos os veículos do sistema", description = "Retorna todos os veículos")
    @ApiResponse(responseCode ="201", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<VehicleResponseDTO>> finByAllVehicles(){
        return ResponseEntity.ok(adminVehicleService.findByAllVehicles());
    }

    //Rota para Pesquisar Carros por ID
    @GetMapping("/id/{id}")
    @Operation(summary = "Rota para retornar todos os veículos do sistema por ID ", description = "Retorna todos os veículos")
    @ApiResponse(responseCode ="201", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<VehicleResponseDTO> findByVehiculeById(@PathVariable Integer id){
        return ResponseEntity.ok(adminVehicleService.findByVehiculeById(id));
    }

    //Rota para Pesquisar Modelo,Marca e Placa = "admin/vehicle/search?plate=ZRL8S4"
    @GetMapping("/search")
    @Operation(summary = "Rota para retornar todos os veículos do sistema por placa ou modelo", description = "Retorna todos os veículos")
    @ApiResponse(responseCode ="201", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<VehicleResponseDTO>> saarch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false)String plate
    ){
        return ResponseEntity.ok(adminVehicleService.search(name,plate));
    }

    @GetMapping("/available")
    @Operation(summary = "Rota para retornar todos os veículos do sistema que estão disponível para agendamento", description = "Retorna todos os veículos")
    @ApiResponse(responseCode ="201", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<VehicleResponseDTO>> findByAvailable(){
        return ResponseEntity.ok(adminVehicleService.findByVehiclesAvailableTrue());
    }

    //Rota para Cadastrar Carros
    @PostMapping("/create")
    @Operation(summary = "Rota para criar o veículos", description = "Cria o veículo")
    @ApiResponse(responseCode ="201", description = "Veículo criado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<VehicleResponseDTO> createVehicule(@RequestBody @Valid VehicleRequestDTO dto){

        System.out.println("DTO: "+dto);

        VehicleResponseDTO createVehicule = adminVehicleService.createVehicle(dto);
        return ResponseEntity.ok(createVehicule);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Rota para alterar as informações do veículo por ID", description = "Consegue mudar todas as informações do veículo")
    @ApiResponse(responseCode ="200", description = "Veículo atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Veículo atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable Integer id, @RequestBody @Valid VehicleRequestDTO dto){

        return ResponseEntity.ok(adminVehicleService.updateVehicleAdmin(id,dto));
    }

    @PutMapping("/update/available/{id}")
    @Operation(summary = "Rota para alterar se o veículo está disponível para agendamento", description = "Consegue mudar status do veículo para agendamento")
    @ApiResponse(responseCode ="200", description = "Veículo atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Veículo atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<VehicleResponseDTO> updateVehicelAvailable(
            @PathVariable Integer id,
            @RequestBody @Valid VehicleAdminUpdateAvailableDTO dto){
        return ResponseEntity.ok(adminVehicleService.updateVehicleAdminAvaible(id,dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Rota para deletar o veículo", description = "Deleta o veículo")
    @ApiResponse(responseCode ="200", description = "Veículo deletado com sucesso")
    @ApiResponse(responseCode ="204", description = "Veículo deletado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<String> deleteVehicle(@PathVariable Integer id){
        adminVehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle Deleted Successfully");
    }
}
