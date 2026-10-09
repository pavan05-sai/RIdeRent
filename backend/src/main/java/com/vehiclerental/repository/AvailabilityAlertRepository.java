package com.vehiclerental.repository;

import com.vehiclerental.entity.AvailabilityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityAlertRepository extends JpaRepository<AvailabilityAlert, Long> {
    List<AvailabilityAlert> findByVehicleIdAndNotifiedFalse(Long vehicleId);
    boolean existsByVehicleIdAndUserIdAndNotifiedFalse(Long vehicleId, Long userId);
}
