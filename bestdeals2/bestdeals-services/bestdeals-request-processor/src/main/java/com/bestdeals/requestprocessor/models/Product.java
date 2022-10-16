package com.bestdeals.requestprocessor.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document("Product")
public class Product {
    @Id
    private String id;
    @Field
    private String productName;
    @Field
    private float price;
    @Field
    private String category;
    @Field
    private String websiteName;
    @Field
    private String[] deals;
    @Field
    private String imageUrl;

    @Field
    private String userQueryName;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = (float) price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsite(String website) {
        this.websiteName = website;
    }

    public String[] getDeals() {
        return deals;
    }

    public void setDeals(String[] deals) {
        this.deals = deals;
    }

    public String getQueryName() {
        return userQueryName;
    }

    public void setQueryName(String userQueryName) {
        this.userQueryName = userQueryName;
    }

    public Product(String id, String productName, Float price, String category, String websiteName, String[] deals,
            String imageUrl, String userQueryName) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.websiteName = websiteName;
        this.deals = deals;
        this.imageUrl = imageUrl;
        this.userQueryName = userQueryName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", websiteName='" + websiteName + '\'' +
                ", deals=" + Arrays.toString(deals) +
                ", imageUrl='" + imageUrl + '\'' +
                ", userQueryName='" + userQueryName + '\'' +
                '}';
    }
}