package com.example.saga.workers;

import com.example.saga.pojo.BalanceDeductionProcess;
import com.example.saga.pojo.CreateOrder;
import com.example.saga.pojo.InventoryDeductionProcess;
import com.example.saga.pojo.ShipOrder;
import com.example.saga.pojo.ValidateBalance;
import com.example.saga.pojo.ValidateInventory;
import com.example.saga.services.AccountService;
import com.example.saga.services.InventoryService;
import com.example.saga.services.OrderService;
import com.example.saga.services.ShipmentService;
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

  private final InventoryService inventoryService;

  private final ShipmentService shipmentService;

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
      result.setReasonForIncompletion("Insufficient balance");
      result.setStatus(TaskResult.Status.FAILED);
    }
    return result;
  }

  @WorkerTask(value = "validate_inventory", threadCount = 3, pollingInterval = 300)
  public TaskResult validateInventoryTask(ValidateInventory input) {
    log.info("ConductorWorkers validate_inventory: {}", input);

    var inventoryResult = inventoryService.validateAndReserveInventory(input);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if (inventoryResult) {
      output.put("orderId", input.getOrderId());
      result.setOutputData(output);
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      result.setReasonForIncompletion("Stock is not available");
      result.setStatus(TaskResult.Status.FAILED);
    }

    result.setStatus(TaskResult.Status.COMPLETED);
    return result;
  }

  @WorkerTask(value = "balance_deduction_process", threadCount = 3, pollingInterval = 300)
  public TaskResult balanceDeductionProcessTask(BalanceDeductionProcess input) {
    log.info("ConductorWorkers balance_deduction_process: {}", input);

    boolean result = accountService.deductBalance(input);

    TaskResult taskResult = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if (result) {
      output.put("orderId", input.getOrderId());
      taskResult.setOutputData(output);
      taskResult.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      taskResult.setStatus(TaskResult.Status.FAILED);
    }

    taskResult.setStatus(TaskResult.Status.COMPLETED);
    return taskResult;
  }

  @WorkerTask(value = "inventory_deduction_process", threadCount = 3, pollingInterval = 300)
  public TaskResult inventoryDeductionProcessTask(InventoryDeductionProcess input) {
    log.info("ConductorWorkers inventory_deduction_process: {}", input);

    boolean result = inventoryService.deductInventory(input);

    TaskResult taskResult = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if (result) {
      output.put("orderId", input.getOrderId());
      taskResult.setOutputData(output);
      taskResult.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      taskResult.setStatus(TaskResult.Status.FAILED);
    }

    taskResult.setStatus(TaskResult.Status.COMPLETED);
    return taskResult;
  }

  @WorkerTask(value = "ship_order", threadCount = 3, pollingInterval = 300)
  public TaskResult shipOrderTask(ShipOrder input) {
    log.info("ConductorWorkers ship_order: {}", input);

    boolean result = shipmentService.shipOrder(input);

    TaskResult taskResult = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if (result) {
      output.put("orderId", input.getOrderId());
      taskResult.setOutputData(output);
      taskResult.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      taskResult.setStatus(TaskResult.Status.FAILED);
    }

    taskResult.setStatus(TaskResult.Status.COMPLETED);
    return taskResult;
  }



}
