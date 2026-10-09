package com.vehiclerental.dto.response;

import com.vehiclerental.entity.enums.MaintenanceStatus;
import com.vehiclerental.entity.enums.MaintenanceType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceResponse {
    private Long id;
    private Long vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleRegistrationNumber;
    private MaintenanceType maintenanceType;
    private String description;
    private BigDecimal cost;
    private LocalDate startDate;
    private LocalDate endDate;
    private MaintenanceStatus status;
    private String notes;
    private LocalDateTime createdAt;

    public MaintenanceResponse() {
    }

    public MaintenanceResponse(Long id, Long vehicleId, String vehicleBrand, String vehicleModel, String vehicleRegistrationNumber, MaintenanceType maintenanceType, String description, BigDecimal cost, LocalDate startDate, LocalDate endDate, MaintenanceStatus status, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.cost = cost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleBrand() {
        return this.vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return this.vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleRegistrationNumber() {
        return this.vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public MaintenanceType getMaintenanceType() {
        return this.maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public MaintenanceStatus getStatus() {
        return this.status;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long vehicleId;
        private String vehicleBrand;
        private String vehicleModel;
        private String vehicleRegistrationNumber;
        private MaintenanceType maintenanceType;
        private String description;
        private BigDecimal cost;
        private LocalDate startDate;
        private LocalDate endDate;
        private MaintenanceStatus status;
        private String notes;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
            return this;
        }
        public Builder vehicleBrand(String vehicleBrand) {
            this.vehicleBrand = vehicleBrand;
            return this;
        }
        public Builder vehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
            return this;
        }
        public Builder vehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
            return this;
        }
        public Builder maintenanceType(MaintenanceType maintenanceType) {
            this.maintenanceType = maintenanceType;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder cost(BigDecimal cost) {
            this.cost = cost;
            return this;
        }
        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }
        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }
        public Builder status(MaintenanceStatus status) {
            this.status = status;
            return this;
        }
        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public MaintenanceResponse build() {
            return new MaintenanceResponse(this.id, this.vehicleId, this.vehicleBrand, this.vehicleModel, this.vehicleRegistrationNumber, this.maintenanceType, this.description, this.cost, this.startDate, this.endDate, this.status, this.notes, this.createdAt);
        }
    }
}
