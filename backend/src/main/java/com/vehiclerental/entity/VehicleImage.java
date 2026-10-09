package com.vehiclerental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
@Table(name = "vehicle_images")
public class VehicleImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnore
    private Vehicle vehicle;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isPrimary = false;

    public VehicleImage() {
    }

    public VehicleImage(Long id, Vehicle vehicle, String imageUrl, Boolean isPrimary) {
        this.id = id;
        this.vehicle = vehicle;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
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

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return this.isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    public Boolean getPrimary() {
        return this.isPrimary;
    }
    public void setPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Vehicle vehicle;
        private String imageUrl;
        private Boolean isPrimary;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder vehicle(Vehicle vehicle) {
            this.vehicle = vehicle;
            return this;
        }
        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }
        public Builder isPrimary(Boolean isPrimary) {
            this.isPrimary = isPrimary;
            return this;
        }

        public VehicleImage build() {
            return new VehicleImage(this.id, this.vehicle, this.imageUrl, this.isPrimary);
        }
    }
}
