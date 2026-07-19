package com.example.crdpractice.order;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    boolean existsByExternalOrderId(String externalOrderId);
}
