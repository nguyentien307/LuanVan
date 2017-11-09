package com.example.tiennguyen.thesis.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tiennguyen.thesis.R;
import com.example.tiennguyen.thesis.fragment.AlbumFm;
import com.example.tiennguyen.thesis.fragment.CategoryFm;
import com.example.tiennguyen.thesis.fragment.HomeFm;
import com.example.tiennguyen.thesis.fragment.PlaylistFm;
import com.example.tiennguyen.thesis.fragment.StyleFm;
import com.example.tiennguyen.thesis.fragment.UserFm;
import com.example.tiennguyen.thesis.interfaces.ItemClickListener;
import com.example.tiennguyen.thesis.model.TopicItem;
import com.example.tiennguyen.thesis.util.Constants;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;

/**
 * Created by TIENNGUYEN on 11/6/2017.
 */

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<TopicItem> arrTopicItems;
    Context context;
    SparseBooleanArray expandState = new SparseBooleanArray();

    public TopicAdapter(ArrayList<TopicItem> arrTopicItems){
        this.arrTopicItems = arrTopicItems;
        for(int i = 0; i < arrTopicItems.size(); i++){
            expandState.append(i, false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.information_content_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder)holder;
        TopicItem item = arrTopicItems.get(position);
        viewHolder.setIsRecyclable(false);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/isadora.ttf");
        viewHolder.tvTopicTitle.setTypeface(typeface);
        viewHolder.tvTopicTitle.setText(item.getTitle());

        viewHolder.ivTopic.setImageResource(item.getImage());
        viewHolder.expandableLayout.setInRecyclerView(true);
        viewHolder.expandableLayout.setExpanded(expandState.get(position));
        viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

            @Override
            public void onPreOpen() {
                viewHolder.llSpace.setVisibility(View.VISIBLE);
                changeRotate(viewHolder.button,0f,180f).start();
                expandState.put(position,true);
            }

            @Override
            public void onPreClose() {
                changeRotate(viewHolder.button,180f,0f).start();
                expandState.put(position,false);
            }

            @Override
            public void onClosed() {
                super.onClosed();
                viewHolder.llSpace.setVisibility(View.GONE);
            }
        });
        viewHolder.button.setRotation(expandState.get(position)?180f:0f);
        viewHolder.rlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.expandableLayout.toggle();
            }
        });

        viewHolder.expandableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.expandableLayout.toggle();
            }
        });

        viewHolder.tvTopicDetail.setText(item.getDetail());


        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //viewHolder.expandableLayout.toggle();
                String topicName =  arrTopicItems.get(position).getTitle();
                Fragment fragment = new Fragment();
                switch(topicName){
                    case Constants.USER_TITLE : fragment = new UserFm(); break;
                    case Constants.HOME_TITLE : fragment = new HomeFm(); break;
                    case Constants.ALBUM_TITLE :fragment = new AlbumFm(); break;
                    case Constants.CATEGORY_TITLE :fragment = new CategoryFm(); break;
                    case Constants.STYLE_TITLE :fragment = new StyleFm(); break;
                    case Constants.PLAYLIST_TITLE :fragment = new PlaylistFm(); break;
                }
                ((AppCompatActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(topicName)
                        .replace(R.id.content_frame, fragment, topicName)
                        .commit();
            }
        });
    }



    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", from, to );
        animator.setDuration(400);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return arrTopicItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvTopicTitle, tvTopicDetail;
        public RelativeLayout button;
        public RelativeLayout rlButton; //button up down
        public ImageView ivTopic;
        public LinearLayout llImangeButton;
        public ExpandableLinearLayout expandableLayout;
        public LinearLayout llSpace;
        ItemClickListener itemClickListener;


        public ViewHolder(View itemView) {
            super(itemView);

            tvTopicTitle = (TextView) itemView.findViewById(R.id.tvTopicTitle);
            tvTopicDetail = (TextView) itemView.findViewById(R.id.tvTopicDetail);
            button = (RelativeLayout) itemView.findViewById(R.id.button);
            rlButton = (RelativeLayout) itemView.findViewById(R.id.rlButton);
            ivTopic = (ImageView) itemView.findViewById(R.id.ivTopic);
            llImangeButton = (LinearLayout) itemView.findViewById(R.id.llImageButton);
            llSpace = (LinearLayout) itemView.findViewById(R.id.llSpace);
            expandableLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.expandableLayout);

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