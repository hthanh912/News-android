package com.example.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class CategoriesFragment extends Fragment{

    RecyclerView listNews;
    NewsAdapter newsAdapter;
    ArrayList<Article> items;
    //ArrayList<Subcate> subcates;
    ArrayList<Subcate> listSubcate;
    String url;
    String subcat;
    int page;
    String currentUrl;
    String nextUrl;
    SubcateAdapter subcateAdapter;
    RecyclerView recyclerView;
    Content content;
    boolean isLoading = false;
    LinearLayoutManager layoutManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        listNews = root.findViewById(R.id.listNews);
        recyclerView = root.findViewById(R.id.recyclerView);

        Bundle bundle=getArguments();
        url = bundle.getString("url");
        page =1;
        nextUrl = "";

        GetSubcates getSubcates= new GetSubcates();
        getSubcates.execute(url+".htm");


        return root;
    }


    class Content extends AsyncTask<String, Void, String> {

        Elements primary, main, highlight;
        String link;
        String title;
        String thumb;
        String desc;
        String time;
        String cate;
        Article a;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                items = new ArrayList<Article>();
                //Connect to the website
                Document document = Jsoup.connect(strings[0]).get();

                primary = document.getElementsByClass("col--primary");
                primary = primary.select("div.news-item");

                main = document.getElementsByClass("col--main");
                main = main.select("div.news-item");

                highlight = document.getElementsByClass("dt-highlight");
                highlight = highlight.select("div.news-item");

                Elements[] list = {highlight, primary, main};

                for(Elements elements: list){
                    for (Element e : elements) {

                        link ="";
                        title ="";
                        thumb ="";
                        desc ="";
                        time ="";
                        cate = "";

                        link = e.select(".news-item__title > a").attr("href");
                        title = e.select(".news-item__title > a").attr("title");
                        thumb = e.select(".dt-thumbnail > img").attr("lazy-src");
                        desc = e.select(".news-item__content > a").text();
                        time = e.select(".news-item__time").text();
                        cate = e.select(".news-item__meta > a").attr("title");

                        if (link != "" && title != "" && thumb != "")
                            items.add(new Article(link, title, thumb, desc, time, cate));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            listNews.setLayoutManager(new LinearLayoutManager(getContext()));
            newsAdapter = new NewsAdapter(getActivity(), items);

            layoutManager = new LinearLayoutManager(getContext());
            DividerItemDecoration itemDecoration = new DividerItemDecoration(listNews.getContext(), layoutManager.getOrientation());
            listNews.addItemDecoration(itemDecoration);

            listNews.setAdapter(newsAdapter);
            listNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                        Log.d("-----","end");
                        page += 1;
                        nextUrl = currentUrl +  "/trang-" + page + ".htm";
                        System.out.println(nextUrl);
                        MoreContent moreContent = new MoreContent();
                        moreContent.execute(nextUrl);
                    }
                }
            });
        }
    }

    class MoreContent extends AsyncTask<String, Void, String> {

        Elements articles;
        ArrayList<Article> moreItems = new ArrayList<Article>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                moreItems = new ArrayList<Article>();
                //Connect to the website
                Document document = Jsoup.connect(strings[0]).get();

                articles = document.getElementsByClass("news-item");

                String link ="";
                String title ="";
                String thumb ="";
                String desc ="";
                String time ="";
                String cate = "";

                for (Element e : articles){
                    link = e.select(".news-item__title > a").attr("href");
                    title = e.select(".news-item__title > a").attr("title");
                    thumb = e.select(".dt-thumbnail > img").attr("lazy-src");
                    desc = e.select(".news-item__content > a").text();
                    time = e.select(".news-item__time").text();
                    cate = e.select(".news-item__meta > a").attr("title");

                    if (link!="" && title!= "" && thumb!="" && desc!="" )
                        items.add(new Article(link, title, thumb, desc, time, cate));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            items.addAll(moreItems);
            newsAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    }

    class GetSubcates extends AsyncTask<String, Void, String> implements SubcateAdapter.ItemClickListener {

        Elements sc;
        ArrayList<Subcate> listSubcate = new ArrayList<Subcate>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                listSubcate = new ArrayList<Subcate>();
                //Connect to the website
                Document document = Jsoup.connect(strings[0]).get();

                sc = document.getElementsByClass("category-navigation__item");

                String link ="";
                String title ="";
                String[] temp;
                for (Element e : sc){
                    link = e.select("a").attr("href");
                    title = e.select("a").text();
                    temp = link.split("/");
                    link = temp[2].substring(0, temp[2].length()-4);

                    if (link!="" && title!= "")
                        listSubcate.add(new Subcate(link, title, false));
                }

                if (listSubcate.size()>0) listSubcate.add(0, new Subcate("", "Tất cả", true));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            subcateAdapter = new SubcateAdapter(getContext(), listSubcate);
            subcateAdapter.setClickListener(this);
            recyclerView.setAdapter(subcateAdapter);
            content = new Content();
            content.execute(url+".htm");
            currentUrl = url;

        }

        @Override
        public void onItemClick(View view, int position) {
            for (int i=0; i<listSubcate.size(); i++){
                if (i==position) listSubcate.get(i).setSelected(true);
                else listSubcate.get(i).setSelected(false);
            }
            subcateAdapter.notifyDataSetChanged();

            if (position==0){
                items.clear();
                newsAdapter.notifyDataSetChanged();
                page = 1;
                content = new Content();
                content.execute(url+ "/trang-" + page + ".htm");
                currentUrl = url;
            }else {
                items.clear();
                newsAdapter.notifyDataSetChanged();
                page = 1;
                content = new Content();
                content.execute(url + "/" + subcateAdapter.getItem(position).getUrl() + "/trang-" + page +".htm");
                currentUrl = url + "/" + subcateAdapter.getItem(position).getUrl();
            }
        }
    }


}