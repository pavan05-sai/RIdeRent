package com.vehiclerental.repository;

import com.vehiclerental.entity.MaintenanceRecord;
import com.vehiclerental.entity.enums.MaintenanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByVehicleIdOrderByCreatedAtDesc(Long vehicleId);
    Page<MaintenanceRecord> findByStatus(MaintenanceStatus status, Pageable pageable);
    Long countByStatus(MaintenanceStatus status);

    @Query("SELECT m FROM MaintenanceRecord m WHERE m.status IN ('SCHEDULED', 'IN_PROGRESS')")
    List<MaintenanceRecord> findActiveMaintenance();
}
