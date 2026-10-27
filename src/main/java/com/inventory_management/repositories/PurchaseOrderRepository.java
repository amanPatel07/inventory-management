package com.inventory_management.repositories;

import com.inventory_management.models.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    Optional<List<PurchaseOrder>> findByCreatedBy_Id(UUID id);
}
