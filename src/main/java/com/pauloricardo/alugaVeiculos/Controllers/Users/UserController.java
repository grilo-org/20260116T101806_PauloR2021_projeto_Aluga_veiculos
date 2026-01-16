package com.pauloricardo.alugaVeiculos.Controllers.Users;


import com.pauloricardo.alugaVeiculos.DTOs.Users.UserResponseDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Users.UserUpdateDTO;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Services.Users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Aluga Veiculo - Rota User",description = "Controlador de Métodos da Rota user com acesso ADMIN")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class UserController {

    @Autowired
    private UserService userService;


    //Método para Atualizar as próprias Informações
    @PutMapping("/{id}")
    @Operation(summary = "Rota para alterar as informações do usuário", description = "Consegue realizar update nas info do usuário")
    @ApiResponse(responseCode ="200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Usuário atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserUpdateDTO dto){

        //Pega o Usuário Logado
        UserModel loggadUser = (UserModel) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserResponseDTO updateUser = userService.updateUser(loggadUser,id,dto);

        return ResponseEntity.ok(updateUser);
    }

    //Método para Listar as próprias informações
    @GetMapping("/me")
    @Operation(summary = "Rota para devolver as info do usuário", description = "Devolve as informações do usuário")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> getLoggedUser(){
        //Pega o Usuário logado via SecurityContext
        UserModel user = (UserModel)  SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserResponseDTO userDto = userService.getLoggedUser(user);
        return ResponseEntity.ok(userDto);

    }

}
