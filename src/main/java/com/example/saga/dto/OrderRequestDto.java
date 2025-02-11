package com.example.saga.dto;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

  private String orderId;
  private String customerName;
  private String customerId;
  private String deliveryAddress;
  private Double amount;
  private ArrayList<ProductItemDto> items;

}
