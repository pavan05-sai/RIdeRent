package com.vehiclerental.dto.response;

import com.vehiclerental.entity.enums.VehicleStatus;
import com.vehiclerental.entity.enums.VehicleType;
import java.math.BigDecimal;
import java.util.List;

public class VehicleResponse {
    private Long id;
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
    private BigDecimal rating;
    private Integer totalReviews;
    private Boolean featured;
    private String primaryImageUrl;
    private List<String> imageUrls;

    public VehicleResponse() {
    }

    public VehicleResponse(Long id, String brand, String model, Integer year, VehicleType vehicleType, String registrationNumber, String description, BigDecimal pricePerDay, BigDecimal securityDeposit, String fuelType, String transmission, Integer seatingCapacity, String location, VehicleStatus status, BigDecimal rating, Integer totalReviews, Boolean featured, String primaryImageUrl, List<String> imageUrls) {
        this.id = id;
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
        this.rating = rating;
        this.totalReviews = totalReviews;
        this.featured = featured;
        this.primaryImageUrl = primaryImageUrl;
        this.imageUrls = imageUrls;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getRating() {
        return this.rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getTotalReviews() {
        return this.totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
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

    public String getPrimaryImageUrl() {
        return this.primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
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
        private Long id;
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
        private BigDecimal rating;
        private Integer totalReviews;
        private Boolean featured;
        private String primaryImageUrl;
        private List<String> imageUrls;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
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
        public Builder rating(BigDecimal rating) {
            this.rating = rating;
            return this;
        }
        public Builder totalReviews(Integer totalReviews) {
            this.totalReviews = totalReviews;
            return this;
        }
        public Builder featured(Boolean featured) {
            this.featured = featured;
            return this;
        }
        public Builder primaryImageUrl(String primaryImageUrl) {
            this.primaryImageUrl = primaryImageUrl;
            return this;
        }
        public Builder imageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
            return this;
        }

        public VehicleResponse build() {
            return new VehicleResponse(this.id, this.brand, this.model, this.year, this.vehicleType, this.registrationNumber, this.description, this.pricePerDay, this.securityDeposit, this.fuelType, this.transmission, this.seatingCapacity, this.location, this.status, this.rating, this.totalReviews, this.featured, this.primaryImageUrl, this.imageUrls);
        }
    }
}
