package com.pauloricardo.alugaVeiculos.Services.Rent;

import com.pauloricardo.alugaVeiculos.DTOs.Rent.*;
import com.pauloricardo.alugaVeiculos.Exceptions.ResourceNotFoundException;
import com.pauloricardo.alugaVeiculos.Models.Rent.RentModel;
import com.pauloricardo.alugaVeiculos.Models.Users.UserModel;
import com.pauloricardo.alugaVeiculos.Models.Vehicles.VehicleModel;
import com.pauloricardo.alugaVeiculos.Repositorys.Rent.RentRepository;
import com.pauloricardo.alugaVeiculos.Repositorys.Vehicles.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class AdminRentService {

    @Autowired
    private  RentRepository rentRepository;
    @Autowired
    private VehicleRepository vehicleRepository;


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

    //Listar Todas as Reservas Feitas
    @Transactional
    public List<RentResponseDTO> FindByAllRent(){
        List<RentResponseDTO> response = rentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        if(response.isEmpty()){
            System.out.println("Lista Vazia Vazio");
            return List.of();
        }

        return response;
    }

    //Listar Todas As Reservas Via ID de Reserva
    @Transactional
    public RentResponseDTO findByIdRent(Integer id){
        RentModel rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID Not Found"));

        return toResponse(rent);
    }

    //Listar Todas As Reservas Via Id Usuario
    @Transactional
    public List<RentResponseQueryUserVehicleDTO> findByRentIdUser(Integer id){
        List<RentResponseQueryUserVehicleDTO> response = rentRepository.findByRentIdUser(id);

        //Verificando se a Lista é Vazia
        if(response.isEmpty()){
            System.out.println("Lista Vazia Vazio");
            return List.of();
        }
        return response;
    }

    //Listar Todas as Reservas Via Id do Veiculo
    @Transactional
    public List<RentResponseQueryUserVehicleDTO> findByRentIdVehicle(Integer id, String plate){
        List<RentResponseQueryUserVehicleDTO> response = rentRepository.findByRentIdVehicle(id,plate);

        if(response.isEmpty()){
            System.out.println("Lista Vazia Vazio");
            return List.of();
        }

        return response;
    }

    //Update para Retirada do veiculo e a
    @Transactional
    public void updateRentRemovedVehicle(Integer id){

        RentModel rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));

        //Verifica se o Veiculo foi Retirado
        if(Boolean.TRUE.equals(rent.getVehicleRemoved())){
            throw new RuntimeException("This Vehicle Has Already Been Removed ");
        }

        //Atualiza o Veiculo Retirado
        rent.setVehicleRemoved(true);

        rentRepository.save(rent);
    }

    //Update para Devolver o veiculo A Concessionária  e  Deixar ele Disponivel para o Aluguel
    @Transactional
    public void updateRentReturnedVehicle (Integer id){
        RentModel rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));

        //Verifica se já Foi devolvido o Veiculo
        if(Boolean.TRUE.equals(rent.getVehicleReturned())){
            throw new RuntimeException("This Vehicle Has Already Been Returned");
        }

        //Atualiza o Veiculo Retornado
        rent.setVehicleReturned(true);
        rent.setEndDate(LocalDate.now()); //Atualiza a Data de Encerramento do Aluguel

        //Atualiza o Veiculo
        VehicleModel vehicleModel = rent.getVehicle();
        vehicleModel.setAvailable(true);

        rentRepository.save(rent);
        vehicleRepository.save(vehicleModel);

    }

    //Update Completo do Aluguel
    @Transactional
    public RentResponseDTO updateRent (Integer id, RentRequestUpdateDTO dto,UserModel loggedUser ){
        RentModel rent = rentRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Rent Not Found"));

        rent.setUser(loggedUser);
        rent.setStartDate(dto.startDate());
        rent.setEndDate(dto.endDate());
        rent.setVehicleRemoved(dto.vehicleRemoved());
        rent.setVehicleReturned(dto.vehicleReturned());

        return toResponse(rentRepository.save(rent));

    }

    @Transactional
    public void deleteRent(Integer id){
        RentModel rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent Not Found"));

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
