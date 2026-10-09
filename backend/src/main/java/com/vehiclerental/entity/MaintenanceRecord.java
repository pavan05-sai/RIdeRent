package com.vehiclerental.entity;

import com.vehiclerental.entity.enums.MaintenanceStatus;
import com.vehiclerental.entity.enums.MaintenanceType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MaintenanceType maintenanceType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MaintenanceStatus status = MaintenanceStatus.SCHEDULED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public MaintenanceRecord() {
    }

    public MaintenanceRecord(Long id, Vehicle vehicle, MaintenanceType maintenanceType, String description, BigDecimal cost, LocalDate startDate, LocalDate endDate, MaintenanceStatus status, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.vehicle = vehicle;
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

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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
        private Vehicle vehicle;
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
        public Builder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
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

        public MaintenanceRecord build() {
            return new MaintenanceRecord(this.id, this.vehicle, this.maintenanceType, this.description, this.cost, this.startDate, this.endDate, this.status, this.notes, this.createdAt);
        }
    }
}
