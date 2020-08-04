package com.pertamina.brightgasapp.view.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.pertamina.brightgasapp.model.Order.Product;
import com.pertamina.brightgasapp.model.login.LoginModel;

import static com.pertamina.brightgasapp.view.utilities.GlobalString.Preferences.LOGIN;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.Preferences.ORDER;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.Preferences.PRODUCT;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.Preferences.PROMO;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.Preferences.REG_TOKEN_NEW;
import static com.pertamina.brightgasapp.view.utilities.GlobalString.Preferences.REG_TOKEN_SECOND;

public class Preference {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public static final String PREF_TOKEN = "com.satriakurniawandicoding.musicoolapp";

    public Preference(Context context) {
        gson = new Gson();
        pref = context.getSharedPreferences(PREF_TOKEN, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setToken1(String token) {
        editor.putString(REG_TOKEN_NEW, token);
        editor.commit();
    }

    public void removeToken1(){
        editor.remove(REG_TOKEN_NEW);
        editor.commit();
    }

    public String getToken1() {
        return pref.getString(REG_TOKEN_NEW, null);
    }

    public void setToken2(String token) {
        editor.putString(REG_TOKEN_SECOND, token);
        editor.commit();
    }

    public void removeToken2(){
        editor.remove(REG_TOKEN_SECOND);
        editor.commit();
    }

    public String getToken2() {
        return pref.getString(REG_TOKEN_SECOND, null);
    }

    public void setUser(String user) {
        editor.putString(LOGIN, user);
        editor.commit();
    }

    public void removeUser(){
        editor.remove(LOGIN);
        editor.commit();
    }

    public String getUser() {
        return pref.getString(LOGIN, null);
    }

    public void setOrder(String order) {
        editor.putString(ORDER, order);
        editor.commit();
    }

    public void removeOrder(){
        editor.remove(ORDER);
        editor.commit();
    }

    public String getOrder() {
        return pref.getString(ORDER, null);
    }

    public void setProduct(String order) {
        editor.putString(PRODUCT, order);
        editor.commit();
    }

    public void removeProduct(){
        editor.remove(PRODUCT);
        editor.commit();
    }

    public String getProduct() {
        return pref.getString(PRODUCT, null);
    }

    public void setPromo(String order) {
        editor.putString(PROMO, order);
        editor.commit();
    }

    public void removePromo(){
        editor.remove(PROMO);
        editor.commit();
    }

    public String getPromo() {
        return pref.getString(PROMO, null);
    }

}
