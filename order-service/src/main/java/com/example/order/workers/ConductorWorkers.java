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
  public TaskResult createOrderTask(CreateOrder input) {
    log.info("ConductorWorkers create_order: {}", input);
    var orderResult = orderService.createOrder(input);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();
    output.put("orderId", input.getOrderId());
    result.setOutputData(output);

    if (orderResult) {
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      result.setStatus(TaskResult.Status.FAILED);
    }
    return result;
  }


}
