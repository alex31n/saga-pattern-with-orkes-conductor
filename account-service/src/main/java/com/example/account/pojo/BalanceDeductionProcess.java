package com.example.account.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDeductionProcess {

  private String orderId;
  private String customerId;
  private Double amount=0.0;

}
