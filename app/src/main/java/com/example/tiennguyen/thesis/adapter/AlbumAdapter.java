package com.example.tiennguyen.thesis.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.fragment.AlbumSongsFm;
import com.example.tiennguyen.thesis.model.AlbumItem;
import com.example.tiennguyen.thesis.model.PersonItem;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 11/8/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AlbumItem> arrAlbums;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle, tvSingers, tvViews;
        public ImageView ivBgCover, overflow;
        public CardView albumCard;

        public ViewHolder(View view) {
            super(view);
            albumCard = (CardView) view.findViewById(R.id.album_card);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvSingers = (TextView) view.findViewById(R.id.tvSingers);
            tvViews = (TextView) view.findViewById(R.id.tvViews);
            ivBgCover = (ImageView) view.findViewById(R.id.ivBgCover);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            ivBgCover.setOnClickListener(this);
            albumCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(mContext, getAdapterPosition() + "", Toast.LENGTH_SHORT).show();
            AlbumSongsFm fragment = AlbumSongsFm.newInstance(arrAlbums.get(getAdapterPosition()));
            ((AppCompatActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }


    public AlbumAdapter(Context mContext, ArrayList<AlbumItem> arrAlbums) {
        this.mContext = mContext;
        this.arrAlbums = arrAlbums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AlbumItem albumItem = arrAlbums.get(position);

        holder.tvTitle.setText(albumItem.getName());
        ArrayList<PersonItem> singers =  albumItem.getSingers();
        String singerName = "";
        for (int singerIndex = 0; singerIndex < singers.size(); singerIndex++) {
            if (singerIndex == singers.size() - 1) {
                singerName += singers.get(singerIndex).getName();
            } else {
                singerName += singers.get(singerIndex).getName() + ", ";
            }
        }
        holder.tvSingers.setText(singerName);

        holder.tvViews.setText(albumItem.getViews()+"");

        // loading album cover using Glide library
        Glide.with(mContext).load(albumItem.getLinkImg())
                .centerCrop()
                .placeholder(R.drawable.item_down)
                .error(R.drawable.item_up)
                .into(holder.ivBgCover);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_play_all:
                    Toast.makeText(mContext, "Play all", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_detail:
                    Toast.makeText(mContext, "Detail", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return arrAlbums.size();
    }


    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = mContext.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}