package com.example.tiennguyen.thesis.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tiennguyen.thesis.Fab;
import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.adapter.AlbumAdapter;
import com.example.tiennguyen.thesis.adapter.CategoryAdapter;
import com.example.tiennguyen.thesis.interfaces.ScreenShotable;
import com.example.tiennguyen.thesis.model.AlbumItem;
import com.example.tiennguyen.thesis.model.CategoryItem;
import com.example.tiennguyen.thesis.model.PersonItem;
import com.example.tiennguyen.thesis.util.Constants;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 11/3/2017.
 */

public class AlbumFm extends Fragment implements ScreenShotable {

    private View containerView;
    private Bitmap bitmap;


    private RecyclerView recyclerView;
    private AlbumAdapter adapter;
    private ArrayList<AlbumItem> arrAlbums;
    private ImageView ivTopBgCover;
    private TextView tvIntro;
    private RecyclerView.LayoutManager layoutManger;

    private RecyclerView rcViewCategory;
    ArrayList<CategoryItem> arrCategory = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Constants c;

    MaterialSheetFab materialSheetFab;

    String res = "";

    public static AlbumFm newInstance(String name) {
        AlbumFm contentFragment = new AlbumFm();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
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
        if (getArguments()!= null)
            res = getArguments().getString("name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_album, viewGroup, false);

        ivTopBgCover = (ImageView) view.findViewById(R.id.ivTopBgCover);
        tvIntro = (TextView) view.findViewById(R.id.tvIntro);
        if(res != ""){
            tvIntro.setText(res);
        }

        Glide.with(getContext()).load(R.drawable.backgroundcover)
                .centerCrop()
                .placeholder(R.drawable.item_down)
                .error(R.drawable.item_up)
                .into(ivTopBgCover);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        arrAlbums = new ArrayList<>();
        adapter = new AlbumAdapter(getContext(), arrAlbums);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new AlbumAdapter.GridSpacingItemDecoration(2, adapter.dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        prepareAlbums();


        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.fab_sheet_color);
        int fabColor = getResources().getColor(R.color.fab_color);


        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);
        //materialSheetFab.showSheet();
        //fab.setVisibility(View.VISIBLE);

        //materialSheetFab.showFab();



        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Called when the material sheet's "show" animation starts.
            }

            @Override
            public void onSheetShown() {
                // Called when the material sheet's "show" animation ends.
            }

            @Override
            public void onHideSheet() {
                // Called when the material sheet's "hide" animation starts.
            }

            public void onSheetHidden() {
            }
        });




        rcViewCategory = (RecyclerView) view.findViewById(R.id.rcViewCategory);
        rcViewCategory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rcViewCategory.setLayoutManager(layoutManager);
        setData();

        return view;
    }

    private void prepareAlbums(){
        JSONObject data = null;
        try {
            data = new JSONObject(Constants.data);

            JSONArray albumListJSON = data.getJSONArray("list");
            for (int albIndex = 0; albIndex < albumListJSON.length(); albIndex++) {
                JSONObject album = albumListJSON.getJSONObject(albIndex);
                String title = album.getString("title");
                String img = album.getString("img");
                String href = album.getString("href");
                JSONArray singersJSON = album.getJSONArray("singers");
                ArrayList<PersonItem> arrSinger = new ArrayList<PersonItem>();
                for (int singerIndex = 0; singerIndex < singersJSON.length(); singerIndex++) {
                    JSONObject singer = singersJSON.getJSONObject(singerIndex);
                    String singerName = singer.getString("singerName");
                    String singerHref = singer.getString("singerHref");
                    PersonItem singerItem = new PersonItem(singerName, singerHref, 100);
                    arrSinger.add(singerItem);
                }

                AlbumItem albumItem = new AlbumItem(title, href, img, 200, arrSinger);

                arrAlbums.add(albumItem);

            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    private void prepareAlbums() {
//        GetData getData = new GetData(getContext());
//        BaseURI baseURI = new BaseURI();
//        String link = "https://web-service-app.herokuapp.com/album/album-hot-list";
//        getData.setDataDownloadListener(new GetData.DataDownloadListener() {
//            @Override
//            public void dataDownloadedSuccessfully(JSONObject data) {
//                try {
//
//                    JSONArray albumListJSON = data.getJSONArray("list");
//                    for (int albIndex = 0; albIndex < albumListJSON.length(); albIndex++) {
//                        JSONObject album = albumListJSON.getJSONObject(albIndex);
//                        String title = album.getString("title");
//                        String img = album.getString("img");
//                        String href = album.getString("href");
//                        JSONArray singersJSON = album.getJSONArray("singers");
//                        ArrayList<PersonItem> arrSinger = new ArrayList<PersonItem>();
//                        for (int singerIndex = 0; singerIndex < singersJSON.length(); singerIndex++) {
//                            JSONObject singer = singersJSON.getJSONObject(singerIndex);
//                            String singerName = singer.getString("singerName");
//                            String singerHref = singer.getString("singerHref");
//                            PersonItem singerItem = new PersonItem(singerName, singerHref, 100);
//                            arrSinger.add(singerItem);
//                        }
//
//                        AlbumItem albumItem = new AlbumItem(title, href, img, 200, arrSinger);
//
//                        arrAlbums.add(albumItem);
//
//                    }
//
//
//
////                    AlbumAdapter adapter = new AlbumAdapter(getContext(), albumList, false);
////                    rccViewAlbum.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void dataDownloadFailed() {
//
//            }
//        });
//        getData.execute(link);
//    }


    private void setData() {
        arrCategory.clear();
        CategoryItem item1 = new CategoryItem("Nhạc trẻ", "");
        arrCategory.add(item1);
        CategoryItem item2 = new CategoryItem("Nhạc Cách Mạng", "");
        arrCategory.add(item2);
        CategoryItem item3 = new CategoryItem("Nhạc Trữ tình", "");
        arrCategory.add(item3);
        CategoryItem item4 = new CategoryItem("Nhạc Dân ca", "");
        arrCategory.add(item4);

        CategoryAdapter adapter = new CategoryAdapter(arrCategory, getContext(), materialSheetFab, Constants.ALBUM_TYPE);
        rcViewCategory.setAdapter(adapter);
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
                AlbumFm.this.bitmap = bitmap;
            }
        };

        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
