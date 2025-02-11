package com.example.saga.controller;

import com.example.saga.dto.CreateOrderRequest;
import com.example.saga.dto.OrderRequestDto;
import com.example.saga.services.WorkflowService;
import com.example.saga.workers.ConductorWorkers;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final WorkflowService workflowService;

  @PostMapping(value = "/trigger-order-flow", produces = "application/json")
  public ResponseEntity<Map<String, Object>> triggerFoodDeliveryFlow(@RequestBody OrderRequestDto foodDeliveryRequest) {
    return ResponseEntity.ok(workflowService.startOrderWorkflow(foodDeliveryRequest));
  }

}
