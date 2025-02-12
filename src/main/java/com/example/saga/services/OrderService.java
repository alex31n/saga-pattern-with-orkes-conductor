package com.example.saga.services;

import com.example.saga.dto.CreateOrderRequest;
import com.example.saga.entity.Order;
import com.example.saga.entity.OrderItem;
import com.example.saga.repository.OrderItemRepository;
import com.example.saga.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final OrderItemRepository orderItemRepository;

  public Order createOrder(CreateOrderRequest createOrderRequest) {
    var order = Order.builder()
        .id(UUID.fromString(createOrderRequest.getOrderId()))
        .customerName(createOrderRequest.getCustomerName())
        .customerId(createOrderRequest.getCustomerId())
        .deliveryAddress(createOrderRequest.getDeliveryAddress())
        .amount(createOrderRequest.getAmount())
        .build();

    var savedOrder = orderRepository.save(order);

    if (createOrderRequest.getCustomerId() != null && !createOrderRequest.getItems().isEmpty()) {
      createOrderRequest.getItems().forEach(orderItem -> {
        var item = OrderItem.builder()
            .id(UUID.randomUUID())
            .orderId(savedOrder.getId().toString())
            .productId(orderItem.getProductId())
            .quantity(orderItem.getQuantity())
            .build();
        orderItemRepository.save(item);
      });
    }

    return savedOrder;
  }

}
