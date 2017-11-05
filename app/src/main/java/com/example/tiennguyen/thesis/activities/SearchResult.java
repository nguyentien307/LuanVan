package com.example.tiennguyen.thesis.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tiennguyen.thesis.MainActivity;
import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.adapters.SongAdapter;
import com.example.tiennguyen.thesis.fragment.HomeFm;
import com.example.tiennguyen.thesis.fragment.SearchingFm;
import com.example.tiennguyen.thesis.fragment.UserFm;
import com.example.tiennguyen.thesis.interfaces.ScreenShotable;
import com.example.tiennguyen.thesis.model.PersonItem;
import com.example.tiennguyen.thesis.model.SongItem;
import com.example.tiennguyen.thesis.service.BaseURI;
import com.example.tiennguyen.thesis.service.GetData;
import com.example.tiennguyen.thesis.util.MyHandle;
import com.example.tiennguyen.thesis.util.ViewAnimator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class SearchResult extends AppCompatActivity {

    private ProgressBar pbSearchLoading;
    private LinearLayout llSearchResult;
    private TextView tvSearchTitle;
    private TextView tvSongNum;
    private ListView lvSearchResult;

    private MaterialSearchView searchView;
    private FrameLayout flSearchProperty;

    private SongAdapter songAdapter;
    private String data = "";
    private int startSearchIndex = 0;
    private boolean isLoading = false;
    private View footerView;
    MyHandle myHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initView();
    }

    private void initView() {
        initialView();
        setActionBar();
        showResult();
        event();
    }

    private void initialView() {
        pbSearchLoading = (ProgressBar) findViewById(R.id.pb_search_loading);
        llSearchResult = (LinearLayout) findViewById(R.id.ll_search_result);
        tvSearchTitle = (TextView) findViewById(R.id.tv_search_title);
        tvSongNum = (TextView) findViewById(R.id.tv_song_num);
        lvSearchResult = (ListView) findViewById(R.id.lv_search_sesult);
        
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        flSearchProperty = (FrameLayout) findViewById(R.id.fl_search_property);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = li.inflate(R.layout.footer_view, null);
        myHandle = new MyHandle(myHandle());
    }

    private MyHandle.HandleFooterView myHandle() {
        return new MyHandle.HandleFooterView() {
            @Override
            public void setFooterView(Message msg) {
                lvSearchResult.addFooterView(footerView);
            }

            @Override
            public void setMoreListView(Message msg) {
                songAdapter.addListItemToAdapter((ArrayList<SongItem>) msg.obj);
                lvSearchResult.removeFooterView(footerView);
                isLoading = false;
            }
        };
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showResult() {
        setLoading(true);
        data = getIntent().getStringExtra("data");
//        String searchTitle = getIntent().getStringExtra("title");
//        tvSearchTitle.setText("Result for " + searchTitle);
        BaseURI baseURI = new BaseURI();
        GetData getData = new GetData();
        getData.execute(baseURI.getSearchedSong(data, "song", startSearchIndex, startSearchIndex + 10));
        startSearchIndex += 10;
        getData.setDataDownloadListener(new GetData.DataDownloadListener() {

            @Override
            public void dataDownloadedSuccessfully(JSONObject data) {
                setLoading(false);
                displayList(data, false);
            }

            @Override
            public void dataDownloadFailed() {
            }
        });
    }

    private void event() {
        lvSearchResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() == totalItemCount - 1 && lvSearchResult.getCount() >= 10 && isLoading == false) {
                    isLoading = true;
                    Thread thread = new ThreadGetMoreData();
                    thread.start();
                }
            }
        });
    }

    private ArrayList<SongItem> displayList(JSONObject data, boolean isMore) {
        ArrayList<SongItem> songArr = new ArrayList<>();
        try {
            String numFound = data.getString("total");
            JSONArray songList = data.getJSONArray("list");
            for(int i = 0; i < songList.length(); i++) {
                JSONObject jsObject = songList.getJSONObject(i);
                ArrayList<PersonItem> artists = getSingerList(jsObject);
                SongItem item = new SongItem(jsObject.getString("title"),123, jsObject.getString("href"), artists, null, "");
                songArr.add(item);
            }
            if (isMore == false) {
                setAdapter(songArr, numFound);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songArr;
    }

    private ArrayList<PersonItem> getSingerList(JSONObject jsObject) {
        ArrayList<PersonItem> artists = new ArrayList<>();
        try {
            JSONArray jsArr = jsObject.getJSONArray("singers");
            for (int i = 0; i < jsArr.length(); i++) {
                JSONObject object = jsArr.getJSONObject(i);
                PersonItem item = new PersonItem(object.getString("singerName"), object.getString("singerHref"), 142);
                artists.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  artists;
    }

    private void setAdapter(ArrayList<SongItem> searchingArray, String numFound) {
        tvSongNum.setText("Have " + numFound + " results is founded for '" + data + "'");
        songAdapter = new SongAdapter(this, R.layout.song_searched_item, searchingArray);
        lvSearchResult.setAdapter(songAdapter);
    }

    private void setLoading(boolean b) {
        if (b == true) {
            llSearchResult.setVisibility(View.INVISIBLE);
            pbSearchLoading.setVisibility(View.VISIBLE);
        } else {
            llSearchResult.setVisibility(View.VISIBLE);
            pbSearchLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView.setMenuItem(search);
        searched(searchView);
        return true;
    }

    private void searched(MaterialSearchView searchView) {
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                ScreenShotable screenShotable = new SearchingFm();
                replaceFragment(screenShotable);
            }

            @Override
            public void onSearchViewClosed() {
                flSearchProperty.setVisibility(View.GONE);
            }
        });
    }

    private ScreenShotable replaceFragment(ScreenShotable screenShotable) {
        //this.res = this.res == R.drawable.content_music ? R.drawable.content_films : R.drawable.content_music;
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 370, 0, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();
        //ContentFragment contentFragment = ContentFragment.newInstance(name);
        ScreenShotable screenShot = new SearchingFm();
        getSupportFragmentManager().beginTransaction().replace(R.id.cl_rearch_result, (Fragment) screenShot).commit();
        return screenShot;
    }

    public ArrayList<SongItem> getMoreData() {
        ArrayList<SongItem> footerSongArray = new ArrayList<>();
        BaseURI baseURI = new BaseURI();
        GetData getData = new GetData();
        getData.execute(baseURI.getSearchedSong(data, "song", startSearchIndex, startSearchIndex + 10));
        startSearchIndex += 10;
        getData.setDataDownloadListener(new GetData.DataDownloadListener() {

            @Override
            public void dataDownloadedSuccessfully(JSONObject data) {
                Message msg = myHandle.obtainMessage(1, displayList(data, true));
                myHandle.sendMessage(msg);
            }

            @Override
            public void dataDownloadFailed() {
            }
        });
        return footerSongArray;
    }

    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            myHandle.sendEmptyMessage(0);
            getMoreData();
        }
    }
}
