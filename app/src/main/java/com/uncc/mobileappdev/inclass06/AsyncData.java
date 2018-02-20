package com.uncc.mobileappdev.inclass06;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Stephen on 2/19/2018.
 */

public class AsyncData extends AsyncTask<String, Void, ArrayList<Articles>> {

    ProgressDialog progressDialog;
    IData iDataActivity;
    Activity activity;

    public AsyncData(IData iDataActivity, Activity activity){
        this.iDataActivity = iDataActivity;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Articles...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected ArrayList<Articles> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Articles> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();

            JSONObject root = new JSONObject(json);
            JSONArray articlesArray = root.getJSONArray("articles");
            for (int i = 0; i < articlesArray.length(); i++) {
            JSONObject articleObject = articlesArray.getJSONObject(i);

            Articles articles = new Articles();
            articles.setAuthor(articleObject.getString("author"));
            articles.setDescription(articleObject.getString("description"));
            articles.setTitle(articleObject.getString("title"));
            articles.setUrl(articleObject.getString("url"));
            articles.setUrlToImage(articleObject.getString("urlToImage"));
            articles.setPublishedAtDate(articleObject.getString("publishedAt"));

            JSONObject sourceObject = articleObject.getJSONObject("source");

            Source source = new Source();
            source.setId(sourceObject.getString("id"));
            source.setName(sourceObject.getString("name"));

            articles.setSource(source);
            result.add(articles);

            }
        }catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }

        return result;
    }

    @Override
    protected  void onPostExecute(ArrayList<Articles> result){
        if(result != null && !result.isEmpty()){
            iDataActivity.setupData(result);
            for(Articles p : result){
                Log.d("Demo", p.getAuthor());
            }
            progressDialog.dismiss();
        } else {
            Log.d("Demo", "NULL result!");
        }
    }

    static public interface IData {
        public void setupData(ArrayList<Articles> result);
    }

}
