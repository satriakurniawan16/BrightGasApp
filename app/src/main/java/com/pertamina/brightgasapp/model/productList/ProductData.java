package com.pertamina.brightgasapp.model.productList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductData {

    @SerializedName("data")
    @Expose
    private  ProductTrade productTrade = null;

    public ProductTrade getProductTrade() {
        return productTrade;
    }

    public void setProductTrade(ProductTrade productTrade) {
        this.productTrade = productTrade;
    }

    @Override
    public String toString() {
        return "ProductData{" +
                "productTrade=" + productTrade +
                '}';
    }
}
