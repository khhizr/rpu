package com.bestdeals.requestprocessor.tests;

import com.bestdeals.commons.testing.AbstractBaseTest;
import com.bestdeals.requestprocessor.models.Product;
import com.bestdeals.requestprocessor.repository.ProductRepository;
import com.bestdeals.requestprocessor.services.ScrapperService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestProcessorControllerTest extends AbstractBaseTest {
    @Autowired
    ProductRepository productRepository;

    @MockBean
    ScrapperService scrapperService;

    private RestTemplate restTemplate;

    @BeforeAll()
    public void setupForTest() {
        restTemplate = getRestTemplate();
    }

    @BeforeEach()
    public void cleanDb() {
        productRepository.deleteAll();
    }

    @Test
    public void getProductDealsWhenDealInDBTest() {
        Product prod1 = new Product("a2dfg", "samsung galaxy", 50000F, "electronics", "Flipkart",
                new String[] { "50% off ", "free phone" }, "http://imagegetter/image1", "samsung galaxy"

        );
        productRepository.save(prod1);

        String name = prod1.getProductName();
        String category = prod1.getCategory();
        String URL = this.baseUrl + "/getdeals?productName={productName}&productCategory={productCategory}";

        ResponseEntity<List<Product>> responseProduct = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {
                }, name, category);
        assertNotNull(responseProduct);
        assertNotNull(responseProduct.getBody());
        assertEquals(1, responseProduct.getBody().size());
    }

    @Test
    public void getProductDealsAfterCallingScrappyServiceTest()
            throws JsonProcessingException, UnsupportedEncodingException {
        List<Product> products = new ArrayList<>();
        products.add(new Product("a2dfg", "samsung galaxy", 50000F, "electronics", "Flipkart",
                new String[] { "50% off ", "free phone" }, "http://imagegetter/image2", "samsung galaxy"));
        products.add(new Product("3456a", "samsung galaxy", 48500F, "electronics", "Amazon",
                new String[] { "40% off ", "free phone" }, "http://imagegetter/image3", "samsung galaxy"));

        Mockito.when(this.scrapperService.sendRequestToScrapper("samsung galaxy", "electronics")).thenReturn(products);

        String name = "samsung galaxy";
        String category = "electronics";
        String URL = this.baseUrl + "/getdeals?productName={productName}&productCategory={productCategory}";

        ResponseEntity<List<Product>> responseProduct = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {
                }, name, category);
        assertNotNull(responseProduct);
        assertNotNull(responseProduct.getBody());
        assertEquals(2, responseProduct.getBody().size());
    }
}