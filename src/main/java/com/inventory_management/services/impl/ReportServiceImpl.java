package com.inventory_management.services.impl;

import com.inventory_management.model.dtos.ProductResponse;
import com.inventory_management.model.entity.Product;
import com.inventory_management.repositories.ProductRepository;
import com.inventory_management.services.ReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ProductRepository productRepository;

    public ReportServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
