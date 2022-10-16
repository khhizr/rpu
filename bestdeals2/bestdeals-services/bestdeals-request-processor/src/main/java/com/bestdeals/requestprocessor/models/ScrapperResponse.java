package com.bestdeals.requestprocessor.models;

public class ScrapperResponse {
    private ScrappyStatus status;
    private String productName;

    public ScrapperResponse() {
    }

    public ScrapperResponse(ScrappyStatus status, String productName) {
        super();
        this.status = status;
        this.productName = productName;
    }

    public ScrappyStatus getStatus() {
        return status;
    }

    public void setStatus(ScrappyStatus status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
