package com.pauloricardo.alugaVeiculos.Services.Users;

import com.pauloricardo.alugaVeiculos.DTOs.Users.UserRequestDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Users.UserResponseDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Users.UserUpdateDTO;
import com.pauloricardo.alugaVeiculos.Exceptions.ResourceNotFoundException;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Repositorys.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class AdminUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    //Criar Usuários
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {

        // Verificar se o username já existe
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ResourceNotFoundException("Username already exists");
        }
        // Criptografando a senha
        String encryptedPassword = passwordEncoder.encode(dto.password());

        // Criando o usuário
        UserModel user = new UserModel();
        user.setName(dto.name());
        user.setUsername(dto.username());
        user.setPassword(encryptedPassword);
        user.setRole(dto.role()); // supondo que UserModel tenha campo 'role'

        userRepository.save(user);

        // Retornando DTO de resposta
        return new UserResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getRole());

    }
    //Metodo para Listar Informações do Proprio Usuário
    @Transactional
    public UserResponseDTO getLoggedUserAdmin(UserModel loggedUser){
        return new UserResponseDTO(
                loggedUser.getId(),
                loggedUser.getName(),
                loggedUser.getUsername(),
                loggedUser.getRole());
    }

    //Método para Listar Todos os Usuários
    @Transactional
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //Método para Listar Usuário via ID
    @Transactional
    public UserResponseDTO findById(Integer id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        return toResponse(user);
    }

    //Método para Listar Usuário via Username
    @Transactional
    public UserResponseDTO findByUserName(String username) {
        UserModel user = userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Not Found"));

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole()
        );
    }

    //Método para Atualizar Usuário
    @Transactional
    public UserResponseDTO updateUser(Integer id, UserUpdateDTO dto) {

        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        user.setName(dto.name());

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        user.setRole(dto.role());
        return toResponse(userRepository.save(user));
    }

    //Método para Deletar Usuário
    @Transactional
    public void deletarUser(Integer id){
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
    }


    private UserResponseDTO toResponse(UserModel user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole()
        );
    }
}
