package com.pertamina.brightgasapp.model.productList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductTrade {
    @SerializedName("Trade In")
    @Expose
    private List<ProductResult> productResults= null;

    public List<ProductResult> getProductResults() {
        return productResults;
    }

    public void setProductResults(List<ProductResult> productResults) {
        this.productResults = productResults;
    }

    @Override
    public String toString() {
        return "ProductTrade{" +
                "productResults=" + productResults +
                '}';
    }
}
