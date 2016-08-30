package com.p2ild.hentai24h.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.p2ild.hentai24h.Hentai;
import com.p2ild.hentai24h.ListHentaiAdapter;
import com.p2ild.hentai24h.OnItemTouch;
import com.p2ild.hentai24h.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    public static final String URL_HENTAI = "URL_HENTAI";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int DATA = 111;
    private static final String HOME_PAGE = "http://hentai24h.net/";
    private static final int DATA_HOMEPAGE = 222;
    private static final int DATA_CATALOG = 333;
    public ArrayList<Hentai> arrayListHentai = new ArrayList<>();
    private RecyclerView rcv;
    private DrawerLayout drw;
    private GridView lst;
    private ListHentaiAdapter listHentaiAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATA_HOMEPAGE) {
                arrayListHentai.clear();
                arrayListHentai.addAll((Collection<? extends Hentai>) msg.obj);
                rcv.getAdapter().notifyDataSetChanged();
            }
            if (msg.what == DATA_CATALOG) {
                CatalogAdapter adapter = new CatalogAdapter((ArrayList<Catalog>) msg.obj);
                lst.setAdapter(adapter);
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadHentaiFromHomePage();
        }
    };
    private boolean isBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(runnable);
        thread.start();

        drw = (DrawerLayout) findViewById(R.id.drw);
        isBackPress = false;
        initDrawerContent();
        initDrawerMenu();
    }

    private void initDrawerMenu() {
        lst = (GridView) findViewById(R.id.lst);
        lst.setOnItemClickListener(this);
    }

    private void initDrawerContent() {
        rcv = (RecyclerView) findViewById(R.id.rcv);
        rcv.hasFixedSize();
        rcv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rcv.addOnItemTouchListener(new OnItemTouch(MainActivity.this, rcv, new OnItemTouch.ItemClick() {
            @Override
            public void onClick(View viewChild, int position) {
                Intent intent = new Intent(MainActivity.this, ContentHentaiActivity.class);
                String url = arrayListHentai.get(position).getLinkTruyen();
                intent.putExtra(URL_HENTAI, url);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View viewChild, int position) {
            }

            @Override
            public void onZoom(Matrix matrix) {
            }
        }));
        listHentaiAdapter = new ListHentaiAdapter(this,arrayListHentai);
        rcv.setAdapter(listHentaiAdapter);
    }

    private void loadHentaiFromHomePage() {
        try {
            Document document = Jsoup.connect(HOME_PAGE).get();
            getAllCatalog(document);
            getAllHentaiInCurrenPage(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*get all catalog in Home page*/
    private void getAllCatalog(Document document) {
        ArrayList<Catalog> arrCtl = new ArrayList<>();
        Elements idMenu = document.select("ul#menu-the-loai");
        Elements li = idMenu.select("li");
        for (int i = 0; i < li.size(); i++) {
            String nameCatalog = li.get(i).text();
            String url = li.get(i).select("a").get(0).attr("href");
            arrCtl.add(new Catalog(url, nameCatalog));
        }
        Message msgCatalog = new Message();
        msgCatalog.what = DATA_CATALOG;
        msgCatalog.obj = arrCtl;
        handler.sendMessage(msgCatalog);
    }

    /*get all hentai in Home page*/
    private void getAllHentaiInCurrenPage(Document document) {
        ArrayList<Hentai> arrHentai = new ArrayList<>();
        Elements idComic = document.select("div#comic");
        Elements classItems = idComic.select("ul.items");
        Elements arrLi = classItems.select("li");
        for (int i = 0; i < arrLi.size(); i++) {
            String linkTruyen = arrLi.get(i).select("a").get(0).attr("href");
            String linkImgPreview = arrLi.get(i).select("a").get(0).select("img").get(0).attr("src");
            String tenTruyen = arrLi.get(i).select("a").get(0).attr("title");
            arrHentai.add(new Hentai(linkImgPreview, tenTruyen, linkTruyen));
        }
        Message msgHomePage = new Message();
        msgHomePage.what = DATA_HOMEPAGE;
        msgHomePage.obj = arrHentai;
        handler.sendMessage(msgHomePage);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Catalog chooseCatalog = (Catalog) adapterView.getAdapter().getItem(i);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getAllHentaiInCurrenPage(Jsoup.connect(chooseCatalog.getUrlCatalog()).get());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        if(drw.isDrawerOpen(Gravity.LEFT)){
            drw.closeDrawer(Gravity.LEFT);
        }
        Log.d(TAG, "onBackPressed: isBackPress:"+isBackPress);
        Toast.makeText(this,"Muốn thoát khỏi app hentai này các bạn phải ấn back 2 lần nhé",Toast.LENGTH_SHORT).show();
        if(isBackPress){
            super.onBackPressed();
        }
        isBackPress=true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPress = false;
            }
        }, 200);
    }
}
