package com.example.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<Article> items = new ArrayList<Article>();
    RecyclerView listNews;
    LinearLayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listNews = root.findViewById(R.id.listNews);

        layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(listNews.getContext(), layoutManager.getOrientation());
        listNews.addItemDecoration(itemDecoration);

        String url = "https://dantri.com.vn/ ";
        Content content = new Content();
        content.execute(url);

        return root;
    }

    class Content extends AsyncTask<String, Void, String> {

        Elements articles;
        ArrayList<Article> listArticles = new ArrayList<Article>();
        String link;
        String title;
        String thumb;
        String desc;
        String time;
        String cate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //Connect to the website
                Document document = Jsoup.connect(strings[0]).get();

                articles = document.getElementsByClass("news-item");
                for (Element e : articles){

                    link ="";
                    title ="";
                    thumb ="";
                    desc ="";
                    time = "";
                    cate = "";

                    link = e.select(".news-item__title > a").attr("href");
                    title = e.select(".news-item__title > a").attr("title");
                    thumb = e.select(".dt-thumbnail > img").attr("lazy-src");
                    desc = e.select(".news-item__content > a").text();
                    time = e.select(".news-item__time").text();
                    cate = e.select(".news-item__meta > a").attr("title");

                    if (link!= "" && title!= "" && thumb!="" && desc!="" )
                        items.add(new Article(link, title, thumb, desc, time, cate));
                }
                System.out.println(items.size()+"");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            NewsAdapter adapter = new NewsAdapter(getActivity(), items);
            listNews.setLayoutManager(layoutManager);
            listNews.setAdapter(adapter);
        }
    }
}