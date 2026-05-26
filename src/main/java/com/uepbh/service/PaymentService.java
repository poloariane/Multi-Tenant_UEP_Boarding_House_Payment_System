package com.uepbh.service;

import com.uepbh.dto.PaymentDTO;
import com.uepbh.dto.PaymentStatusDTO;
import com.uepbh.entity.Payment;
import com.uepbh.entity.Tenant;
import com.uepbh.repository.PaymentRepository;
import com.uepbh.repository.TenantRepository;
import com.uepbh.util.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TenantRepository tenantRepository;

    public PaymentDTO recordPayment(PaymentDTO paymentDTO) {
        String ownerId = TenantContext.getCurrentTenant();
        Tenant tenant = tenantRepository.findById(paymentDTO.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Payment payment = new Payment();
        payment.setOwnerId(ownerId);
        payment.setTenant(tenant);
        payment.setAmount(paymentDTO.getAmount());
        payment.setStatus(Payment.PaymentStatus.valueOf(paymentDTO.getStatus()));
        payment.setMonth(paymentDTO.getMonth());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setDueDate(paymentDTO.getDueDate());
        payment.setNotes(paymentDTO.getNotes());

        Payment saved = paymentRepository.save(payment);
        return convertToDTO(saved);
    }

    public List<PaymentDTO> getAllPayments() {
        String ownerId = TenantContext.getCurrentTenant();
        return paymentRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentStatusDTO> getPaymentStatus() {
        String ownerId = TenantContext.getCurrentTenant();
        return paymentRepository.findByOwnerId(ownerId)
                .stream()
                .map(payment -> new PaymentStatusDTO(
                        payment.getTenant().getId(),
                        payment.getTenant().getFirstName() + " " + payment.getTenant().getLastName(),
                        payment.getStatus().toString(),
                        payment.getAmount(),
                        payment.getDueDate() != null ? payment.getDueDate().toString() : ""
                ))
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getTenantPayments(Long tenantId) {
        return paymentRepository.findByTenantId(tenantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        String ownerId = TenantContext.getCurrentTenant();
        Payment payment = paymentRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(paymentDTO.getAmount());
        payment.setStatus(Payment.PaymentStatus.valueOf(paymentDTO.getStatus()));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setNotes(paymentDTO.getNotes());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment updated = paymentRepository.save(payment);
        return convertToDTO(updated);
    }

    public List<PaymentDTO> getUnpaidPayments() {
        String ownerId = TenantContext.getCurrentTenant();
        return paymentRepository.findByOwnerIdAndStatus(ownerId, Payment.PaymentStatus.UNPAID)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getOverduePayments() {
        String ownerId = TenantContext.getCurrentTenant();
        return paymentRepository.findByOwnerIdAndStatus(ownerId, Payment.PaymentStatus.OVERDUE)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTenantId(payment.getTenant().getId());
        dto.setTenantName(payment.getTenant().getFirstName() + " " + payment.getTenant().getLastName());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus().toString());
        dto.setMonth(payment.getMonth());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setDueDate(payment.getDueDate());
        dto.setNotes(payment.getNotes());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}
