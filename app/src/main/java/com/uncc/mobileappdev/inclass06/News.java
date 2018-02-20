package com.uncc.mobileappdev.inclass06;

import java.util.ArrayList;

/**
 * Created by Stephen on 2/19/2018.
 */

public class News {
    private String status;
    private int totalResults;
    private ArrayList<Articles> articles = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Articles> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Articles> articles) {
        this.articles = articles;
    }
}