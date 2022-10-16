package com.bestdeals.requestprocessor.controllers;

import com.bestdeals.requestprocessor.exceptions.BadRequestException;
import com.bestdeals.requestprocessor.models.Product;
import com.bestdeals.requestprocessor.services.ProductService;
import com.bestdeals.requestprocessor.services.ScrapperService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
class RequestProcessorController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ScrapperService scrapperService;

    @GetMapping(path = "/getdeals")
    public ResponseEntity<List<Product>> getAllDeals(@RequestParam(name = "productName") String name,
            @RequestParam(name = "productCategory") String category)
            throws JsonProcessingException, UnsupportedEncodingException {

        List<Product> productDeals = new ArrayList<>();

        productDeals = productService.getDealByProductNameOrUserQueryName(name);

        if (productDeals.size() == 0) {

            productDeals = scrapperService.sendRequestToScrapper(name, category);
        }

        if (productDeals.size() == 0) {
            throw new BadRequestException("Deals not Found");
        }
        return ResponseEntity.ok(productDeals);
    }

    @GetMapping("products/autocorrect")
    public ResponseEntity<List<String>> searchProducts(@RequestParam String productName) {
        try {
            List<String> productnamelist = productService.searchProducts(productName).stream()
                    .map(Product::getProductName).collect(Collectors.toList());
            return ResponseEntity.ok().body(productnamelist);
        } catch (Exception e) {
            throw new BadRequestException("auto search error");
        }
    }

}