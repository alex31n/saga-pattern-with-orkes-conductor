package com.example.saga.services;

import com.example.saga.pojo.InventoryDeductionProcess;
import com.example.saga.pojo.ValidateInventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class InventoryService {

  private int itemQuantity = 10;
  private int reservedQuantity = 0;

  public boolean validateAndReserveInventory(ValidateInventory request) {
    log.info("validateAndReserveInventory");

    if (getAvailableQuantity() >= request.getItem().getQuantity()) {
      reservedQuantity += request.getItem().getQuantity();
      return true;
    } else {
      return false;
    }

  }

  private double getAvailableQuantity() {
    return itemQuantity - reservedQuantity;
  }

  public boolean deductInventory(InventoryDeductionProcess input) {
    itemQuantity -= input.getItem().getQuantity();
    reservedQuantity =0;
    return true;
  }
}
