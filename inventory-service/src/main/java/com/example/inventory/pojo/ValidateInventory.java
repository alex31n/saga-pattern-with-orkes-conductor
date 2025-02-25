package com.example.inventory.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateInventory {

  private String orderId;
  private OrderItem item;
  private Double amount=0.0;

}
