package com.example.saga.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDeductionProcess {

  private String orderId;
  private OrderItem item;
  private Double amount=0.0;

}
