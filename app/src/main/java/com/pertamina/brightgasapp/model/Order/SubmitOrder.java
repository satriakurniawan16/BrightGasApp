package com.pertamina.brightgasapp.model.Order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubmitOrder {

    private int user_id_pemesan;
    private int user_same;
    private String nama_penerima;
    private String no_hp_penerima;
    private String alamat_pengiriman;
    private String provinsi_pengiriman;
    private String kota_pengiriman;
    private String kecamatan_pengiriman;
    private String kode_pos_pengiriman;
    private String tgl_pengiriman;
    private int waktu_pengiriman;
    private String kode_promo;
    private int total_pesanan;

    @SerializedName("products")
    @Expose
    private List<Product> products= null;

    public int getUser_id_pemesan() {
        return user_id_pemesan;
    }

    public void setUser_id_pemesan(int user_id_pemesan) {
        this.user_id_pemesan = user_id_pemesan;
    }

    public int getUser_same() {
        return user_same;
    }

    public void setUser_same(int user_same) {
        this.user_same = user_same;
    }

    public String getNama_penerima() {
        return nama_penerima;
    }

    public void setNama_penerima(String nama_penerima) {
        this.nama_penerima = nama_penerima;
    }

    public String getNo_hp_penerima() {
        return no_hp_penerima;
    }

    public void setNo_hp_penerima(String no_hp_penerima) {
        this.no_hp_penerima = no_hp_penerima;
    }

    public String getAlamat_pengiriman() {
        return alamat_pengiriman;
    }

    public void setAlamat_pengiriman(String alamat_pengiriman) {
        this.alamat_pengiriman = alamat_pengiriman;
    }

    public String getProvinsi_pengiriman() {
        return provinsi_pengiriman;
    }

    public void setProvinsi_pengiriman(String provinsi_pengiriman) {
        this.provinsi_pengiriman = provinsi_pengiriman;
    }

    public String getKota_pengiriman() {
        return kota_pengiriman;
    }

    public void setKota_pengiriman(String kota_pengiriman) {
        this.kota_pengiriman = kota_pengiriman;
    }

    public String getKecamatan_pengiriman() {
        return kecamatan_pengiriman;
    }

    public void setKecamatan_pengiriman(String kecamatan_pengiriman) {
        this.kecamatan_pengiriman = kecamatan_pengiriman;
    }

    public String getKode_pos_pengiriman() {
        return kode_pos_pengiriman;
    }

    public void setKode_pos_pengiriman(String kode_pos_pengiriman) {
        this.kode_pos_pengiriman = kode_pos_pengiriman;
    }

    public String getTgl_pengiriman() {
        return tgl_pengiriman;
    }

    public void setTgl_pengiriman(String tgl_pengiriman) {
        this.tgl_pengiriman = tgl_pengiriman;
    }

    public int getWaktu_pengiriman() {
        return waktu_pengiriman;
    }

    public void setWaktu_pengiriman(int waktu_pengiriman) {
        this.waktu_pengiriman = waktu_pengiriman;
    }

    public String getKode_promo() {
        return kode_promo;
    }

    public void setKode_promo(String kode_promo) {
        this.kode_promo = kode_promo;
    }

    public int getTotal_pesanan() {
        return total_pesanan;
    }

    public void setTotal_pesanan(int total_pesanan) {
        this.total_pesanan = total_pesanan;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
