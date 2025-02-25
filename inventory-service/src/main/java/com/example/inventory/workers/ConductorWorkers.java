package com.example.inventory.workers;

import com.example.inventory.pojo.FailedValidatingInventory;
import com.example.inventory.pojo.InventoryDeductionProcess;
import com.example.inventory.pojo.ValidateInventory;
import com.example.inventory.service.InventoryService;
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

  private final InventoryService inventoryService;

  @WorkerTask(value = "validate_inventory", threadCount = 3, pollingInterval = 300)
  public TaskResult validateInventoryTask(ValidateInventory input) {
    log.info("ConductorWorkers validate_inventory: {}", input);

    var inventoryResult = inventoryService.validateAndReserveInventory(input);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();
    output.put("orderId", input.getOrderId());
    result.setOutputData(output);

    if (inventoryResult) {
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      result.setReasonForIncompletion("Stock is not available");
      result.setStatus(TaskResult.Status.FAILED);
    }


    return result;
  }


  @WorkerTask(value = "inventory_deduction_process", threadCount = 3, pollingInterval = 300)
  public TaskResult inventoryDeductionProcessTask(InventoryDeductionProcess input) {
    log.info("ConductorWorkers inventory_deduction_process: {}", input);

    boolean result = inventoryService.deductInventory(input);

    TaskResult taskResult = new TaskResult();
    Map<String, Object> output = new HashMap<>();
    output.put("orderId", input.getOrderId());
    taskResult.setOutputData(output);

    if (result) {
      taskResult.setStatus(TaskResult.Status.COMPLETED);
    } else {
      taskResult.setStatus(TaskResult.Status.FAILED);
    }

    return taskResult;
  }

  @WorkerTask(value = "failed_validating_inventory", threadCount = 3, pollingInterval = 300)
  public TaskResult failedValidatingInventoryTask(FailedValidatingInventory input) {
    log.info("ConductorWorkers failed_validating_inventory: {}", input);

    boolean result = inventoryService.fallbackReserveInventory(input);

    TaskResult taskResult = new TaskResult();
    Map<String, Object> output = new HashMap<>();
    output.put("orderId", input.getOrderId());
    taskResult.setOutputData(output);

    if (result) {
      taskResult.setStatus(TaskResult.Status.COMPLETED);
    } else {
      taskResult.setStatus(TaskResult.Status.FAILED);
    }

    return taskResult;
  }

}
