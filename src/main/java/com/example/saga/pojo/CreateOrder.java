package com.example.saga.pojo;

import java.util.List;
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
  private List<OrderItem> items;


  @Data
  public static class OrderItem{
    private String productId;
    private Integer quantity;
  }

}
