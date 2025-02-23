package com.example.saga.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateBalance {

  private String orderId;
  private String customerId;
  private Double amount=0.0;

}
