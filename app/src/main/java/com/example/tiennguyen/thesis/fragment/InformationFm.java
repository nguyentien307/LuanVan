package com.example.tiennguyen.thesis.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.adapter.TopicAdapter;
import com.example.tiennguyen.thesis.interfaces.ScreenShotable;
import com.example.tiennguyen.thesis.model.TopicItem;
import com.example.tiennguyen.thesis.util.Constants;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 11/5/2017.
 */

public class InformationFm extends Fragment implements ScreenShotable {

    private View containerView;
    private Bitmap bitmap;

    private RecyclerView rcViewInfoTopic;
    ArrayList<TopicItem> arrTopicItems = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Constants c;

//    public static HomeFm newInstance(String name) {
//        HomeFm contentFragment = new HomeFm();
//        Bundle bundle = new Bundle();
//        bundle.putString(String.class.getName(), name);
//        contentFragment.setArguments(bundle);
//
//        return contentFragment;
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = view.findViewById(R.id.container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //res = getArguments().getString(String.class.getName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_information, viewGroup, false);
        rcViewInfoTopic = (RecyclerView) view.findViewById(R.id.rcViewInfoTopic);
        rcViewInfoTopic.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rcViewInfoTopic.setLayoutManager(layoutManager);
        showTopic();
        return view;
    }

    private void showTopic() {
        arrTopicItems.clear();
        TopicItem itemUser = new TopicItem(c.USER_TITLE, c.USER_DETAIL, R.drawable.icons8_user_50);
        arrTopicItems.add(itemUser);
        TopicItem itemHome = new TopicItem(c.HOME_TITLE, c.HOME_DETAIL, R.drawable.icons8_home_page_50);
        arrTopicItems.add(itemHome);
        TopicItem itemAlbum = new TopicItem(c.ALBUM_TITLE, c.ALBUM_DETAIL, R.drawable.icons8_rhythm_50);
        arrTopicItems.add(itemAlbum);
        TopicItem itemCategory = new TopicItem(c.CATEGORY_TITLE, c.CATEGORY_DETAIL, R.drawable.icons8_music_folder);
        arrTopicItems.add(itemCategory);
        TopicItem itemStyle = new TopicItem(c.STYLE_TITLE, c.STYLE_DETAIL, R.drawable.icons8_treble_clef);
        arrTopicItems.add(itemStyle);
        TopicItem itemPlaylist = new TopicItem(c.PLAYLIST_TITLE, c.PLAYLIST_DETAIL, R.drawable.icons8_playlist);
        arrTopicItems.add(itemPlaylist);

        TopicAdapter adapter = new TopicAdapter(arrTopicItems);
        rcViewInfoTopic.setAdapter(adapter);
    }


    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                                containerView.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        containerView.draw(canvas);
                        InformationFm.this.bitmap = bitmap;
                    }
                });
            }
        };

        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
