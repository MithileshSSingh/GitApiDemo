package com.example.gitapidemo.mvp.screen_home;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gitapidemo.R;
import com.example.gitapidemo.mvp.BaseViewHolder;
import com.example.gitapidemo.mvp.beans.CommitDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mithilesh on 9/4/16.
 */
public class CommitDetailAdapter extends RecyclerView.Adapter<CommitDetailViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CommitDetail> mListData = Collections.emptyList();

    private HomeContract.OnItemSelectedListener mListener;

    public CommitDetailAdapter(Context context,
                               ArrayList<CommitDetail> listPosts,
                               HomeContract.OnItemSelectedListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListData = listPosts;
        mListener = listener;

    }

    @Override
    public CommitDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.item_commit_detail, parent, false);
        return new CommitDetailViewHolder(mContext, convertView, mListener);
    }

    @Override
    public void onBindViewHolder(CommitDetailViewHolder holder, int position) {
        CommitDetail data = mListData.get(position);
        holder.apply(data, position);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void setListData(ArrayList<CommitDetail> data) {
        mListData = data;
    }
}


class CommitDetailViewHolder extends RecyclerView.ViewHolder implements BaseViewHolder<CommitDetail>, View.OnClickListener {

    private View mView;
    private int mPosition;
    private Context mContext;
    private CommitDetail mData;

    private HomeContract.OnItemSelectedListener mListener;

    public CommitDetailViewHolder(Context context, View itemView,
                                  HomeContract.OnItemSelectedListener listener) {
        super(itemView);
        mView = itemView;
        mContext = context;
        mListener = listener;

        init();
    }

    public CommitDetailViewHolder(View itemView) {
        super(itemView);
    }

    private void init() {
        initView();
        initListener();
    }

    private TextView tvName;
    private TextView tvMessage;

    private ImageView ivProfile;
    private ImageView ivError;
    private ProgressBar progress;

    private void initView() {
        tvName = (TextView) mView.findViewById(R.id.tvName);
        tvMessage = (TextView) mView.findViewById(R.id.tvMessage);

        ivError = (ImageView) mView.findViewById(R.id.ivError);
        progress = (ProgressBar) mView.findViewById(R.id.progress);
        ivProfile = (ImageView) mView.findViewById(R.id.ivProfile);

    }

    private void initListener() {
    }

    @Override
    public void apply(CommitDetail data, int position) {
        mData = data;
        mPosition = position;

        if (mData.getCommiterName() != null) {
            tvName.setText(String.valueOf(mData.getCommiterName()));
        }
        if (mData.getCommitMessage() != null) {
            tvMessage.setText(String.valueOf("Message : " + mData.getCommitMessage()));
        }
        if (mData.getCommiterAvatar() != null && !mData.getCommiterAvatar().trim().equals("")) {
            initProfileImage(mData.getCommiterAvatar());
        }


    }

    private void initProfileImage(String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .resetViewBeforeLoading(true)
                    .build();
        } else {
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .resetViewBeforeLoading(false)
                    .build();
        }

        imageLoader.displayImage(url, ivProfile, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progress.setVisibility(View.VISIBLE);
                ivProfile.setVisibility(View.INVISIBLE);
                ivError.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progress.setVisibility(View.GONE);
                ivProfile.setVisibility(View.GONE);
                ivError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progress.setVisibility(View.GONE);
                ivProfile.setVisibility(View.VISIBLE);
                ivError.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progress.setVisibility(View.GONE);
                ivProfile.setVisibility(View.GONE);
                ivError.setVisibility(View.VISIBLE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });

    }


    @Override
    public void onClick(View view) {
    }
}
