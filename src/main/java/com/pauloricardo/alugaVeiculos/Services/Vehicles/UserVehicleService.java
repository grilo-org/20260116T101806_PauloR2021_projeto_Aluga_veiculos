package com.pauloricardo.alugaVeiculos.Services.Vehicles;

import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleRequestDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleResponseDTO;
import com.pauloricardo.alugaVeiculos.Models.Vehicles.VehicleModel;
import com.pauloricardo.alugaVeiculos.Repositorys.Vehicles.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserVehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public List<VehicleResponseDTO> findByVehicleAvaibleTrue(){
        return vehicleRepository.findByVehiclesAvailableTrue()
                .stream()
                .map(this::toResponse)
                .toList();

    }

    private VehicleResponseDTO toResponse(VehicleModel vehicle){
        return  new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getMark(),
                vehicle.getModel(),
                vehicle.getPlate(),
                vehicle.getYearVehicle(),
                vehicle.getDailyValue(),
                vehicle.getAvailable()
        );
    }
}
