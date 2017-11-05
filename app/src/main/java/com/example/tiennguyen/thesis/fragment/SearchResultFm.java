package com.example.tiennguyen.thesis.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.activities.SearchResult;
import com.example.tiennguyen.thesis.adapters.SongAdapter;
import com.example.tiennguyen.thesis.interfaces.ScreenShotable;
import com.example.tiennguyen.thesis.model.PersonItem;
import com.example.tiennguyen.thesis.model.SongItem;
import com.example.tiennguyen.thesis.service.BaseURI;
import com.example.tiennguyen.thesis.service.GetData;
import com.example.tiennguyen.thesis.util.MyHandle;
import com.example.tiennguyen.thesis.util.StringUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResultFm extends Fragment implements ScreenShotable {

    private ProgressBar pbSearchLoading;
    private LinearLayout llSearchResult;
    private TextView tvSongNum;
    private ListView lvSearchResult;

    private SongAdapter songAdapter;
    private String data = "";
    private int startSearchIndex = 1;
    private boolean isLoading = false;
    private View footerView;
    MyHandle myHandle;

    public static SearchResultFm newInstance(String name) {
        SearchResultFm contentFragment = new SearchResultFm();
        Bundle bundle = new Bundle();
        bundle.putString(String.class.getName(), name);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result_fm, viewGroup, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initialView(view);
        showResult();
    }

    private void initialView(View view) {
        pbSearchLoading = (ProgressBar) view.findViewById(R.id.pb_search_loading);
        llSearchResult = (LinearLayout) view.findViewById(R.id.ll_search_result);
        tvSongNum = (TextView) view.findViewById(R.id.tv_song_num);
        lvSearchResult = (ListView) view.findViewById(R.id.lv_search_sesult);

//        searchView = (MaterialSearchView) findViewById(R.id.search_view);
//        flSearchProperty = (FrameLayout) findViewById(R.id.fl_search_property);

        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void showResult() {
        setLoading(true);
        Bundle bundle = getArguments();
        if (bundle != null) data = bundle.getString(String.class.getName());
        StringUtils convertedToUnsigned = new StringUtils();
        String name = convertedToUnsigned.convertedToUnsigned(data);
        BaseURI baseURI = new BaseURI();
        GetData getData = new GetData();
        getData.execute(baseURI.getSearchedSong(name, "song", startSearchIndex, startSearchIndex + 10));
        startSearchIndex += 10;
        getData.setDataDownloadListener(new GetData.DataDownloadListener() {

            @Override
            public void dataDownloadedSuccessfully(JSONObject data) {
                setLoading(false);
                displayList(data, false);
                event();
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
        songAdapter = new SongAdapter(getActivity(), R.layout.song_searched_item, searchingArray);
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

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}
