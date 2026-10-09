package com.inventory_management.services.impl;

import com.inventory_management.mappers.ProductMapper;
import com.inventory_management.models.dtos.ProductResponse;
import com.inventory_management.models.dtos.InventorySummaryResponse;
import com.inventory_management.models.entity.Product;
import com.inventory_management.models.enums.StockMovementType;
import com.inventory_management.repositories.ProductRepository;
import com.inventory_management.repositories.StockMovementRepository;
import com.inventory_management.services.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ProductMapper productMapper;

    public ReportServiceImpl(
            ProductRepository productRepository,
            StockMovementRepository stockMovementRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getCurrentStock() <= product.getMinimumStock())
                .map(productMapper::toResponse)
                .toList();
    }

    public double getTotalStockValue() {
        return productRepository.findAll()
                .stream()
                .mapToDouble(product -> product.getCurrentStock() * product.getPrice())
                .sum();
    }

    public Map<String, Double> getStockValueByCategory() {
        return productRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.summingDouble(product ->
                                product.getCurrentStock()
                                        * product.getPrice()
                        )
                ));
    }

    public InventorySummaryResponse getInventorySummary() {
        List<Product> products = productRepository.findAll();
        var movements = stockMovementRepository.findAll();
        InventorySummaryResponse response = new InventorySummaryResponse();
        response.setTotalProducts(products.size());
        response.setTotalUnitsInStock(products.stream()
                .mapToLong(Product::getCurrentStock)
                .sum());
        response.setLowStockProducts(products.stream()
                .filter(product -> product.getCurrentStock() <= product.getMinimumStock())
                .count());
        response.setTotalInventoryValue(products.stream()
                .mapToDouble(product -> product.getCurrentStock() * product.getPrice())
                .sum());
        response.setTotalStockIn(movements.stream()
                .filter(movement -> movement.getType() == StockMovementType.IN)
                .mapToLong(movement -> movement.getQuantity())
                .sum());
        response.setTotalStockOut(movements.stream()
                .filter(movement -> movement.getType() == StockMovementType.OUT)
                .mapToLong(movement -> movement.getQuantity())
                .sum());
        return response;
    }
}
