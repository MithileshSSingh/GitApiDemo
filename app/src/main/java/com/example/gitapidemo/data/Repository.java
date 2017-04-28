package com.example.gitapidemo.data;

import android.content.Context;

import com.example.gitapidemo.mvp.beans.CommitDetail;

import java.util.ArrayList;


/**
 * Created by mithilesh on 8/23/16.
 */
public class Repository implements DataSource {
    private static Repository INSTANCE = null;

    private DataSource mLocalDataSource = null;
    private DataSource mRemoteDataSource = null;
    private DataSource mFakeDataSource = null;

    private Context mContext;

    private Repository() {

    }

    private Repository(Context context, DataSource localDataSource, DataSource remoteDataSource, DataSource fakeDataSource) {
        mContext = context;
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
        mFakeDataSource = fakeDataSource;

    }

    public static Repository getInstance(Context context, DataSource localDataSource, DataSource remoteDataSource, DataSource fakeDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new Repository(context, localDataSource, remoteDataSource, fakeDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void loadData(
            int pageNo,
            final LoadDataCallback callback) {

        mRemoteDataSource.loadData(
                pageNo,
                new LoadDataCallback() {
                    @Override
                    public void onSuccess(ArrayList<CommitDetail> data) {
                        callback.onSuccess(data);
                    }

                    @Override
                    public void failed(int errorCode, String msgError) {
                        callback.failed(errorCode, msgError);
                    }
                }
        );

    }
}
