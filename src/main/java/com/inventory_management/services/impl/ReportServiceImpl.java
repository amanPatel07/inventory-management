package com.inventory_management.services.impl;

import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.model.dtos.InventorySummaryResponse;
import com.inventory_management.model.entity.Product;
import com.inventory_management.model.enums.StockMovementType;
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

    public ReportServiceImpl(
            ProductRepository productRepository,
            StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getCurrentStock() <= product.getMinimumStock())
                .map(this::toResponse)
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

    private ProductResponse toResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setCurrentStock(product.getCurrentStock());
        response.setMinimumStock(product.getMinimumStock());

        return response;
    }
}
