package com.p2ild.hentai24h.Activity;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.p2ild.hentai24h.ContentHentaiAdapter;
import com.p2ild.hentai24h.OnItemTouch;
import com.p2ild.hentai24h.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by duypi on 8/29/2016.
 */
public class ContentHentaiActivity extends Activity {
    private static final String TAG = ContentHentaiActivity.class.getSimpleName();
    private RecyclerView rcv;
    private ContentHentaiAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new ContentHentaiAdapter(ContentHentaiActivity.this, (ArrayList<String>) msg.obj);
            rcv.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        rcv = (RecyclerView) findViewById(R.id.rcv);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcv.addOnItemTouchListener(new OnItemTouch(ContentHentaiActivity.this, rcv, new OnItemTouch.ItemClick() {
            @Override
            public void onClick(View viewChild, int position) {

            }

            @Override
            public void onLongClick(View viewChild, int position) {

            }

            @Override
            public void onZoom(Matrix matrix) {
                Log.d(TAG, "onZoom: "+matrix);
            }
        }));
        final String urlHentai = getIntent().getStringExtra(MainActivity.URL_HENTAI);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadContentImageFromUrl(urlHentai);
            }
        });
        thread.start();
    }

    private void loadContentImageFromUrl(String link) {
        ArrayList<String> arrSrc = new ArrayList<>();
        try {
            Document dm = Jsoup.connect(link).get();
            Elements chapter = dm.select("div#chapter");
            Elements img = chapter.select("img");
            for (int i = 0; i < img.size(); i++) {
                Element onePic = img.get(i);
                arrSrc.add(onePic.attr("src"));
            }
            Message message = new Message();
            message.obj = arrSrc;
            message.setTarget(handler);
            message.sendToTarget();
        } catch (IOException e) {
            Log.d(TAG, "loadImage: Lỗi đéo biết ở đâu: " + e);
            e.printStackTrace();
        }
    }

}
