package com.example.saga.dto;

import com.example.saga.enumeration.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
  private String orderId;
  private String workflowId;
  private String paymentId;
  private double amount;
  private PaymentMethod paymentMethod;
}