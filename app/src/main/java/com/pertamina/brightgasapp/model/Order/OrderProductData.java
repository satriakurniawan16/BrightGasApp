package com.pertamina.brightgasapp.model.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pertamina.brightgasapp.model.productList.ProductResult;

import java.util.List;

public class OrderProductData {
    @SerializedName("data")
    @Expose
    private List<OrderProductResult> productResults= null;

    public List<OrderProductResult> getProductResults() {
        return productResults;
    }

    public void setProductResults(List<OrderProductResult> productResults) {
        this.productResults = productResults;
    }
}
