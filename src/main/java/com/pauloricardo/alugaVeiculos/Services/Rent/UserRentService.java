package com.pauloricardo.alugaVeiculos.Services.Rent;


import com.pauloricardo.alugaVeiculos.DTOs.Rent.RentRequestDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Rent.RentRequestUpdateDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Rent.RentResponseDTO;
import com.pauloricardo.alugaVeiculos.Exceptions.ResourceNotFoundException;
import com.pauloricardo.alugaVeiculos.Models.Rent.RentModel;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Models.Vehicles.VehicleModel;
import com.pauloricardo.alugaVeiculos.Repositorys.Rent.RentRepository;
import com.pauloricardo.alugaVeiculos.Repositorys.Users.UserRepository;
import com.pauloricardo.alugaVeiculos.Repositorys.Vehicles.VehicleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UserRentService {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    //Criar Agendas de Aluguel
    @Transactional
    public RentResponseDTO createRent(RentRequestDTO dto, UserModel loggedUser){

        VehicleModel vehicleModel = vehicleRepository.findById(dto.idVehicle())
                .orElseThrow(() -> new RuntimeException("Vehicle Not Found"));

        //Veriifca se o Veículo pode Ser Alugador ou Não
        //True - Veiculo Disponivel
        //False - Veiculo Indisponivel
        if(!vehicleModel.getAvailable()){
            throw new RuntimeException("Vehicle Is Not Available");
        }

        // Calcula dias - plusDays para contar o dia final da reserva
        long days = ChronoUnit.DAYS.between(dto.startDate(), dto.endDate().plusDays(1));

        if (days <= 0) {
            throw new RuntimeException("Minimum rental period is 1 day");
        }

        // Calcula valor total
        BigDecimal totalValue = vehicleModel.getDailyValue()
                .multiply(BigDecimal.valueOf(days));


        RentModel rent = new RentModel();
        rent.setUser(loggedUser);
        rent.setVehicle(vehicleModel);
        rent.setStartDate(dto.startDate());
        rent.setEndDate(dto.endDate());
        rent.setTotalValue(totalValue);
        rent.setVehicleRemoved(false);

        rentRepository.save(rent);

        //Marca o Veiculo Indisponivel
        vehicleModel.setAvailable(false);
        vehicleRepository.save(vehicleModel);

        return toResponse(rent);
    }


    //Listar todas as Reservas feita Pelo Próprio Usuário
    @Transactional
    public List<RentModel>getMyRents(){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        UserModel user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found Rent"));

        return rentRepository.findByUserId(user.getId());
    }

    //Update Para Sua Próprias Reservas
    @Transactional
    public RentResponseDTO updateMyRent(Integer id,RentRequestUpdateDTO dto){
        //Usuário Autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel loggedUser = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Foun Rent"));

        //Busca a Reserva Pelo Usuário
        RentModel rent = rentRepository.findByIdRentAndUserId(id,loggedUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No Rental Was Found For This User"));

        //Atualizações Permitidas

        rent.setStartDate(dto.startDate());
        rent.setEndDate(dto.endDate());
        rent.setVehicleRemoved(dto.vehicleRemoved());
        rent.setVehicleReturned(dto.vehicleReturned());
;
        return toResponse(rentRepository.save(rent));
    }

    //Marcar veiculo Retirado
    @Transactional
    public void updateRentRemovedVehicle(Integer id){
        //Usuário Autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel loggedUser = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Foun Rent"));

        //Busca a Reserva Pelo Usuário
        RentModel rent = rentRepository.findByIdRentAndUserId(id,loggedUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No Rental Was Found For This User"));

        //Verifica se o Veiculo foi Retirado
        if(Boolean.TRUE.equals(rent.getVehicleRemoved())){
            throw new RuntimeException("This Vehicle Has Already Been Removed ");
        }

        //Atualiza o Veiculo Retirado
        rent.setVehicleRemoved(true);

        rentRepository.save(rent);

    }

    //Marcar como Devolvido o Veiculo
    @Transactional
    public void updateRentReturnedVehicle (Integer id){

        //Usuário Autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel loggedUser = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Foun Rent"));

        //Busca a Reserva Pelo Usuário
        RentModel rent = rentRepository.findByIdRentAndUserId(id,loggedUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No Rental Was Found For This User"));

        //Atualiza o Veiculo Retornado
        rent.setVehicleReturned(true);
        rent.setEndDate(LocalDate.now()); //Atualiza a Data de Encerramento do Aluguel

        //Atualiza o Veiculo
        VehicleModel vehicleModel = rent.getVehicle();
        vehicleModel.setAvailable(true);

        rentRepository.save(rent);
        vehicleRepository.save(vehicleModel);

    }

    @Transactional
    public void deleteRent(Integer id){
        //Usuário Autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel loggedUser = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Foun Rent"));

        //Busca a Reserva Pelo Usuário
        RentModel rent = rentRepository.findByIdRentAndUserId(id,loggedUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No Rental Was Found For This User"));

        rentRepository.delete(rent);

    }


    private RentResponseDTO toResponse(RentModel rent) {
        return new RentResponseDTO(
                rent.getIdRent(),
                rent.getUser().getId(),
                rent.getVehicle().getId(),
                rent.getStartDate(),
                rent.getEndDate(),
                rent.getTotalValue(),
                rent.getVehicleRemoved(),
                rent.getVehicleReturned()

        );
    }
}
