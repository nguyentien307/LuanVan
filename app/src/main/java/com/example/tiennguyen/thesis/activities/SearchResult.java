package com.example.tiennguyen.thesis.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.fragment.SearchResultFm;
import com.example.tiennguyen.thesis.fragment.SearchingFm;

import com.example.tiennguyen.thesis.interfaces.ScreenShotable;
import com.example.tiennguyen.thesis.util.SearchDialog;
import com.example.tiennguyen.thesis.util.ViewAnimator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class SearchResult extends AppCompatActivity {

    private MaterialSearchView searchView;
    private FrameLayout flSearchProperty;

    private String searchTitle = "";

    ScreenShotable screenShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initView();
    }

    private void initView() {
        initialView();
        setActionBar();
        getFragment();
    }

    private void getFragment() {
        String data = getIntent().getStringExtra("data");
        searchTitle = getIntent().getStringExtra("title");

        screenShot = new SearchResultFm();
        SearchResultFm searchResultFm = SearchResultFm.newInstance(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_content_frame, searchResultFm).commit();
    }

    private void initialView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        flSearchProperty = (FrameLayout) findViewById(R.id.fl_search_property);
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView.setMenuItem(search);
        searched(searchView);
        return true;
    }

    private void searched(final MaterialSearchView searchView) {
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchTitle = "song";
                searchView.setHint("Searching for " + searchTitle);
                flSearchProperty.setVisibility(View.VISIBLE);
                eventForSearch();
                screenShot = new SearchingFm();
                getSupportFragmentManager().beginTransaction().replace(R.id.ll_content_frame, (Fragment) screenShot).commit();
            }

            @Override
            public void onSearchViewClosed() {
                flSearchProperty.setVisibility(View.GONE);
            }
        });
    }

    private void eventForSearch() {
        flSearchProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog showSearchDialog = new SearchDialog(new SearchDialog.CustomLayoutInflater() {
                    @Override
                    public LayoutInflater getLayoutInflater() {
                        return SearchResult.this.getLayoutInflater();
                    }

                    @Override
                    public AlertDialog.Builder getAlertDialog() {
                        return new AlertDialog.Builder(SearchResult.this);
                    }

                    @Override
                    public void onResult(String title) {
                        searchTitle = title;
                        searchView.setHint("Searching for " + searchTitle);
                    }

                    @Override
                    public String getCheckedTitle() {
                        return searchTitle;
                    }
                });

                showSearchDialog.displaySearchDialog();
            }
        });
    }
}
