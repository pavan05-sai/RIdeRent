package com.vehiclerental.dto.response;

import java.math.BigDecimal;

public class DashboardStatsResponse {
    private Long totalUsers;
    private Long totalVehicles;
    private Long activeRentals;
    private Long upcomingBookings;
    private Long completedRentals;
    private Long cancelledRentals;
    private BigDecimal totalRevenue;
    private Long vehiclesUnderMaintenance;

    public DashboardStatsResponse() {
    }

    public DashboardStatsResponse(Long totalUsers, Long totalVehicles, Long activeRentals, Long upcomingBookings, Long completedRentals, Long cancelledRentals, BigDecimal totalRevenue, Long vehiclesUnderMaintenance) {
        this.totalUsers = totalUsers;
        this.totalVehicles = totalVehicles;
        this.activeRentals = activeRentals;
        this.upcomingBookings = upcomingBookings;
        this.completedRentals = completedRentals;
        this.cancelledRentals = cancelledRentals;
        this.totalRevenue = totalRevenue;
        this.vehiclesUnderMaintenance = vehiclesUnderMaintenance;
    }

    public Long getTotalUsers() {
        return this.totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalVehicles() {
        return this.totalVehicles;
    }

    public void setTotalVehicles(Long totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public Long getActiveRentals() {
        return this.activeRentals;
    }

    public void setActiveRentals(Long activeRentals) {
        this.activeRentals = activeRentals;
    }

    public Long getUpcomingBookings() {
        return this.upcomingBookings;
    }

    public void setUpcomingBookings(Long upcomingBookings) {
        this.upcomingBookings = upcomingBookings;
    }

    public Long getCompletedRentals() {
        return this.completedRentals;
    }

    public void setCompletedRentals(Long completedRentals) {
        this.completedRentals = completedRentals;
    }

    public Long getCancelledRentals() {
        return this.cancelledRentals;
    }

    public void setCancelledRentals(Long cancelledRentals) {
        this.cancelledRentals = cancelledRentals;
    }

    public BigDecimal getTotalRevenue() {
        return this.totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getVehiclesUnderMaintenance() {
        return this.vehiclesUnderMaintenance;
    }

    public void setVehiclesUnderMaintenance(Long vehiclesUnderMaintenance) {
        this.vehiclesUnderMaintenance = vehiclesUnderMaintenance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalUsers;
        private Long totalVehicles;
        private Long activeRentals;
        private Long upcomingBookings;
        private Long completedRentals;
        private Long cancelledRentals;
        private BigDecimal totalRevenue;
        private Long vehiclesUnderMaintenance;

        public Builder totalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
            return this;
        }
        public Builder totalVehicles(Long totalVehicles) {
            this.totalVehicles = totalVehicles;
            return this;
        }
        public Builder activeRentals(Long activeRentals) {
            this.activeRentals = activeRentals;
            return this;
        }
        public Builder upcomingBookings(Long upcomingBookings) {
            this.upcomingBookings = upcomingBookings;
            return this;
        }
        public Builder completedRentals(Long completedRentals) {
            this.completedRentals = completedRentals;
            return this;
        }
        public Builder cancelledRentals(Long cancelledRentals) {
            this.cancelledRentals = cancelledRentals;
            return this;
        }
        public Builder totalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
            return this;
        }
        public Builder vehiclesUnderMaintenance(Long vehiclesUnderMaintenance) {
            this.vehiclesUnderMaintenance = vehiclesUnderMaintenance;
            return this;
        }

        public DashboardStatsResponse build() {
            return new DashboardStatsResponse(this.totalUsers, this.totalVehicles, this.activeRentals, this.upcomingBookings, this.completedRentals, this.cancelledRentals, this.totalRevenue, this.vehiclesUnderMaintenance);
        }
    }
}
