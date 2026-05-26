package com.uepbh.repository;

import com.uepbh.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOwnerId(String ownerId);
    List<Payment> findByOwnerIdAndStatus(String ownerId, Payment.PaymentStatus status);
    List<Payment> findByTenantId(Long tenantId);
    Optional<Payment> findByIdAndOwnerId(Long id, String ownerId);
    List<Payment> findByTenantIdAndMonth(Long tenantId, YearMonth month);
}
