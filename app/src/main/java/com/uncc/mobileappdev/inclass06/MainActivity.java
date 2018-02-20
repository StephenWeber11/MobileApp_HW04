package com.uncc.mobileappdev.inclass06;

import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncData.IData {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isConnected()){
            new AsyncData(this).execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=ebb2dacd7f314b5fa3300c914e144121");
        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    @Override
    public void setupData(ArrayList<Articles> result) {

    }

//    private void showPopup(ArrayList<String> keywords) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("Choose Keyword");
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
//        for(String str : keywords){
//            arrayAdapter.add(str);
//            Log.d("Keyword", str);
//        }
//
//        alert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                selectedKeyword = arrayAdapter.getItem(which);
//                search.setText(selectedKeyword);
//
//                RequestParams requestParams = new RequestParams();
//                requestParams.addParameter("keyword", selectedKeyword);
//                imageLinks = new ArrayList<>();
//                new GetImageLinksAsync(requestParams).execute("http://dev.theappsdr.com/apis/photos/index.php");
//                new GetImageAsync(imageView).execute();
//                dialog.dismiss();
//            }
//        });
//
//        alert.show();
//    }

}
