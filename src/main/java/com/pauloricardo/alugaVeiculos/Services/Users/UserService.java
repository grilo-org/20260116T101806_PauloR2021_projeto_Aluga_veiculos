package com.pauloricardo.alugaVeiculos.Services.Users;

import com.pauloricardo.alugaVeiculos.DTOs.Users.UserResponseDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Users.UserUpdateDTO;
import com.pauloricardo.alugaVeiculos.Exceptions.ResourceNotFoundException;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Models.Users.UserRole;
import com.pauloricardo.alugaVeiculos.Repositorys.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO updateUser(UserModel loggedUser, Integer id, UserUpdateDTO dto){
        UserModel userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        //Verifica se é o prórpio usuário ou admin
        if(!loggedUser.getId().equals(id) && loggedUser.getRole() !=  UserRole.ADMIN){
            throw new RuntimeException("\"Access denied: cannot update another user");
        }

        //Atualiza os Campos
        userToUpdate.setName(dto.name());

        if(dto.password() != null && !dto.password().isBlank()){
            userToUpdate.setPassword(passwordEncoder.encode(dto.password()));
        }

        userRepository.save(userToUpdate);

        return new UserResponseDTO(userToUpdate.getId(), userToUpdate.getName(), userToUpdate.getPassword(), userToUpdate.getRole());
    }

    //Método para Listar o Proprio Usuário
    @Transactional
    public UserResponseDTO getLoggedUser(UserModel loggedUser){
        return new UserResponseDTO(
                loggedUser.getId(),
                loggedUser.getName(),
                loggedUser.getUsername(),
                loggedUser.getRole()
        );
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
