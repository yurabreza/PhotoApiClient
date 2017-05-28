package com.yurab.photoapiclient.screens.photos_list.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.model.Response.Photo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private final ArrayList<Photo> mItems;
    private final Context mContext;

    public PhotosAdapter(Context context, ArrayList<Photo> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image) ImageView mMainImageView;
        @BindView(R.id.cb_like_view) AppCompatCheckBox mLikeCheckBox;
        @BindView(R.id.tv_amount_of_likes) TextView mAmountOfLikesTextView;
        @BindView(R.id.tv_author_name) TextView mAuthorNameTextView;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Photo photo) {
            Glide.with(mContext).load(photo.getUrls().getRegular())
                    .bitmapTransform(new CenterCrop(mContext),
                            new RoundedCornersTransformation(mContext, 16, 0))
                    .into(mMainImageView);
            mLikeCheckBox.setChecked(photo.getLikedByUser());
            mAmountOfLikesTextView.setText(String.valueOf(photo.getLikes()));
            mAuthorNameTextView.setText(photo.getUser().getName());
        }
    }
}
