package com.example.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    TextView tv;
    String searchValue;
    RecyclerView listSearch;
    ListSearchAdapter listSearchAdapter;
    ArrayList<SearchItem> searchItems;
    LinearLayoutManager layoutManager;
    ListSearchAdapter adapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listSearch = findViewById(R.id.listSearch);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        searchValue = bundle.getString("searchValue");
        searchValue = searchValue.trim().replace(' ', '+');

        layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(listSearch.getContext(), layoutManager.getOrientation());
        listSearch.addItemDecoration(itemDecoration);

        Search search = new Search(this);
        search.execute("https://www.google.com/search?q=" + searchValue +"+site%3Adantri.com.vn&num=40");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    class Search extends AsyncTask<String, Void, String> {

        Elements results, blacklist;
        String title, url, desc;
        boolean ishtml;
        Context context;

        public Search(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Document documentMenu = Jsoup.connect("https://dantri.com.vn").get();
                blacklist = documentMenu.getElementsByClass("site-menu").first().select("a");

                for(Element u: blacklist){
                    System.out.println(u.attr("href"));
                }

                //Connect to the website
                Document document = Jsoup.connect(strings[0]).get();
                results = document.getElementsByClass("g");
                searchItems = new ArrayList<SearchItem>();


                for (Element e: results){
                    url = "";
                    title = "";
                    desc = "";
                    ishtml = true;

                    url = e.getElementsByTag("a").attr("href");
                    title = e.getElementsByTag("h3").text();
                    desc = e.getElementsByTag("span").last().text();

                    System.out.println(isArticle(url, blacklist)+"");
                    if (url!="" && title!="" && desc !="" && isArticle(url, blacklist))
                        searchItems.add(new SearchItem(url,title,desc));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean isArticle(String url, Elements list){
            if (!url.substring(url.length()-4, url.length()).equals(".htm")) return  false;
            for(Element e: list){
                if (("https://dantri.com.vn" + e.attr("href")).equals(url)) return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            listSearch.setLayoutManager(layoutManager);
            if (searchItems.size() != 0) {
                adapter = new ListSearchAdapter(context, searchItems);
                listSearch.setAdapter(adapter);
            }
        }

    }
}