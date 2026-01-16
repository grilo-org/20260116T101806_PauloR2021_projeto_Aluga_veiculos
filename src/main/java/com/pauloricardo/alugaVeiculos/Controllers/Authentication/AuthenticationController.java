package com.pauloricardo.alugaVeiculos.Controllers.Authentication;


import com.pauloricardo.alugaVeiculos.DTOs.Authentication.AuthenticationDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Authentication.LoginResponseDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Authentication.RegisterDTO;
import com.pauloricardo.alugaVeiculos.Infra.Security.SecurityConfiguration;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Models.Users.UserRole;
import com.pauloricardo.alugaVeiculos.Repositorys.Users.UserRepository;
import com.pauloricardo.alugaVeiculos.Services.Authentication.TokenServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Aluga Veiculos - Rota de Login" ,description = "Controlador de Login e Cadastro de Usuários sem Acesso ADMIN")
@SecurityRequirement(name = SecurityConfiguration.SECURITY)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenServices tokenServices;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/login")
    @Operation(summary = "Faz o Login de usuários cadastrados", description = "Faz login do usuário já salvo no banco de dados gerando um retorno de token JWT")
    @ApiResponse(responseCode ="200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "500",description = "Erro no Servidor")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto){

        //Criando uma Váriavel com o Username e o Passworod de Login
        var userNamePassword = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        //Fazendo a Autenticação das Informações
        var auth = this.authenticationManager.authenticate(userNamePassword);

        var token = tokenServices.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @Transactional
    @PostMapping("/register")
    @Operation(summary = "Faz o cadastro de um Novo Usuário", description = "Realiza o cadastro de um usuário novo sem ter acessos de autenticação ROLE_ADMIN")
    @ApiResponse(responseCode ="201", description = "Cadastro Realizado com Sucesso")
    @ApiResponse(responseCode = "500",description = "Erro no Servidor")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO dto){

        //Verificando se no Banco de Dados já existe o Usuário
        if(this.userRepository.findByUsername(dto.username()).isPresent()){
            return ResponseEntity.badRequest().body("Username already exists");
        }

        //Criptografando a senha que o Usuário Digitou
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());

        //Criando um Novo User
        UserModel user = new UserModel(dto.name(),dto.username(),encryptedPassword, UserRole.USER);

        this.userRepository.save(user); //Salvando o Usuário no Banco de dados

        return ResponseEntity.ok("User registered successfully");

    }



}
