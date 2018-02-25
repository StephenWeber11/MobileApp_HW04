package com.uncc.mobileappdev.inclass06;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    protected ArrayList<Articles> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Articles> result = null;

        try{
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                result = NewsParser.NewsPullParser.parseArticles(connection.getInputStream());

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
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
            for(Articles article : result){
                Log.d("Demo", article.getTitle());
            }
            iDataActivity.setupData(result);
        } else {
            Log.d("Demo", "NULL result!");
        }

        progressDialog.dismiss();
    }



    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Articles...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    static public interface IData {
        public void setupData(ArrayList<Articles> result);
    }

}
