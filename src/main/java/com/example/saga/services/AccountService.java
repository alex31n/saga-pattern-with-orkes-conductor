package com.example.saga.services;

import com.example.saga.pojo.ValidateBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountService {

  private double balancedAmount=1000;
  private double reservedAmount=0;

  public boolean validateAndReserveBalance(ValidateBalance request){
    log.info("validateAndReserveBalance");
    if (getAvailableBalance()>=request.getAmount()){
      reservedAmount+=request.getAmount();
      return true;
    }else {
      return false;
    }
  }

  private double getAvailableBalance(){
    return balancedAmount-reservedAmount;
  }

}
