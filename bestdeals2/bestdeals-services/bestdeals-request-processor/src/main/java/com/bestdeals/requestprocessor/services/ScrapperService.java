package com.bestdeals.requestprocessor.services;

import com.bestdeals.requestprocessor.models.Product;
import com.bestdeals.requestprocessor.models.ScrapperResponse;
import com.bestdeals.requestprocessor.models.ScrappyStatus;

import com.bestdeals.requestprocessor.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ScrapperService {
    private RestTemplate restTemplate;
    private static Logger LOGGER = LoggerFactory.getLogger(ScrapperService.class);

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Value("${scrapper.url}")
    private String baseURL;

    String defaultCategory = "electronics";

    @Autowired
    public final void setRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<Product> sendRequestToScrapper(String name, String category)
            throws JsonProcessingException, UnsupportedEncodingException {

        List<Product> productDeals = new ArrayList<>();

        Map<String, String> params = new HashMap<String, String>();
        params.put("productName", name);
        params.put("productCategory", category);

        StringBuilder builder = new StringBuilder(baseURL + "/scrape-data");
        builder.append("?productName=");
        builder.append(URLEncoder.encode(name, StandardCharsets.UTF_8.toString()));
        builder.append("&productCategory=");
        builder.append(URLEncoder.encode(category, StandardCharsets.UTF_8.toString()));

        URI uri = URI.create(builder.toString());

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

        String responseBody = responseEntity.getBody();

        ScrapperResponse scrapperResponse = objectMapper.readValue(responseBody, ScrapperResponse.class);

        if (responseBody != null && scrapperResponse.getStatus() == ScrappyStatus.SCRAPPING_COMPLETE) {
            productDeals = productService.getDealByProductNameOrUserQueryName(name);
        } else {
            LOGGER.error("Unable to fetch scrapped product deals for product name" + name);
            LOGGER.error("Scrapper response " + scrapperResponse);
        }

        return productDeals;
    }

    @Scheduled(cron = "0 00 01 * * *")
    public void scheduleScrapping() throws UnsupportedEncodingException, JsonProcessingException {
        List<String> productToUpdate = new ArrayList<>();

        productToUpdate = productService.getAllDeals();

        for (String queryName : productToUpdate) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("productName", queryName);
            params.put("productCategory", defaultCategory);

            StringBuilder builder = new StringBuilder(baseURL + "/scrape-data");
            builder.append("?productName=");
            builder.append(URLEncoder.encode(queryName, StandardCharsets.UTF_8.toString()));
            builder.append("&productCategory=");
            builder.append(URLEncoder.encode(defaultCategory, StandardCharsets.UTF_8.toString()));

            URI uri = URI.create(builder.toString());

            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

            String responseBody = responseEntity.getBody();

            ScrapperResponse scrapperResponse = objectMapper.readValue(responseBody, ScrapperResponse.class);

            if (responseBody != null && scrapperResponse.getStatus() == ScrappyStatus.SCRAPPING_FAILURE) {
                LOGGER.error("Unable to update the product " + queryName);
            }
        }
    }

}