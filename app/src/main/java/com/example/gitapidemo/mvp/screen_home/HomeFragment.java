package com.example.gitapidemo.mvp.screen_home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gitapidemo.R;
import com.example.gitapidemo.di.RepositoryInjector;
import com.example.gitapidemo.mvp.BaseFragment;
import com.example.gitapidemo.mvp.beans.CommitDetail;
import com.example.gitapidemo.mvp.customviews.CustomProgressBar;

import java.util.ArrayList;

/**
 * Created by mithilesh on 9/11/16.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View, HomeContract.OnItemSelectedListener, View.OnClickListener {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private HomeContract.Presenter mPresenter;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new HomePresenter(
                RepositoryInjector.provideRepository(mActivity.getApplicationContext()),
                this
        );
        init();
    }

    @Override
    protected void init() {

        initView();
        initMembers();
        initListeners();
        initData();

    }

    private RecyclerView rvCommitDetail;
    private LinearLayoutManager mLayoutManagerRV;
    private CommitDetailAdapter mAdapterCommitDetail;
    private ArrayList<CommitDetail> mListData = new ArrayList<>();

    private boolean mIsLoadingData;

    private int mPageNo = 1;

    @Override
    protected void initView() {
        rvCommitDetail = (RecyclerView) mView.findViewById(R.id.rvCommitDetail);
    }

    @Override
    protected void initMembers() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapterCommitDetail = new CommitDetailAdapter(mActivity, mListData, this);
        mLayoutManagerRV = new LinearLayoutManager(
                mActivity.getApplicationContext()
        );
        RecyclerView.ItemAnimator itemAnimatorVertical = new DefaultItemAnimator();

        rvCommitDetail.setHasFixedSize(true);
        rvCommitDetail.setLayoutManager(mLayoutManagerRV);
        rvCommitDetail.setItemAnimator(itemAnimatorVertical);
        rvCommitDetail.setAdapter(mAdapterCommitDetail);

        mAdapterCommitDetail.notifyDataSetChanged();

    }

    @Override
    protected void initListeners() {
        rvCommitDetail.addOnScrollListener(mHorizontalScrollRecyclerView);
    }

    @Override
    protected void initData() {
        mPageNo = 1;
        mListData.clear();
        loadData(mPageNo++);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(int position) {

    }

    private void loadData(final int page) {

        mIsLoadingData = true;

        CustomProgressBar.getInstance(mActivity).show("Loading Commits...");

        mPresenter.loadData(page, new LoadDataCallback() {
            @Override
            public void onSuccess(ArrayList<CommitDetail> data) {

                CustomProgressBar.getInstance(mActivity).hide();

                if (page == 1) {
                    mListData.clear();
                }

                mListData.addAll(data);
                mAdapterCommitDetail.setListData(mListData);
                mAdapterCommitDetail.notifyDataSetChanged();

                mIsLoadingData = false;
            }

            @Override
            public void failed(int errorCode, String msgError) {

                mIsLoadingData = false;
                CustomProgressBar.getInstance(mActivity).hide();

                Toast.makeText(mActivity, msgError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    RecyclerView.OnScrollListener mHorizontalScrollRecyclerView = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mIsLoadingData)
                return;

            int visibleItemCount = mLayoutManagerRV.findLastCompletelyVisibleItemPosition();
            int listSize = mListData.size();

            if (visibleItemCount == listSize - 1) {
                loadData(mPageNo++);
            }
        }
    };
}
