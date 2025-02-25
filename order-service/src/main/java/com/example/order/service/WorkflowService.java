package com.example.order.service;

import com.example.order.dto.OrderRequestDto;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class WorkflowService {

  @Value("${conductor.worker.all.domain}")
  private String taskDomain;

  private final WorkflowClient workflowClient;

  private static final String workflowName = "order_workflow";

  public Map<String, Object> startOrderWorkflow(OrderRequestDto requestDto) {

    var orderId = UUID.randomUUID().toString();

    StartWorkflowRequest workflowRequest = new StartWorkflowRequest();
    workflowRequest.setName(workflowName);
//    workflowRequest.setVersion(1);
    workflowRequest.setCorrelationId(orderId);

    if (taskDomain != null) {
      Map<String, String> taskToDomain = new HashMap<>();
      taskToDomain.put("*", taskDomain);
      workflowRequest.setTaskToDomain(taskToDomain);
    }

    Map<String, Object> inputData = new HashMap<>();
    inputData.put("orderId", orderId);
    inputData.put("customerId", requestDto.getCustomerId());
    inputData.put("deliveryAddress", requestDto.getDeliveryAddress());
    inputData.put("amount", requestDto.getAmount());
    inputData.put("item", requestDto.getItem());

    workflowRequest.setInput(inputData);

    String workflowId = "";
    try {
      workflowId = workflowClient.startWorkflow(workflowRequest);
      log.info("Workflow id: {}", workflowId);
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
      return Map.of("error", "Order creation failure", "detail", ex.toString());
    }

    return Map.of("workflowId", workflowId);
  }


}
