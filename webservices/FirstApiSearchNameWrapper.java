package com.example.unamed.mvc.webservices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FirstApiSearchNameWrapper {
    @SerializedName("results")
    @Expose
    private List<FirstApiItemWrapper> results = null;

    public List<FirstApiItemWrapper> getResults() {
        return results;
    }
}
