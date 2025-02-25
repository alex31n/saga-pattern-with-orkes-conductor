package com.example.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrder {

  private String orderId;
  private String customerId;
  private String deliveryAddress;
  private Double amount;
  private OrderItem item;


}
