package com.example.saga.services;

import com.example.saga.dto.PaymentRequest;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult.Status;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import com.netflix.conductor.common.run.Workflow;
import com.example.saga.dto.OrderRequestDto;
import io.orkes.conductor.client.http.OrkesTaskClient;
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

  private final OrkesTaskClient taskClient;

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
    inputData.put("items", requestDto.getItems());

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

  public Map<String, Object> payment(PaymentRequest request) {

    Workflow workflow = workflowClient.getWorkflow(request.getWorkflowId(), true);
//    workflow.setCorrelationId(request.getOrderId());

    // Retrieve the human task by task ID
    Task paymentTask = workflow.getTaskByRefName("make_payment_ref");

    /*if (paymentTask != null && paymentTask.getStatus() == Task.Status.IN_PROGRESS) {
      TaskResult taskResult = new TaskResult();
      taskResult.setTaskId(paymentTask.getTaskId());
      taskResult.setStatus(TaskResult.Status.COMPLETED);
      taskResult.setOutputData(new HashMap<>());
      taskResult.setWorkflowInstanceId(workflow.getWorkflowId());


      taskClient.updateTask(taskResult);
    } else {
      System.out.println("The task is either already completed or not found.");
    }*/

    Map<String, Object> inputData = new HashMap<>();
    inputData.put("orderId", request.getOrderId());
    inputData.put("paymentId", request.getPaymentId());
    inputData.put("amount", request.getAmount());
    inputData.put("paymentMethod", request.getPaymentMethod());
//    paymentTask.setInputData(inputData);

//    paymentTask.setStatus(Task.Status.COMPLETED);

    // Update the task
    taskClient.updateTask(request.getWorkflowId(),"human_ref", Status.FAILED,inputData);

    return Map.of("workflowId", workflow.getWorkflowId());
  }

}
