package com.example.saga.services;

import com.example.saga.dto.PaymentRequest;
import com.example.saga.entity.Payment;
import com.example.saga.enumeration.Status;
import com.example.saga.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;


  public Payment createPayment(PaymentRequest paymentRequest) {
    var order = Payment.builder()
        .id(UUID.randomUUID())
        .orderId(paymentRequest.getOrderId())
        .paymentId(paymentRequest.getPaymentId())
        .amount(paymentRequest.getAmount())
        .paymentMethod(paymentRequest.getPaymentMethod())
        .status(Status.SUCCESSFUL)
        .build();

    return paymentRepository.save(order);
  }

}
