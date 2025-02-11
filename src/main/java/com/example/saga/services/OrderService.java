package com.example.saga.services;

import com.example.saga.dto.CreateOrderRequest;
import com.example.saga.entity.Order;
import com.example.saga.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public Order createOrder(CreateOrderRequest createOrderRequest) {
    var order = Order.builder()
        .id(UUID.randomUUID())
        .customerName(createOrderRequest.getCustomerName())
        .customerId(createOrderRequest.getCustomerId())
        .deliveryAddress(createOrderRequest.getDeliveryAddress())
        .amount(createOrderRequest.getAmount())
        .build();
    return orderRepository.save(order);
  }

}
