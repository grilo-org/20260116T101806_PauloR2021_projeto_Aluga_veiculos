package com.pauloricardo.alugaVeiculos.Services.Vehicles;

import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleAdminUpdateAvailableDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleRequestDTO;
import com.pauloricardo.alugaVeiculos.DTOs.Vehicles.VehicleResponseDTO;
import com.pauloricardo.alugaVeiculos.Exceptions.ResourceNotFoundException;
import com.pauloricardo.alugaVeiculos.Models.Vehicles.VehicleModel;
import com.pauloricardo.alugaVeiculos.Repositorys.Vehicles.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminVehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    //Criar Veiculos
    @Transactional
    public VehicleResponseDTO createVehicle(VehicleRequestDTO dto) {

        VehicleModel vehicle = new VehicleModel();
        vehicle.setMark(dto.mark());
        vehicle.setModel(dto.model());
        vehicle.setPlate(dto.plate());
        vehicle.setYearVehicle(dto.yearVehicle());
        vehicle.setDailyValue(dto.dailyValue());
        vehicle.setAvailable(dto.available());

        vehicleRepository.save(vehicle);
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getMark(),
                vehicle.getModel(),
                vehicle.getPlate(),
                vehicle.getYearVehicle(),
                vehicle.getDailyValue(),
                vehicle.getAvailable()
        );
    }

    //Listar Todos os Veiculos
    @Transactional
    public List<VehicleResponseDTO> findByAllVehicles(){
        return vehicleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //Listar Veiculos por ID
    @Transactional
    public VehicleResponseDTO findByVehiculeById(Integer id){
        VehicleModel vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle Not Found"));

        return toResponse(vehicle);
    }

    //Listar Veiculos por  Pesquisa de Modelo, Marca e Placa
    @Transactional
    public List<VehicleResponseDTO> search (String name, String plate){
        return vehicleRepository.findByNameAndPlate(name,plate)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //Lista Todos os Veiculos Disponiveis
    @Transactional
    public List<VehicleResponseDTO> findByVehiclesAvailableTrue(){
        return vehicleRepository.findByVehiclesAvailableTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //Faz Update  no Veiculo Completo
    @Transactional
    public VehicleResponseDTO updateVehicleAdmin (Integer id, VehicleRequestDTO dto){
        VehicleModel vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle Not Found"));

        vehicle.setMark(dto.mark());
        vehicle.setModel(dto.model());
        vehicle.setPlate(dto.plate());
        vehicle.setYearVehicle(dto.yearVehicle());
        vehicle.setDailyValue(dto.dailyValue());
        vehicle.setAvailable(dto.available());

        return toResponse(vehicleRepository.save(vehicle));
    }

    //Faz Update do Veiculo Apenas na Função de Disponivel
    @Transactional
    public VehicleResponseDTO updateVehicleAdminAvaible(Integer id, VehicleAdminUpdateAvailableDTO dto){
        VehicleModel vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));

        vehicle.setAvailable(dto.available());

        return toResponse(vehicleRepository.save(vehicle));
    }

    //Deleta Veiculos
    @Transactional
    public void deleteVehicle(Integer id){
        VehicleModel vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));
        vehicleRepository.delete(vehicle);
    }



    private VehicleResponseDTO toResponse(VehicleModel vehicle) {
        return new VehicleResponseDTO(
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
