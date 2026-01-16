package com.pauloricardo.alugaVeiculos.Repositorys.Rent;

import com.pauloricardo.alugaVeiculos.DTOs.Rent.RentResponseQueryUserVehicleDTO;
import com.pauloricardo.alugaVeiculos.Models.Rent.RentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<RentModel, Integer> {


    List<RentModel> findByUserId(Integer userId);


    Optional<RentModel> findByIdRentAndUserId(Integer rentId, Integer userId);

    //Query para Lista todas as Reservas em um Usuário Especifico
    @Query("""
            SELECT new com.pauloricardo.alugaVeiculos.DTOs.Rent.RentResponseQueryUserVehicleDTO(
                r.idRent,
                r.startDate,
                r.endDate,
                r.totalValue,
                r.vehicleRemoved,
                r.vehicleReturned,
                s.id,
                s.username,
                s.name,
                s.role,
                v.id,
                v.mark,
                v.model,
                v.plate,
                v.yearVehicle,
                v.dailyValue,
                v.available
            )
            FROM RentModel r
            JOIN r.user s
            JOIN r.vehicle v
            WHERE(r.user.id = :idUser)
            """)
    List<RentResponseQueryUserVehicleDTO> findByRentIdUser(@Param("idUser") Integer id);

    @Query("""
            SELECT new com.pauloricardo.alugaVeiculos.DTOs.Rent.RentResponseQueryUserVehicleDTO(
                r.idRent,
                r.startDate,
                r.endDate,
                r.totalValue,
                r.vehicleRemoved,
                r.vehicleReturned,
                s.id,
                s.username,
                s.name,
                s.role,
                v.id,
                v.mark,
                v.model,
                v.plate,
                v.yearVehicle,
                v.dailyValue,
                v.available
            )
            FROM RentModel r
            JOIN r.user s
            JOIN r.vehicle v
            WHERE(:vehicleId IS NULL OR v.id = :vehicleId)
                AND (:plate IS NULL OR v.plate = :plate)
            """)
    List<RentResponseQueryUserVehicleDTO> findByRentIdVehicle(@Param("vehicleId") Integer id, @Param("plate") String plate);


}
