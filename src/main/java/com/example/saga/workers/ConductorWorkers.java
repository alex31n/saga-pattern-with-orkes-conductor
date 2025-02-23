package com.example.saga.workers;

import com.example.saga.pojo.CreateOrder;
import com.example.saga.pojo.ValidateBalance;
import com.example.saga.services.AccountService;
import com.example.saga.services.OrderService;
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

  private final AccountService accountService;

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

  @WorkerTask(value = "validate_balance", threadCount = 3, pollingInterval = 300)
  public TaskResult validateBalanceTask(ValidateBalance input) {
    log.info("ConductorWorkers validate_balance: {}", input);

    var balanceResult = accountService.validateAndReserveBalance(input);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if (balanceResult) {
      output.put("orderId", input.getOrderId());
      result.setOutputData(output);
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      result.setStatus(TaskResult.Status.FAILED);
    }
    return result;
  }

}
