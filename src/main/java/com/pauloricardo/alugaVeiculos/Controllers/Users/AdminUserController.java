package com.pauloricardo.alugaVeiculos.Controllers.Users;


import com.pauloricardo.alugaVeiculos.DTOs.Users.UserRequestDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Users.UserResponseDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Users.UserUpdateDTO;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Services.Users.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
@Tag(name = "Aluga Veiculo - Rota User Admin",description = "Controlador de Métodos da Rota user com acesso ADMIN")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;

    //Listar as Próprias Infor do Usuário Logado
    @GetMapping("/me")
    @Operation(summary = "Rota para devolver as info do usuário", description = "Devolve as informações do usuário")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> getLopggedUser(){
        //Pega o Usuário logado via SeccurityContext
        UserModel user = (UserModel) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserResponseDTO userDto = adminUserService.getLoggedUserAdmin(user);
        return  ResponseEntity.ok(userDto);
    }

    //Listar Todos os Usuários
    @GetMapping()
    @Operation(summary = "Rota para devolver as info dos usuários", description = "Devolve as informações de todos usuários")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<List<UserResponseDTO>> findByAll(){
        return ResponseEntity.ok(adminUserService.findAll());
    }

    //Listar por ID
    @GetMapping("/id/{id}")
    @Operation(summary = "Rota para devolver as info do usuário por ID", description = "Devolve as informações de todos usuários")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com Sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Integer id){
        System.out.println("DEBUG: ID recebido -> " + id);
        return ResponseEntity.ok(adminUserService.findById(id));
    }

    //Listar por Username
    @GetMapping("/username/{username}")
    @Operation(summary = "Rota para devolver as info do usuário ", description = "Devolve as informações do usuário por username")
    @ApiResponse(responseCode ="200", description = "Retorno realizado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não Autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> findByUsermane(@PathVariable String username){
        System.out.println("DEBUG: Username recebido -> " + username);
        return ResponseEntity.ok(adminUserService.findByUserName(username));
    }

    //Criando Usuário
    @PostMapping("/create")
    @Operation(summary = "Rota para criar um usuário", description = "Cria usuário no sistema com funções de ADMIN / USER")
    @ApiResponse(responseCode ="201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO dto) {
        System.out.println("DEBUG: Chegou no controller /create!");
        System.out.println("Body recebido: " + dto);

        UserResponseDTO createdUser = adminUserService.createUser(dto);
        return ResponseEntity.ok(createdUser);
    }

    //Atualizando Usuário
    @PutMapping("/{id}")
    @Operation(summary = "Rota para alterar as informações do usuário", description = "Consegue realizar update nas info do usuário")
    @ApiResponse(responseCode ="200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode ="204", description = "Usuário atualizado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserUpdateDTO dto){

        return ResponseEntity.ok(adminUserService.updateUser(id,dto));
    }

    //Deletando Usuário
    @DeleteMapping("/{id}")
    @Operation(summary = "Rota para deletar o usuário", description = "Deleta o usuário por ID")
    @ApiResponse(responseCode ="200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode ="204", description = "Usuário deletado com sucesso mas sem retorno")
    @ApiResponse(responseCode ="401", description = "Usuário não autenticado")
    @ApiResponse(responseCode = "500",description = "Erro no servidor")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id){
        adminUserService.deletarUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }

}
