package com.example.tiennguyen.thesis.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.fragment.AlbumFm;
import com.example.tiennguyen.thesis.fragment.CategoryFm;
import com.example.tiennguyen.thesis.interfaces.ItemClickListener;
import com.example.tiennguyen.thesis.model.CategoryItem;
import com.example.tiennguyen.thesis.util.Constants;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 11/7/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<CategoryItem> arrCategory;
    Context ctx;
    MaterialSheetFab materialSheetFab;
    int topic;
    Constants c;

    public CategoryAdapter(ArrayList<CategoryItem> arrCategory, Context ctx, MaterialSheetFab materialSheetFab, int topic){
        this.arrCategory = arrCategory;
        this.ctx = ctx;
        this.materialSheetFab = materialSheetFab;
        this.topic = topic;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        itemView = inflater.inflate(R.layout.category_card, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/mystical.ttf");
        holder.tvTitle.setTypeface(typeface);
        holder.tvTitle.setText(arrCategory.get(position).getName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (materialSheetFab.isSheetVisible()) {
                    materialSheetFab.hideSheet();

                    Fragment fragment = new Fragment();

                    switch (topic){
                        case Constants.ALBUM_TYPE : fragment = AlbumFm.newInstance(arrCategory.get(position).getName());break;
                        case Constants.CATEGORY_TYPE: fragment = CategoryFm.newInstance(arrCategory.get(position).getName());break;
                    }


                    ((AppCompatActivity)ctx).getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.content_frame, fragment)
                            .commit();
                } else {
                    //super.onBackPressed();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrCategory.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cvCategory;
        TextView tvTitle;
        ItemClickListener itemClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            cvCategory = (CardView) itemView.findViewById(R.id.cvCategory);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
