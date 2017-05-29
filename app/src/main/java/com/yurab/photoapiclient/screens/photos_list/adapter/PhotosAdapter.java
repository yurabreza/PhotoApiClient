package com.yurab.photoapiclient.screens.photos_list.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.model.response.Photo;
import com.yurab.photoapiclient.tools.Utils;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    public static final String TAG = PhotosAdapter.class.getSimpleName();
    private final ArrayList<Photo> mItems;
    private final Context mContext;
    private PhotosAdapterInteractionListener mListener;

    @BindInt(R.integer.image_corner_radius) int mCornerRadius;

    public PhotosAdapter(Context context, PhotosAdapterInteractionListener listener, ArrayList<Photo> items) {
        mContext = context;
        mListener = listener;
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
            Log.d(TAG, "bind: " + photo.toString());
            Glide.with(mContext).load(photo.getUrls().getRegular())
                    .placeholder(R.drawable.ic_placeholder)
                    .bitmapTransform(new CenterCrop(mContext),
                            new RoundedCornersTransformation(mContext, mCornerRadius, 0))
                    .dontAnimate()
                    .into(mMainImageView);
            mLikeCheckBox.setChecked(photo.getLikedByUser());
            mAmountOfLikesTextView.setText(String.valueOf(photo.getLikes()));
            mAuthorNameTextView.setText(photo.getUser().getName());
        }

        @OnClick(R.id.iv_image)
        void onImageClick(View v) {
            Utils.disableViewAfterClick(v);
            mListener.selectPhoto(mItems.get(getAdapterPosition()).getId());
        }

        @OnCheckedChanged(R.id.cb_like_view)
        void onLikeClick(boolean checked) {
            Utils.disableViewAfterClick(mLikeCheckBox);
            int likes = 0;
            if (!TextUtils.isEmpty(mAmountOfLikesTextView.getText().toString())){
                likes = Integer.valueOf(mAmountOfLikesTextView.getText().toString());
            }
            if (checked) {
                mListener.likePhoto(mItems.get(getAdapterPosition()).getId());
                ++likes;
            } else {
                mListener.unlikePhoto(mItems.get(getAdapterPosition()).getId());
                --likes;
            }
            mAmountOfLikesTextView.setText(String.valueOf(likes));
        }
    }

    public interface PhotosAdapterInteractionListener {
        void likePhoto(String id);

        void unlikePhoto(String id);

        void selectPhoto(String id);

    }
}
