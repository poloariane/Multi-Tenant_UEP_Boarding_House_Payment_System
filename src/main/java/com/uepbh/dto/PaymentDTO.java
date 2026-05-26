package com.uepbh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long tenantId;
    private String tenantName;
    private Double amount;
    private String status;
    private YearMonth month;
    private LocalDateTime paymentDate;
    private LocalDateTime dueDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
