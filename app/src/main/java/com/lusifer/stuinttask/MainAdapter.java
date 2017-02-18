package com.lusifer.stuinttask;


import com.lusifer.stuinttask.model.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_VIDEO = 2;
    private final int VIEW_IMAGE = 1;
    private final int VIEW_TEXT = 0;
    private final Context context;
    private List<Data> data;

    public MainAdapter(Context context, List<Data> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_TEXT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_view, parent, false);
            vh = new ViewHolder(v);
            return vh;
        } else if (viewType == VIEW_IMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_image, parent, false);
            vh = new ImageViewHolder(v);
            return vh;
        } else if (viewType == VIEW_VIDEO) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_image, parent, false);
            vh = new VideoViewHolder(v);
            return vh;
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LinearLayout.LayoutParams paramview1, paramview2, paramview3;

        if (holder instanceof ViewHolder) {

            paramview1 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getYes());

            paramview2 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getNeutral());

            paramview3 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getNo());

            ((ViewHolder) holder).view1.setLayoutParams(paramview1);
            ((ViewHolder) holder).view2.setLayoutParams(paramview2);
            ((ViewHolder) holder).view3.setLayoutParams(paramview3);
            ((ViewHolder) holder).tvTitle.setText(data.get(position).getTitle());
        } else if (holder instanceof ImageViewHolder) {

            paramview1 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getYes());

            paramview2 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getNeutral());

            paramview3 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getNo());

            ((ImageViewHolder) holder).view1.setLayoutParams(paramview1);
            ((ImageViewHolder) holder).view2.setLayoutParams(paramview2);
            ((ImageViewHolder) holder).view3.setLayoutParams(paramview3);
            ((ImageViewHolder) holder).tvTitle.setText(data.get(position).getTitle());

            String picturePath = data.get(position).getData().getPath();

            ((ImageViewHolder) holder).image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (holder instanceof VideoViewHolder) {

            paramview1 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getYes());

            paramview2 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getNeutral());

            paramview3 = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT, data.get(position).getVoteData().getNo());

            ((VideoViewHolder) holder).view1.setLayoutParams(paramview1);
            ((VideoViewHolder) holder).view2.setLayoutParams(paramview2);
            ((VideoViewHolder) holder).view3.setLayoutParams(paramview3);

            ((VideoViewHolder) holder).imagePlay.setVisibility(View.VISIBLE);
            ((VideoViewHolder) holder).tvTitle.setText(data.get(position).getTitle());
            String picturePath = data.get(position).getData().getPath();
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MINI_KIND);
            ((VideoViewHolder) holder).image.setImageBitmap(bMap);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        switch (data.get(position).getType()) {
            case Text:
                return VIEW_TEXT;

            case Image:
                return VIEW_IMAGE;

            case Video:
                return VIEW_VIDEO;

        }

        return VIEW_TEXT;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv)
        TextView tvTitle;

        @BindView(R.id.card)
        CardView card;

        @BindView(R.id.ivImage)
        ImageView image;

        @BindView(R.id.ivPlay)
        ImageView imagePlay;

        @BindView(R.id.view1)
        View view1;

        @BindView(R.id.view2)
        View view2;

        @BindView(R.id.view3)
        View view3;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv)
        TextView tvTitle;

        @BindView(R.id.card)
        CardView card;

        @BindView(R.id.ivImage)
        ImageView image;

        @BindView(R.id.view1)
        View view1;

        @BindView(R.id.view2)
        View view2;

        @BindView(R.id.view3)
        View view3;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv)
        TextView tvTitle;

        @BindView(R.id.card)
        CardView card;

        @BindView(R.id.view1)
        View view1;

        @BindView(R.id.view2)
        View view2;

        @BindView(R.id.view3)
        View view3;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
