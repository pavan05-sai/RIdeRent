package com.vehiclerental.repository;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    Page<Booking> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status IN ('ACTIVE', 'CONFIRMED') " +
           "AND b.pickupDate <= :now AND b.returnDate >= :now ORDER BY b.pickupDate ASC")
    List<Booking> findCurrentActiveRental(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = 'CONFIRMED' " +
           "AND b.pickupDate > :now ORDER BY b.pickupDate ASC")
    List<Booking> findUpcomingBookings(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.vehicle.id = :vehicleId " +
           "AND b.status IN ('CONFIRMED', 'ACTIVE', 'EXTENDED') " +
           "AND (:excludeBookingId IS NULL OR b.id != :excludeBookingId) " +
           "AND NOT (b.returnDate <= :pickupDate OR b.pickupDate >= :returnDate)")
    Long countOverlappingBookings(
            @Param("vehicleId") Long vehicleId,
            @Param("pickupDate") LocalDateTime pickupDate,
            @Param("returnDate") LocalDateTime returnDate,
            @Param("excludeBookingId") Long excludeBookingId
    );

    @Query("SELECT b FROM Booking b WHERE " +
           "(:search IS NULL OR LOWER(b.bookingReference) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(b.user.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(b.vehicle.brand) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(b.vehicle.model) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR b.status = :status)")
    Page<Booking> findBookingsWithFilters(
            @Param("search") String search,
            @Param("status") BookingStatus status,
            Pageable pageable
    );

    Long countByStatus(BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.status IN ('CONFIRMED', 'ACTIVE', 'COMPLETED', 'EXTENDED')")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT FUNCTION('MONTH', b.createdAt) as month, FUNCTION('YEAR', b.createdAt) as year, SUM(b.totalAmount) as total " +
           "FROM Booking b WHERE b.status IN ('CONFIRMED', 'ACTIVE', 'COMPLETED', 'EXTENDED') " +
           "GROUP BY FUNCTION('YEAR', b.createdAt), FUNCTION('MONTH', b.createdAt) " +
           "ORDER BY year DESC, month DESC")
    List<Object[]> getMonthlyRevenue();

    @Query("SELECT b.vehicle.brand, b.vehicle.model, COUNT(b) as rentalCount " +
           "FROM Booking b WHERE b.status IN ('CONFIRMED', 'ACTIVE', 'COMPLETED', 'EXTENDED') " +
           "GROUP BY b.vehicle.id, b.vehicle.brand, b.vehicle.model " +
           "ORDER BY rentalCount DESC")
    List<Object[]> getMostRentedVehicles();

    boolean existsByUserIdAndVehicleIdAndStatus(Long userId, Long vehicleId, BookingStatus status);
}
