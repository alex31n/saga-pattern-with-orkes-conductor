package com.example.saga.workers;

import com.example.saga.dto.CreateOrderRequest;
import com.example.saga.dto.PaymentRequest;
import com.example.saga.entity.Order;
import com.example.saga.entity.Payment;
import com.example.saga.enumeration.Status;
import com.example.saga.services.OrderService;
import com.example.saga.services.PaymentService;
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

  private final PaymentService paymentService;

  @WorkerTask(value = "create_order", threadCount = 3, pollingInterval = 300)
  public TaskResult orderFoodTask(CreateOrderRequest createOrderRequest) {
    log.info("ConductorWorkers create_order: {}", createOrderRequest);
    Order order = orderService.createOrder(createOrderRequest);

    TaskResult result = new TaskResult();
    Map<String, Object> output = new HashMap<>();

    if (order != null) {
      output.put("orderId", order.getId().toString());
      result.setOutputData(output);
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      output.put("orderId", null);
      result.setStatus(TaskResult.Status.FAILED);
    }
    return result;
  }

  @WorkerTask(value = "make_payment", threadCount = 2, pollingInterval = 300)
  public TaskResult makePaymentTask(PaymentRequest paymentRequest) {
    log.info("ConductorWorkers make_payment: {}", paymentRequest);
    Payment payment = paymentService.createPayment(paymentRequest);

    TaskResult result = new TaskResult();

    Map<String, Object> output = new HashMap<>();
    output.put("orderId", payment.getOrderId());
    output.put("paymentId", payment.getPaymentId());
    output.put("paymentStatus", payment.getStatus().name());

    if (payment.getStatus() == Status.SUCCESSFUL) {
      result.setStatus(TaskResult.Status.COMPLETED);
    } else {
      result.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
    }

    result.setOutputData(output);

    return result;
  }

  @WorkerTask(value = "ship_order", threadCount = 2, pollingInterval = 300)
  public TaskResult shipOrderTask(Map<String, Object> request) {
    log.info("ConductorWorkers ship_order: {}", request);

    TaskResult result = new TaskResult();

    Map<String, Object> output = new HashMap<>();

    if (request.containsKey("orderId")) {
      output.put("orderId", request.get("orderId"));
    }

    result.setStatus(TaskResult.Status.COMPLETED);

    result.setOutputData(output);

    return result;
  }

  @WorkerTask(value = "notify_customer", threadCount = 2, pollingInterval = 300)
  public TaskResult notifyCustomerTask(Map<String, Object> request) {
    log.info("ConductorWorkers notify_customer: {}", request);

    TaskResult result = new TaskResult();

    Map<String, Object> output = new HashMap<>();

    if (request.containsKey("orderId")) {
      output.put("orderId", request.get("orderId"));
    }

    result.setStatus(TaskResult.Status.COMPLETED);

    result.setOutputData(output);

    return result;
  }

}
