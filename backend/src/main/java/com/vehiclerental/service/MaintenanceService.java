package com.vehiclerental.service;

import com.vehiclerental.dto.request.MaintenanceCreateRequest;
import com.vehiclerental.dto.response.MaintenanceResponse;
import com.vehiclerental.entity.enums.MaintenanceStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaintenanceService {
    MaintenanceResponse createMaintenanceRecord(MaintenanceCreateRequest request);
    MaintenanceResponse completeMaintenance(Long id, String notes);
    List<MaintenanceResponse> getMaintenanceForVehicle(Long vehicleId);
    Page<MaintenanceResponse> getAllMaintenance(MaintenanceStatus status, int page, int size);
    List<MaintenanceResponse> getActiveMaintenance();
}
