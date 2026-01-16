package com.pauloricardo.alugaVeiculos.Controllers.Vehicles;

import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleResponseDTO;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Services.Vehicles.UserVehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/vehicle")
@Tag(name = "Aluga Veiculo - Rota de Cadastro de Veiculos - User",description = "Controlador de métodos para cadastrar veículos")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class UserVehicleController {

    @Autowired
    private UserVehicleService userVehicleService;

    @GetMapping()
    @Operation(summary = "Rota para retornar todos os veículos do sistema", description = "Retorna todos os veículos")
    @ApiResponse(responseCode ="201", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<VehicleResponseDTO>> findByVehicleAvalieble() {

        List<VehicleResponseDTO> vehicles = userVehicleService.findByVehicleAvaibleTrue();

        if (vehicles.isEmpty()) {
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity.ok(vehicles);
    }

}
