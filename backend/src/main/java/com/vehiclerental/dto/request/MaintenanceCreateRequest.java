package com.vehiclerental.dto.request;

import com.vehiclerental.entity.enums.MaintenanceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MaintenanceCreateRequest {
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotNull(message = "Maintenance type is required")
    private MaintenanceType maintenanceType;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", message = "Cost cannot be negative")
    private BigDecimal cost;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;
    private String notes;

    public MaintenanceCreateRequest() {
    }

    public MaintenanceCreateRequest(Long vehicleId, MaintenanceType maintenanceType, String description, BigDecimal cost, LocalDate startDate, LocalDate endDate, String notes) {
        this.vehicleId = vehicleId;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.cost = cost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long vehicleId;
        private MaintenanceType maintenanceType;
        private String description;
        private BigDecimal cost;
        private LocalDate startDate;
        private LocalDate endDate;
        private String notes;

        public Builder vehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
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
        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public MaintenanceCreateRequest build() {
            return new MaintenanceCreateRequest(this.vehicleId, this.maintenanceType, this.description, this.cost, this.startDate, this.endDate, this.notes);
        }
    }
}
