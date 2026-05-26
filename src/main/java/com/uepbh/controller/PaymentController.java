package com.uepbh.controller;

import com.uepbh.dto.PaymentDTO;
import com.uepbh.dto.PaymentStatusDTO;
import com.uepbh.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> recordPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO saved = paymentService.recordPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status")
    public ResponseEntity<List<PaymentStatusDTO>> getPaymentStatus() {
        List<PaymentStatusDTO> statuses = paymentService.getPaymentStatus();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<PaymentDTO>> getTenantPayments(@PathVariable Long tenantId) {
        List<PaymentDTO> payments = paymentService.getTenantPayments(tenantId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<PaymentDTO>> getUnpaidPayments() {
        List<PaymentDTO> payments = paymentService.getUnpaidPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<PaymentDTO>> getOverduePayments() {
        List<PaymentDTO> payments = paymentService.getOverduePayments();
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updated = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updated);
    }
}
