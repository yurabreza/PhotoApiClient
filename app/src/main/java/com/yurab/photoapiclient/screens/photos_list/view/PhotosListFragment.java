package com.yurab.photoapiclient.screens.photos_list.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yurab.photoapiclient.R;
import com.yurab.photoapiclient.model.Response.Photo;
import com.yurab.photoapiclient.screens.photos_list.adapter.PhotosAdapter;
import com.yurab.photoapiclient.screens.photos_list.presenter.PhotosListPresenter;
import com.yurab.photoapiclient.screens.photos_list.presenter.PhotosListPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PhotosListFragment extends Fragment implements PhotosListView {

    public static final int PHOTOS_PER_PAGE = 10;

    public static final String ORDER_PHOTOS_BY_LATEST = "latest";
    public static final String ORDER_PHOTOS_BY_OLDEST = "oldest";
    public static final String ORDER_PHOTOS_BY_POPULAR = "popular";

    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private PhotosListPresenter mPresenter = new PhotosListPresenterImpl();
    private Unbinder mUnbinder;
    private PhotosAdapter mAdapter;
    private ArrayList<Photo> mPhotos = new ArrayList<>();
    private int mPage = 1;
    private String mOrderBy = ORDER_PHOTOS_BY_LATEST;

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                int visibleItemCount = mLinearLayoutManager.getChildCount();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                int pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                if (!mIsLoading/* && mPages != 0 && mPage != mPages*/) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        mIsLoading = true;
                        showProgress(true);
                        mPresenter.getPhotos(mPage, PHOTOS_PER_PAGE, mOrderBy);
                    }
                }
            }
        }
    };

    private LinearLayoutManager mLinearLayoutManager;
    private boolean mIsLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.onAttach(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new PhotosAdapter(getActivity(), mPhotos);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        showProgress(true);
        mIsLoading = true;
        mPresenter.getPhotos(mPage, PHOTOS_PER_PAGE, mOrderBy);
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    //region PhotosListView
    @Override
    public void onError(String s) {
        mIsLoading = false;
        showProgress(false);
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotosLoadSuccess(List<Photo> photos) {
        mIsLoading = false;
        ++mPage;
        mPhotos.addAll(photos);
        mAdapter.notifyDataSetChanged();
        showProgress(false);
    }
    //endregion

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        mPresenter.onDetach();
        super.onDestroyView();
    }
}
