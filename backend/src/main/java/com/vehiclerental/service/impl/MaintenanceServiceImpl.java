package com.vehiclerental.service.impl;

import com.vehiclerental.dto.request.MaintenanceCreateRequest;
import com.vehiclerental.dto.response.MaintenanceResponse;
import com.vehiclerental.entity.AvailabilityAlert;
import com.vehiclerental.entity.MaintenanceRecord;
import com.vehiclerental.entity.Notification;
import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.entity.enums.MaintenanceStatus;
import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.AvailabilityAlertRepository;
import com.vehiclerental.repository.MaintenanceRecordRepository;
import com.vehiclerental.repository.NotificationRepository;
import com.vehiclerental.repository.VehicleRepository;
import com.vehiclerental.service.MaintenanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRecordRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;
    private final AvailabilityAlertRepository alertRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public MaintenanceResponse createMaintenanceRecord(MaintenanceCreateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVehicleId()));

        MaintenanceRecord record = MaintenanceRecord.builder()
                .vehicle(vehicle)
                .maintenanceType(request.getMaintenanceType())
                .description(request.getDescription().trim())
                .cost(request.getCost())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(MaintenanceStatus.IN_PROGRESS)
                .notes(request.getNotes())
                .build();

        MaintenanceRecord saved = maintenanceRepository.save(record);

        // Put vehicle into MAINTENANCE status
        vehicle.setStatus(VehicleStatus.MAINTENANCE);
        vehicleRepository.save(vehicle);

        return mapToMaintenanceResponse(saved);
    }

    @Override
    @Transactional
    public MaintenanceResponse completeMaintenance(Long id, String notes) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found: " + id));

        record.setStatus(MaintenanceStatus.COMPLETED);
        record.setEndDate(LocalDate.now());
        if (notes != null && !notes.isBlank()) {
            record.setNotes(record.getNotes() != null ? record.getNotes() + " | " + notes.trim() : notes.trim());
        }

        MaintenanceRecord saved = maintenanceRepository.save(record);

        // Restore vehicle status to AVAILABLE
        Vehicle vehicle = record.getVehicle();
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);

        // Notify all users subscribed to availability alert for this vehicle!
        List<AvailabilityAlert> pendingAlerts = alertRepository.findByVehicleIdAndNotifiedFalse(vehicle.getId());
        for (AvailabilityAlert alert : pendingAlerts) {
            notificationRepository.save(Notification.builder()
                    .user(alert.getUser())
                    .title("Vehicle Available: " + vehicle.getBrand() + " " + vehicle.getModel())
                    .message("The vehicle you were interested in is now available for rent! Book before it gets reserved.")
                    .type("AVAILABILITY")
                    .isRead(false)
                    .build());
            alert.setNotified(true);
            alertRepository.save(alert);
        }

        return mapToMaintenanceResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getMaintenanceForVehicle(Long vehicleId) {
        return maintenanceRepository.findByVehicleIdOrderByCreatedAtDesc(vehicleId).stream()
                .map(this::mapToMaintenanceResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceResponse> getAllMaintenance(MaintenanceStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (status != null) {
            return maintenanceRepository.findByStatus(status, pageable).map(this::mapToMaintenanceResponse);
        }
        return maintenanceRepository.findAll(pageable).map(this::mapToMaintenanceResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getActiveMaintenance() {
        return maintenanceRepository.findActiveMaintenance().stream()
                .map(this::mapToMaintenanceResponse)
                .collect(Collectors.toList());
    }

    private MaintenanceResponse mapToMaintenanceResponse(MaintenanceRecord record) {
        return MaintenanceResponse.builder()
                .id(record.getId())
                .vehicleId(record.getVehicle().getId())
                .vehicleBrand(record.getVehicle().getBrand())
                .vehicleModel(record.getVehicle().getModel())
                .vehicleRegistrationNumber(record.getVehicle().getRegistrationNumber())
                .maintenanceType(record.getMaintenanceType())
                .description(record.getDescription())
                .cost(record.getCost())
                .startDate(record.getStartDate())
                .endDate(record.getEndDate())
                .status(record.getStatus())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }

    public MaintenanceServiceImpl(MaintenanceRecordRepository maintenanceRepository, VehicleRepository vehicleRepository, AvailabilityAlertRepository alertRepository, NotificationRepository notificationRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.vehicleRepository = vehicleRepository;
        this.alertRepository = alertRepository;
        this.notificationRepository = notificationRepository;
    }
}
