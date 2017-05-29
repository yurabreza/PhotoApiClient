package com.yurab.photoapiclient.screens.random_photo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.model.response.random_photo.RandomPhotoResponse;
import com.yurab.photoapiclient.screens.random_photo.presenter.RandomPhotoPresenter;
import com.yurab.photoapiclient.screens.random_photo.presenter.RandomPhotoPresenterImpl;
import com.yurab.photoapiclient.tools.Utils;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class RandomPhotoFragment extends Fragment implements RandomPhotoView {

    @BindView(R.id.iv_image) ImageView mMainImageView;
    @BindView(R.id.iv_user_pic) ImageView mUserPicImageView;
    @BindView(R.id.tv_author_name) TextView mUserNameTextView;
    @BindView(R.id.tv_created_at) TextView mCreatedDateTextView;
    @BindView(R.id.tv_portfolio_url) TextView mPortfolioUrlTextView;
    @BindView(R.id.tv_author_bio) TextView mAuthorBioTextView;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    @BindInt(R.integer.image_corner_radius) int mCornerRadius;

    private Unbinder unbinder;
    private RandomPhotoPresenter mPresenter = new RandomPhotoPresenterImpl();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_photo, container, false);
        unbinder = ButterKnife.bind(this, view);
        mPresenter.onAttach(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgress(true);
        mPresenter.getRandomPhoto();
    }

    @OnClick(R.id.iv_image)
    void loadNext(View v) {
        Utils.disableViewAfterClick(v);
        showProgress(true);
        mPresenter.getRandomPhoto();
    }

    void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    //region RandomPhotoView
    @Override
    public void onError(String s) {
        showProgress(false);
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoLoadSuccess(RandomPhotoResponse photo) {
        showProgress(false);
        Glide.with(getActivity()).
                load(photo.getUrls().getRegular())
                .bitmapTransform(new CenterCrop(getActivity()), new RoundedCornersTransformation(getActivity(), mCornerRadius, 0))
                .into(mMainImageView);
        Glide.with(getActivity()).
                load(photo.getUser().getProfileImage().getSmall())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(mUserPicImageView);
        mUserNameTextView.setText(photo.getUser().getName());
        mCreatedDateTextView.setText(photo.getCreatedAt());
        mPortfolioUrlTextView.setText(photo.getUser().getLinks().getPortfolio());
        mAuthorBioTextView.setText(photo.getUser().getBio());
    }
    //endregion


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
        unbinder.unbind();
    }
}
