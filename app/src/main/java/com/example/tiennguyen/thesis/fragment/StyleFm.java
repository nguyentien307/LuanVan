package com.example.tiennguyen.thesis.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.interfaces.ScreenShotable;

/**
 * Created by TIENNGUYEN on 11/3/2017.
 */

public class StyleFm extends Fragment implements ScreenShotable {

    private View containerView;
    private Bitmap bitmap;

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
        View view = inflater.inflate(R.layout.fm_style, viewGroup, false);
        TextView output= (TextView)view.findViewById(R.id.msg1);
        output.setText("Fragment Style");
        return view;
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
                StyleFm.this.bitmap = bitmap;
            }
        };

        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
