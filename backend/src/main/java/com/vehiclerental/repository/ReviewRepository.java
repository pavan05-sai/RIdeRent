package com.vehiclerental.repository;

import com.vehiclerental.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByVehicleIdAndIsHiddenFalseOrderByCreatedAtDesc(Long vehicleId);
    Page<Review> findByVehicleIdAndIsHiddenFalseOrderByCreatedAtDesc(Long vehicleId, Pageable pageable);
    boolean existsByBookingId(Long bookingId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.vehicle.id = :vehicleId AND r.isHidden = false")
    Double calculateAverageRatingForVehicle(@Param("vehicleId") Long vehicleId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.vehicle.id = :vehicleId AND r.isHidden = false")
    Long countReviewsForVehicle(@Param("vehicleId") Long vehicleId);
}
