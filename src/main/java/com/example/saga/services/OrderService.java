package com.example.saga.services;

import com.example.saga.pojo.CreateOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

  public boolean createOrder(CreateOrder createOrder) {

    log.info("Create Order");

    return Boolean.TRUE;
  }

}
