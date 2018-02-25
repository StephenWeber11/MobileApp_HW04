package com.uncc.mobileappdev.homework04;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AsyncData.IData {

    private String selectedCategory;
    private String link;
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



    private static final HashMap<String,String> CATEGORIESMAP = new HashMap<>();

    static{
        CATEGORIESMAP.put("Top Stories","http://rss.cnn.com/rss/cnn_topstories.rss");
        CATEGORIESMAP.put("World","http://rss.cnn.com/rss/cnn_world.rss");
        CATEGORIESMAP.put("U.S.","http://rss.cnn.com/rss/cnn_us.rss");
        CATEGORIESMAP.put("Business","http://rss.cnn.com/rss/money_latest.rss");
        CATEGORIESMAP.put("Politics","http://rss.cnn.com/rss/cnn_allpolitics.rss");
        CATEGORIESMAP.put("Technology","http://rss.cnn.com/rss/cnn_tech.rss");
        CATEGORIESMAP.put("Health","http://rss.cnn.com/rss/cnn_health.rss");
        CATEGORIESMAP.put("Entertainment","http://rss.cnn.com/rss/cnn_showbiz.rss");
        CATEGORIESMAP.put("Travel","http://rss.cnn.com/rss/cnn_travel.rss");
        CATEGORIESMAP.put("Living","http://rss.cnn.com/rss/cnn_living.rss");
        CATEGORIESMAP.put("Most Recent","http://rss.cnn.com/rss/cnn_latest.rss");
    }

    private static final ArrayList<String> CATEGORIES = new ArrayList<>();

    static {
        CATEGORIES.add("Top Stories");
        CATEGORIES.add("World");
        CATEGORIES.add("U.S.");
        CATEGORIES.add("Business");
        CATEGORIES.add("Politics");
        CATEGORIES.add("Technology");
        CATEGORIES.add("Health");
        CATEGORIES.add("Entertainment");
        CATEGORIES.add("Travel");
        CATEGORIES.add("Living");
        CATEGORIES.add("Most Recent");

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

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(getLink()));
                startActivity(browse);
            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Toast toast = Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT);
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

                new AsyncData(MainActivity.this,MainActivity.this).execute(CATEGORIESMAP.get(selectedCategory));
                currentIndex = 0;

                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    public void setupData(ArrayList<Articles> result) {
        articles = result;
        if(articles == null || articles.size() == 0){
            Toast toast = Toast.makeText(this,"No News Found", Toast.LENGTH_SHORT);
            return;
        }

        Log.d("Demo", "The size of the list of articles is: " + articles.size());

        title.setText(articles.get(0).getTitle());
        date.setText(articles.get(0).getPublishedAtDate());
        setLink(articles.get(currentIndex).getUrl());
        Picasso.with(MainActivity.this).load(articles.get(0).getUrlToImage()).into(main);
        description.setText("");
        if(!articles.get(0).getDescription().equals("") || !articles.get(0).getDescription().equals(null)){
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
            setLink(articles.get(currentIndex).getUrl());
            Picasso.with(MainActivity.this).load(articles.get(currentIndex).getUrlToImage()).into(main);
            description.setText("");
            if(!articles.get(currentIndex).getDescription().equals("null")) {
                description.setText(articles.get(currentIndex).getDescription());
            }
            counter.setText(currentIndex+1 + " out of "+articles.size());

        } else {
            currentIndex = 0;

            title.setText(articles.get(currentIndex).getTitle());
            date.setText(articles.get(currentIndex).getPublishedAtDate());
            setLink(articles.get(currentIndex).getUrl());
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
            setLink(articles.get(currentIndex).getUrl());
            Picasso.with(MainActivity.this).load(articles.get(currentIndex).getUrlToImage()).into(main);
            description.setText("");
            if(!articles.get(currentIndex).getDescription().equals("null")) {
                description.setText(articles.get(currentIndex).getDescription());
            }
            counter.setText(currentIndex+1 + " out of "+articles.size());

        } else {
            currentIndex = articles.size()-1;

            title.setText(articles.get(currentIndex).getTitle());
            date.setText(articles.get(currentIndex).getPublishedAtDate());
            setLink(articles.get(currentIndex).getUrl());
            Picasso.with(MainActivity.this).load(articles.get(currentIndex).getUrlToImage()).into(main);
            description.setText("");
            if(!articles.get(currentIndex).getDescription().equals("null")) {
                description.setText(articles.get(currentIndex).getDescription());
            }
            counter.setText(currentIndex+1 + " out of "+articles.size());

        }
    }

    public String getLink(){
        return link;
    }

    public void setLink(String url){
        this.link = url;
    }

}
