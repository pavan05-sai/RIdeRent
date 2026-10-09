package com.vehiclerental.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardAnalyticsResponse {
    private List<MonthlyRevenueItem> revenueByMonth;
    private List<CategoryDistributionItem> categoryDistribution;
    private List<MostRentedItem> mostRentedVehicles;
    private Map<String, Long> bookingStatusDistribution;

    public DashboardAnalyticsResponse() {
    }

    public DashboardAnalyticsResponse(List<MonthlyRevenueItem> revenueByMonth,
                                      List<CategoryDistributionItem> categoryDistribution,
                                      List<MostRentedItem> mostRentedVehicles,
                                      Map<String, Long> bookingStatusDistribution) {
        this.revenueByMonth = revenueByMonth;
        this.categoryDistribution = categoryDistribution;
        this.mostRentedVehicles = mostRentedVehicles;
        this.bookingStatusDistribution = bookingStatusDistribution;
    }

    public List<MonthlyRevenueItem> getRevenueByMonth() {
        return revenueByMonth;
    }

    public void setRevenueByMonth(List<MonthlyRevenueItem> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }

    public List<CategoryDistributionItem> getCategoryDistribution() {
        return categoryDistribution;
    }

    public void setCategoryDistribution(List<CategoryDistributionItem> categoryDistribution) {
        this.categoryDistribution = categoryDistribution;
    }

    public List<MostRentedItem> getMostRentedVehicles() {
        return mostRentedVehicles;
    }

    public void setMostRentedVehicles(List<MostRentedItem> mostRentedVehicles) {
        this.mostRentedVehicles = mostRentedVehicles;
    }

    public Map<String, Long> getBookingStatusDistribution() {
        return bookingStatusDistribution;
    }

    public void setBookingStatusDistribution(Map<String, Long> bookingStatusDistribution) {
        this.bookingStatusDistribution = bookingStatusDistribution;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<MonthlyRevenueItem> revenueByMonth;
        private List<CategoryDistributionItem> categoryDistribution;
        private List<MostRentedItem> mostRentedVehicles;
        private Map<String, Long> bookingStatusDistribution;

        public Builder revenueByMonth(List<MonthlyRevenueItem> revenueByMonth) {
            this.revenueByMonth = revenueByMonth;
            return this;
        }

        public Builder categoryDistribution(List<CategoryDistributionItem> categoryDistribution) {
            this.categoryDistribution = categoryDistribution;
            return this;
        }

        public Builder mostRentedVehicles(List<MostRentedItem> mostRentedVehicles) {
            this.mostRentedVehicles = mostRentedVehicles;
            return this;
        }

        public Builder bookingStatusDistribution(Map<String, Long> bookingStatusDistribution) {
            this.bookingStatusDistribution = bookingStatusDistribution;
            return this;
        }

        public DashboardAnalyticsResponse build() {
            return new DashboardAnalyticsResponse(revenueByMonth, categoryDistribution, mostRentedVehicles, bookingStatusDistribution);
        }
    }

    public static class MonthlyRevenueItem {
        private String month;
        private BigDecimal revenue;

        public MonthlyRevenueItem() {
        }

        public MonthlyRevenueItem(String month, BigDecimal revenue) {
            this.month = month;
            this.revenue = revenue;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    public static class CategoryDistributionItem {
        private String category;
        private Long count;

        public CategoryDistributionItem() {
        }

        public CategoryDistributionItem(String category, Long count) {
            this.category = category;
            this.count = count;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }

    public static class MostRentedItem {
        private String vehicle;
        private Long rentalCount;

        public MostRentedItem() {
        }

        public MostRentedItem(String vehicle, Long rentalCount) {
            this.vehicle = vehicle;
            this.rentalCount = rentalCount;
        }

        public String getVehicle() {
            return vehicle;
        }

        public void setVehicle(String vehicle) {
            this.vehicle = vehicle;
        }

        public Long getRentalCount() {
            return rentalCount;
        }

        public void setRentalCount(Long rentalCount) {
            this.rentalCount = rentalCount;
        }
    }
}
