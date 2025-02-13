package com.example.saga.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

  @Id
  private UUID id;

  @Column(name = "workflow_id")
  private String workflowId;

  @Column(name = "customer_name")
  private String customerName;

  @Column(name = "customer_id")
  private String customerId;

  @Column(name = "delivery_address")
  private String deliveryAddress;

  private Double amount;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;



  @PrePersist
  public void prePersist() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = Instant.now();
  }

}
