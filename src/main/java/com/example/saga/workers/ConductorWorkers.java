package com.example.saga.workers;

import com.example.saga.dto.CreateOrderRequest;
import com.example.saga.entity.Order;
import com.example.saga.services.OrderService;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

@RequiredArgsConstructor
@Component
@ComponentScan(basePackages = {"io.orkes"})
public class ConductorWorkers {

  private final OrderService orderService;

  @WorkerTask(value = "create_order", threadCount = 3, pollingInterval = 300)
  public TaskResult orderFoodTask(CreateOrderRequest createOrderRequest) {
    Order order = orderService.createOrder(createOrderRequest);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if(order != null) {
      output.put("orderId", order.getId().toString());
      result.setOutputData(output);
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      result.setStatus(TaskResult.Status.FAILED);
    }
    return result;
  }

}
