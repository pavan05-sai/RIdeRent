package com.vehiclerental.dto.request;

import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.entity.enums.VehicleType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class VehicleRequest {
    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    private String description;

    @NotNull(message = "Price per day is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per day must be positive")
    private BigDecimal pricePerDay;

    @NotNull(message = "Security deposit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Security deposit cannot be negative")
    private BigDecimal securityDeposit;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;

    @NotBlank(message = "Transmission is required")
    private String transmission;

    @NotNull(message = "Seating capacity is required")
    private Integer seatingCapacity;

    @NotBlank(message = "Location is required")
    private String location;

    private VehicleStatus status;
    private Boolean featured;
    private List<String> imageUrls;

    public VehicleRequest() {
    }

    public VehicleRequest(String brand, String model, Integer year, VehicleType vehicleType, String registrationNumber, String description, BigDecimal pricePerDay, BigDecimal securityDeposit, String fuelType, String transmission, Integer seatingCapacity, String location, VehicleStatus status, Boolean featured, List<String> imageUrls) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.vehicleType = vehicleType;
        this.registrationNumber = registrationNumber;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.securityDeposit = securityDeposit;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.location = location;
        this.status = status;
        this.featured = featured;
        this.imageUrls = imageUrls;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPricePerDay() {
        return this.pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public BigDecimal getSecurityDeposit() {
        return this.securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getFuelType() {
        return this.fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return this.transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Integer getSeatingCapacity() {
        return this.seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public VehicleStatus getStatus() {
        return this.status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public Boolean getFeatured() {
        return this.featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }
    public Boolean isFeatured() {
        return this.featured;
    }

    public List<String> getImageUrls() {
        return this.imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String brand;
        private String model;
        private Integer year;
        private VehicleType vehicleType;
        private String registrationNumber;
        private String description;
        private BigDecimal pricePerDay;
        private BigDecimal securityDeposit;
        private String fuelType;
        private String transmission;
        private Integer seatingCapacity;
        private String location;
        private VehicleStatus status;
        private Boolean featured;
        private List<String> imageUrls;

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }
        public Builder model(String model) {
            this.model = model;
            return this;
        }
        public Builder year(Integer year) {
            this.year = year;
            return this;
        }
        public Builder vehicleType(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
            return this;
        }
        public Builder registrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder pricePerDay(BigDecimal pricePerDay) {
            this.pricePerDay = pricePerDay;
            return this;
        }
        public Builder securityDeposit(BigDecimal securityDeposit) {
            this.securityDeposit = securityDeposit;
            return this;
        }
        public Builder fuelType(String fuelType) {
            this.fuelType = fuelType;
            return this;
        }
        public Builder transmission(String transmission) {
            this.transmission = transmission;
            return this;
        }
        public Builder seatingCapacity(Integer seatingCapacity) {
            this.seatingCapacity = seatingCapacity;
            return this;
        }
        public Builder location(String location) {
            this.location = location;
            return this;
        }
        public Builder status(VehicleStatus status) {
            this.status = status;
            return this;
        }
        public Builder featured(Boolean featured) {
            this.featured = featured;
            return this;
        }
        public Builder imageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
            return this;
        }

        public VehicleRequest build() {
            return new VehicleRequest(this.brand, this.model, this.year, this.vehicleType, this.registrationNumber, this.description, this.pricePerDay, this.securityDeposit, this.fuelType, this.transmission, this.seatingCapacity, this.location, this.status, this.featured, this.imageUrls);
        }
    }
}
