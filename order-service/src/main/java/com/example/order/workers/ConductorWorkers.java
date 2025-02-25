package com.example.order.workers;

import com.example.order.pojo.CreateOrder;
import com.example.order.service.OrderService;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

@Log4j2
@RequiredArgsConstructor
@Component
@ComponentScan(basePackages = {"io.orkes"})
public class ConductorWorkers {

  private final OrderService orderService;

  @WorkerTask(value = "create_order", threadCount = 3, pollingInterval = 300)
  public TaskResult createOrderTask(CreateOrder createOrder) {
    log.info("ConductorWorkers create_order: {}", createOrder);
    var orderResult = orderService.createOrder(createOrder);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if (orderResult) {
      output.put("orderId", createOrder.getOrderId());
      result.setOutputData(output);
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      result.setStatus(TaskResult.Status.FAILED);
    }
    return result;
  }


  @WorkerTask(value = "json_transform_task", threadCount = 3, pollingInterval = 300)
  public TaskResult json_transform_taskTask(Map<String, Object> input) {
    log.info("ConductorWorkers json_transform_task: {}", input);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    result.setStatus(TaskResult.Status.FAILED);
    return result;
  }




}
