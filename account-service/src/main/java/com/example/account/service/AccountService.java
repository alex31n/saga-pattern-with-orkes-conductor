package com.example.account.service;

import com.example.account.pojo.BalanceDeductionProcess;
import com.example.account.pojo.ValidateBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountService {

  private double balancedAmount = 1000;
  private double reservedAmount = 0;

  public boolean validateAndReserveBalance(ValidateBalance request) {
    log.info("validateAndReserveBalance");

    /*try {
      // 5 minutes
      Thread.sleep(1000 * 60 * 2);
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
    }*/

    if (getAvailableBalance() >= request.getAmount()) {
      reservedAmount += request.getAmount();
      return true;
    } else {
      return false;
    }
  }

  private double getAvailableBalance() {
    return balancedAmount - reservedAmount;
  }

  public boolean deductBalance(BalanceDeductionProcess input) {
    balancedAmount -= input.getAmount();
    reservedAmount = 0;
    return true;
  }
}
