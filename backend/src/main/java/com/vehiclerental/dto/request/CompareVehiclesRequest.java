package com.vehiclerental.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CompareVehiclesRequest {
    @NotEmpty(message = "Please select vehicles to compare")
    @Size(min = 2, max = 3, message = "You can compare between 2 and 3 vehicles")
    private List<Long> vehicleIds;

    public CompareVehiclesRequest() {
    }

    public CompareVehiclesRequest(List<Long> vehicleIds) {
        this.vehicleIds = vehicleIds;
    }

    public List<Long> getVehicleIds() {
        return this.vehicleIds;
    }

    public void setVehicleIds(List<Long> vehicleIds) {
        this.vehicleIds = vehicleIds;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Long> vehicleIds;

        public Builder vehicleIds(List<Long> vehicleIds) {
            this.vehicleIds = vehicleIds;
            return this;
        }

        public CompareVehiclesRequest build() {
            return new CompareVehiclesRequest(this.vehicleIds);
        }
    }
}
