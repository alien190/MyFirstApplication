package com.example.ivanovnv.myfirstapplication.model;

import com.google.gson.annotations.SerializedName;

public class Data<T> {
    @SerializedName("data")
    public T response;
}
