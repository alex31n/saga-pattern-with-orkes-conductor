package com.example.saga.services;

import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import io.orkes.conductor.client.WorkflowClient;
import com.example.saga.dto.OrderRequestDto;
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

  public Map<String, Object> startOrderWorkflow(OrderRequestDto orderRequest) {

    var orderId = UUID.randomUUID().toString();
    orderRequest.setOrderId(orderId);

    StartWorkflowRequest workflowRequest = new StartWorkflowRequest();
    workflowRequest.setName("DemoOrderWorkflow");
    workflowRequest.setVersion(1);
    workflowRequest.setCorrelationId(orderId);

    if (taskDomain != null) {
      Map<String, String> taskToDomain = new HashMap<>();
      taskToDomain.put("*", taskDomain);
      workflowRequest.setTaskToDomain(taskToDomain);
    }

    Map<String, Object> inputData = new HashMap<>();
    inputData.put("orderId", orderRequest.getOrderId());
    inputData.put("customerName", orderRequest.getCustomerName());
    inputData.put("customerId", orderRequest.getCustomerId());
    inputData.put("deliveryAddress", orderRequest.getDeliveryAddress());
    inputData.put("amount", orderRequest.getAmount());
    inputData.put("items", orderRequest.getItems());

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
