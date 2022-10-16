package com.bestdeals.requestprocessor.services;

import com.bestdeals.requestprocessor.models.Product;
import com.bestdeals.requestprocessor.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    public ProductRepository productRepository;

    public List<Product> getDealByProductNameOrUserQueryName(String name) {

        if (name == null) {
            throw new IllegalArgumentException();
        }
        return productRepository.findByProductNameOrUserQueryName(name);

    }

    public List<String> getAllDeals() {
        return productRepository.findByUniqueUserQueryName();
    }

    public List<Product> searchProducts(String productName) {
        AggregationResults<Product> products = productRepository.searchForProducts(productName);
        if (products == null) {
            return null;
        }
        return products.getMappedResults();
    }
}
