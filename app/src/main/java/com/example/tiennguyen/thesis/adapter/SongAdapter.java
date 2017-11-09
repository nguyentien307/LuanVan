package com.example.tiennguyen.thesis.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.model.PersonItem;
import com.example.tiennguyen.thesis.model.SongItem;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 11/8/2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    ArrayList<SongItem> arrSongs;
    private Context ctx;
    Boolean isInAlbum;

    public SongAdapter (ArrayList<SongItem> arrSongs, Context ctx, Boolean isInAlbum){
        this.arrSongs = arrSongs;
        this.ctx = ctx;
        this.isInAlbum = isInAlbum;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView ;
        if (isInAlbum){
            itemView = inflater.inflate(R.layout.album_song, parent, false);
        }
        else {
            itemView = inflater.inflate(R.layout.song_card, parent, false);
        }
        SongViewHolder svh = new SongViewHolder(itemView);
        return svh;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/mystical.ttf");
        holder.tvSongTitle.setTypeface(typeface);
        holder.tvSongTitle.setText(arrSongs.get(position).getTitle());

        if (isInAlbum) {
            holder.tvIndex.setText(position + 1 +"");
        } else {
            ArrayList<PersonItem> singers = arrSongs.get(position).getArtist();
            String singerName = "";
            for (int singerIndex = 0; singerIndex < singers.size(); singerIndex++) {
                if (singerIndex == singers.size() - 1) {
                    singerName += singers.get(singerIndex).getName();
                } else {
                    singerName += singers.get(singerIndex).getName() + ", ";
                }
            }
            if (singerName != "") {
                holder.tvSongSingers.setText(singerName);
            } else holder.tvSongSingers.setText("khong co");
            if (arrSongs.get(position).getLinkImg() != "") {
                Glide.with(ctx)
                        .load(arrSongs.get(position).getLinkImg())
                        .centerCrop()
                        .placeholder(R.drawable.item_down)
                        .error(R.drawable.item_up)
                        .into(holder.imgSong);
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrSongs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvSongTitle;
        TextView tvIndex;

        CardView cvSong;
        TextView tvSongSingers;
        ImageView imgSong;
        ImageView btnAdd;
        ImageView btnPlay;

        SongViewHolder(View itemView) {
            super(itemView);
            if(itemView.getId() == R.id.song_card) {
                cvSong = (CardView) itemView.findViewById(R.id.cvSong);
                tvSongTitle = (TextView) itemView.findViewById(R.id.tvSongTitle);
                tvSongSingers = (TextView) itemView.findViewById(R.id.tvSongSinger);
                imgSong = (ImageView) itemView.findViewById(R.id.imgSong);
                btnAdd = (ImageView) itemView.findViewById(R.id.btnAdd);
                btnPlay = (ImageView) itemView.findViewById(R.id.btnPlay);

                btnPlay.setOnClickListener(this);
                btnAdd.setOnClickListener(this);
            }
            else {
                tvSongTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvIndex = (TextView) itemView.findViewById(R.id.tvIndex);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnAdd: {
                    Toast.makeText(ctx,"Add", Toast.LENGTH_SHORT).show();
                }; break;

                case R.id.btnPlay: {
                    Toast.makeText(ctx, "play", Toast.LENGTH_SHORT).show();
                }; break;

                default: Toast.makeText(ctx, getAdapterPosition() + "", Toast.LENGTH_SHORT).show();break;
            }

        }
    }
}