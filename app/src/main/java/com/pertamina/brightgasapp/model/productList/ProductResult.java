package com.pertamina.brightgasapp.model.productList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductResult {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("type_product")
    @Expose
    private String type_product;

    @SerializedName("type_sell")
    @Expose
    private String type_sell;

    @SerializedName("ongkir")
    @Expose
    private String ongkir;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("kota_id")
    @Expose
    private String kota_id;

    @SerializedName("kota")
    @Expose
    private String kota;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType_product() {
        return type_product;
    }

    public void setType_product(String type_product) {
        this.type_product = type_product;
    }

    public String getType_sell() {
        return type_sell;
    }

    public void setType_sell(String type_sell) {
        this.type_sell = type_sell;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKota_id() {
        return kota_id;
    }

    public void setKota_id(String kota_id) {
        this.kota_id = kota_id;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    @Override
    public String toString() {
        return "ProductResult{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", type_product='" + type_product + '\'' +
                ", type_sell='" + type_sell + '\'' +
                ", ongkir='" + ongkir + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", kota_id='" + kota_id + '\'' +
                ", kota='" + kota + '\'' +
                '}';
    }
}
