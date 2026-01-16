package com.pauloricardo.alugaVeiculos.Repositorys.Vehicles;

import com.pauloricardo.alugaVeiculos.Models.Vehicles.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleModel, Integer> {

    //Criando um FILTRO SQL para buscar  por nome, Placa e Nome parecido
    //Utilizando Query

    @Query("""
            SELECT v FROM VehicleModel v
            WHERE (:name IS NULL OR
                    LOWER(v.mark) LIKE LOWER (CONCAT('%', :name,'%')) OR
                    LOWER(v.model) LIKE LOWER (CONCAT('%', :name ,'%')))
            AND(:plate IS NULL OR
                  LOWER(v.plate) LIKE LOWER (CONCAT('%', :plate ,'%')))
            """)
    List<VehicleModel> findByNameAndPlate(
            @Param("name") String name,
            @Param("plate") String plate
    );


    @Query("SELECT v FROM VehicleModel v WHERE v.available = true")
    List<VehicleModel> findByVehiclesAvailableTrue();


}
