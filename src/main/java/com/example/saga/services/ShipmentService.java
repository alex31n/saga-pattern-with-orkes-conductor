package com.example.saga.services;

import com.example.saga.pojo.ShipOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ShipmentService {


  public boolean shipOrder(ShipOrder input) {
    log.info("ConductorWorkers ship_order: {}", input);
    return true;
  }
}
