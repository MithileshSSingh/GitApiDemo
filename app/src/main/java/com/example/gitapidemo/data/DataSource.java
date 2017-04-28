package com.example.gitapidemo.data;

import com.example.gitapidemo.mvp.beans.CommitDetail;

import java.util.ArrayList;

/**
 * Created by mithilesh on 8/23/16.
 */
public interface DataSource {
    interface LoadDataCallback {
        void onSuccess(ArrayList<CommitDetail> data);

        void failed(int errorCode, String msgError);
    }

    void loadData(
            int pageNo,
            LoadDataCallback callback
    );
}
