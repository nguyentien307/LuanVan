package com.example.tiennguyen.thesis.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.interfaces.ScreenShotable;

/**
 * Created by TIENNGUYEN on 11/3/2017.
 */

public class ContentFm extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String USER = "User";
    public static final String HOME = "Home";
    public static final String ALBUM = "Album";
    public static final String SONG_CATAGORIES = "Catagories";
    public static final String SONG_MUSIC_TUNE = "Tune";
    public static final String INFORMATION = "Info";

    private View containerView;
    protected ImageView mImageView;
    protected String res;
    private Bitmap bitmap;

    public static ContentFm newInstance(String name) {
        ContentFm contentFragment = new ContentFm();
        Bundle bundle = new Bundle();
        bundle.putString(String.class.getName(), name);
        contentFragment.setArguments(bundle);

        return contentFragment;
    }


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_content, container, false) ;

        return rootView;
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                        containerView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                ContentFm.this.bitmap = bitmap;
            }
        };

        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
