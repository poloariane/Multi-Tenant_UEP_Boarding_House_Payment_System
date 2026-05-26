package com.uepbh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusDTO {
    private Long tenantId;
    private String tenantName;
    private String status;
    private Double amount;
    private String dueDate;
}
