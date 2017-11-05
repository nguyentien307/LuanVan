package com.example.tiennguyen.thesis.fragment;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AlertDialog;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.AccelerateInterpolator;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.example.tiennguyen.thesis.MainActivity;
        import com.example.tiennguyen.thesis.R;
        import com.example.tiennguyen.thesis.activities.SearchResult;
        import com.example.tiennguyen.thesis.interfaces.ScreenShotable;
        import com.example.tiennguyen.thesis.util.StringUtils;
        import com.miguelcatalan.materialsearchview.MaterialSearchView;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.util.ArrayList;

        import io.codetail.animation.SupportAnimator;
        import io.codetail.animation.ViewAnimationUtils;

public class SearchingFm extends Fragment implements ScreenShotable {

    private MaterialSearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searching, viewGroup, false);
        setInitial(view);
        searched();
        return view;
    }

    private void setInitial(View view) {
        searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);

    }

    private void searched() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                StringUtils convertedToUnsigned = new StringUtils();
                String name = convertedToUnsigned.convertedToUnsigned(query);
                Intent intent = new Intent(getActivity(), SearchResult.class);
                intent.putExtra("data", name);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Request infomation from url search
                return false;
            }
        });
    }

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

}
