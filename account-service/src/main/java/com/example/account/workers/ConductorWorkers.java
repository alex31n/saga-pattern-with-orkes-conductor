package com.example.account.workers;

import com.example.account.pojo.BalanceDeductionProcess;
import com.example.account.pojo.ValidateBalance;
import com.example.account.service.AccountService;
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

  private final AccountService accountService;


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


}
