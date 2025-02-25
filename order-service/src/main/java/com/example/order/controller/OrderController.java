package com.example.order.controller;

import com.example.order.dto.OrderRequestDto;
import com.example.order.service.WorkflowService;
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
  public ResponseEntity<Map<String, Object>> triggerFoodDeliveryFlow(@RequestBody OrderRequestDto requestDto) {
    return ResponseEntity.ok(workflowService.startOrderWorkflow(requestDto));
  }


}
