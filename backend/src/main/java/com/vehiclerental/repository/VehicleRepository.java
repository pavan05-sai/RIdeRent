package com.vehiclerental.repository;

import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.entity.enums.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    List<Vehicle> findByFeaturedTrue();

    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:search IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(v.model) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(v.location) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:vehicleType IS NULL OR v.vehicleType = :vehicleType) AND " +
           "(:fuelType IS NULL OR LOWER(v.fuelType) = LOWER(:fuelType)) AND " +
           "(:transmission IS NULL OR LOWER(v.transmission) = LOWER(:transmission)) AND " +
           "(:location IS NULL OR LOWER(v.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:minPrice IS NULL OR v.pricePerDay >= :minPrice) AND " +
           "(:maxPrice IS NULL OR v.pricePerDay <= :maxPrice) AND " +
           "(:seats IS NULL OR v.seatingCapacity = :seats) AND " +
           "(:status IS NULL OR v.status = :status)")
    Page<Vehicle> findVehiclesWithFilters(
            @Param("search") String search,
            @Param("vehicleType") VehicleType vehicleType,
            @Param("fuelType") String fuelType,
            @Param("transmission") String transmission,
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("seats") Integer seats,
            @Param("status") VehicleStatus status,
            Pageable pageable
    );

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = :status")
    Long countByStatus(@Param("status") VehicleStatus status);

    @Query("SELECT v.vehicleType, COUNT(v) FROM Vehicle v GROUP BY v.vehicleType")
    List<Object[]> countVehiclesByCategory();

    boolean existsByRegistrationNumber(String registrationNumber);
}
