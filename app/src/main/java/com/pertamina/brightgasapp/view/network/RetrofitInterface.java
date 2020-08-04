package com.pertamina.brightgasapp.view.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface RetrofitInterface {
    void onRequestSuccess(JsonObject response);
    void onRequestSuccess2(JsonArray response);
    void onRequestError(String error);
}
