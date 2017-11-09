package com.example.tiennguyen.thesis.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.adapter.SongAdapter;
import com.example.tiennguyen.thesis.model.AlbumItem;
import com.example.tiennguyen.thesis.model.PersonItem;
import com.example.tiennguyen.thesis.model.SongItem;
import com.example.tiennguyen.thesis.service.BaseURI;
import com.example.tiennguyen.thesis.service.GetData;
import com.example.tiennguyen.thesis.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumSongsFm extends Fragment {
    private static final String ALBUM_ITEM = "albItem";

    private AlbumItem albumItem;

    ImageView imgViewAlbumBG;
    RecyclerView rccViewAlbumSongs;
    TextView tvAlbumTitle, tvAlbumSingers;

    ArrayList<SongItem> arrSongs = new ArrayList<>();
    BaseURI baseURI = new BaseURI();

    public AlbumSongsFm() {
        // Required empty public constructor
    }

    public static AlbumSongsFm newInstance(AlbumItem albumItem) {
        AlbumSongsFm fragment = new AlbumSongsFm();
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_ITEM, albumItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumItem = (AlbumItem) getArguments().getSerializable(ALBUM_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fm_album_songs, container, false);
        imgViewAlbumBG = (ImageView) view.findViewById(R.id.imgViewAlbumBG);

        Glide.with(getContext()).load(albumItem.getLinkImg())
                .centerCrop()
                .placeholder(R.drawable.item_down)
                .error(R.drawable.item_up)
                .into(imgViewAlbumBG);

        tvAlbumTitle = (TextView) view.findViewById(R.id.tvAlbumTitle);
        tvAlbumSingers = (TextView) view.findViewById(R.id.tvAlbumSingers);

        tvAlbumTitle.setText(albumItem.getName());

        ArrayList<PersonItem> singers =  albumItem.getSingers();
        String singerName = "";
        for (int singerIndex = 0; singerIndex < singers.size(); singerIndex ++ ){
            if (singerIndex == singers.size() - 1){
                singerName += singers.get(singerIndex).getName();
            }
            else {
                singerName += singers.get(singerIndex).getName() + ", ";
            }
        }
        if(singerName!="") {
            tvAlbumSingers.setText(singerName);
        }
        else tvAlbumSingers.setText("khong co");

//        View backgroundimage = view.findViewById(R.id.llBackground);
//        Drawable background = backgroundimage.getBackground();
//        background.setAlpha(80);

        rccViewAlbumSongs = (RecyclerView) view.findViewById(R.id.rccViewAlbumSongs);
        rccViewAlbumSongs.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rccViewAlbumSongs.setLayoutManager(llm);

        setData(albumItem.getLink());

        return view;
    }

    private void setData(String hr) {
        GetData getdata = new GetData(getContext());

        try{
            JSONObject data = new JSONObject(Constants.album_song_data);
            JSONArray songListJSON = data.getJSONArray("list");
            for(int songIndex = 0; songIndex < songListJSON.length() ; songIndex++) {
                JSONObject song = songListJSON.getJSONObject(songIndex);
                String title = song.getString("title");
                String img = "";
                String href = song.getString("href");
                JSONArray singersJSON = song.getJSONArray("singers");
                ArrayList<PersonItem> arrSinger = new ArrayList<PersonItem>();
                ArrayList<PersonItem> arrComposer = new ArrayList<PersonItem>();
                for (int singerIndex = 0; singerIndex < singersJSON.length(); singerIndex++) {
                    JSONObject singer = singersJSON.getJSONObject(singerIndex);
                    String singerName = singer.getString("singerName");
                    String singerHref = singer.getString("singerHref");
                    PersonItem singerItem = new PersonItem(singerName, singerHref, 200);
                    arrSinger.add(singerItem);
                }

                PersonItem composer = new PersonItem("Nhạc sĩ", "", 200 );
                arrComposer.add(composer);
                SongItem songItem = new SongItem(title,200, href, arrSinger, arrComposer,"", img);
                arrSongs.add(songItem);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        SongAdapter adapter = new SongAdapter(arrSongs, getContext(), true);
        rccViewAlbumSongs.setAdapter(adapter);

    }

}
