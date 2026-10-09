package com.vehiclerental.repository;

import com.vehiclerental.entity.Payment;
import com.vehiclerental.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    Optional<Payment> findByTransactionReference(String transactionReference);
    List<Payment> findByBookingIdOrderByCreatedAtDesc(Long bookingId);
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
}
