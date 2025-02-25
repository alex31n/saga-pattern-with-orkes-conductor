package com.example.shipment.workers;

import com.example.shipment.pojo.ShipOrder;
import com.example.shipment.service.ShipmentService;
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

  private final ShipmentService shipmentService;


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
