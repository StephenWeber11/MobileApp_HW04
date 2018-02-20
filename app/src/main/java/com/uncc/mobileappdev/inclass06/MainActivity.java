package com.uncc.mobileappdev.inclass06;

import android.content.DialogInterface;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    private String selectedCategory;
    private int currentIndex = 0;
    private ArrayList<Articles> articles = new ArrayList<>();
    EditText searchBar;
    TextView counter;
    TextView title;
    TextView date;
    TextView description;
    ImageView main;
    ImageView prev;
    ImageView next;



    private static final ArrayList<String> CATEGORIES = new ArrayList<>();

    static{
        CATEGORIES.add("Business");
        CATEGORIES.add("Entertainment");
        CATEGORIES.add("General");
        CATEGORIES.add("Health");
        CATEGORIES.add("Science");
        CATEGORIES.add("Sports");
        CATEGORIES.add("Technology");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBar = findViewById(R.id.search_box);
        title = findViewById(R.id.article_title);
        date = findViewById(R.id.article_date);
        description = findViewById(R.id.desc_text);
        main = findViewById(R.id.article_image);
        prev = findViewById(R.id.prev_image);
        next = findViewById(R.id.next_image);
        counter = findViewById(R.id.counter);

        findViewById(R.id.button_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(CATEGORIES);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrev();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
            }
        });

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

    private void showPopup(ArrayList<String> categories) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Choose Category");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for(String str : categories){
            arrayAdapter.add(str);
            Log.d("Keyword", str);
        }

        alert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCategory = arrayAdapter.getItem(which);
                searchBar.setText(selectedCategory);

                String queryParam = "&category=" + selectedCategory;
                new AsyncData(MainActivity.this,MainActivity.this).execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=ebb2dacd7f314b5fa3300c914e144121" + queryParam);
                currentIndex = 0;

                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    public void setupData(ArrayList<Articles> result) {
        articles = result;
        Log.d("Demo", "The size of the list of articles is: " + articles.size());

        title.setText(articles.get(0).getTitle());
        date.setText(articles.get(0).getPublishedAtDate());
        Picasso.with(MainActivity.this).load(articles.get(0).getUrlToImage()).into(main);
        description.setText("");
        if(!articles.get(0).getDescription().equals(null)){
            description.setText(articles.get(0).getDescription());
        }
        counter.setText("1 out of "+articles.size());

        title.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        prev.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        main.setVisibility(View.VISIBLE);
        counter.setVisibility(View.VISIBLE);



    }

    public void showNext(){
        if(currentIndex != articles.size()-1){
            currentIndex++;

            title.setText(articles.get(currentIndex).getTitle());
            date.setText(articles.get(currentIndex).getPublishedAtDate());
            Picasso.with(MainActivity.this).load(articles.get(currentIndex).getUrlToImage()).into(main);
            description.setText("");
            if(!articles.get(currentIndex).getDescription().equals("null")) {
                description.setText(articles.get(currentIndex).getDescription());
            }
            counter.setText(currentIndex+1 + " out of "+articles.size());

        }
    }

    public void showPrev(){
        if(currentIndex != 0){
            currentIndex--;

            title.setText(articles.get(currentIndex).getTitle());
            date.setText(articles.get(currentIndex).getPublishedAtDate());
            Picasso.with(MainActivity.this).load(articles.get(currentIndex).getUrlToImage()).into(main);
            description.setText("");
            if(!articles.get(currentIndex).getDescription().equals("null")) {
                description.setText(articles.get(currentIndex).getDescription());
            }
            counter.setText(currentIndex+1 + " out of "+articles.size());

        }
    }

}
