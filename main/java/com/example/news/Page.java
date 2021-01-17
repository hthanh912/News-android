package com.example.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Page extends AppCompatActivity {

    WebView webView;
    String url;
    Toolbar toolbar;
    TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTime = findViewById(R.id.time);

        webView = findViewById(R.id.webview);


        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);


        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("link");
        System.out.println(url);
        PageView pageView = new PageView();
        pageView.execute(url);

    }

    class PageView extends AsyncTask<String, Void, String> {

        Elements page;
        Elements body, brc ,list;
        String title;
        String html;
        String breadcrumb, time;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Connect to the website
                Document document = Jsoup.connect(strings[0]).get();
                page = document.getElementsByClass("dt-news__title");
                title = page.select("h1").text();
                body = document.getElementsByClass("dt-news__content");
                brc = document.getElementsByClass("dt-breadcrumb");
                list = brc.select("a");
                breadcrumb = list.get(list.size()-1).text();
                time = document.select("span.dt-news__time").text();

                String head = "<head><style>figcaption{background-color: #f0f0f0} figure{margin:0} img{max-width: 100%; width:auto; height: auto;} video{max-width: 100%; width:auto; height: auto;}</style></head>";
                html = "<html>" + head + "<body>" + "<h3 style=\"font-family:serif\">" + title + "</h1>" + body.html() + "</body></html>";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            toolbar.setTitle(breadcrumb);
            tvTime.setText(time);
            //webView.loadData(html, "text/html", null);
            webView.loadDataWithBaseURL(url, html,
                    "text/html; charset=utf-8", "UTF-8", null);
        }
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
}