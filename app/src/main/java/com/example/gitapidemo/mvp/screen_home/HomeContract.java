package com.example.gitapidemo.mvp.screen_home;

import com.example.gitapidemo.mvp.BasePresenter;
import com.example.gitapidemo.mvp.BaseView;
import com.example.gitapidemo.mvp.beans.CommitDetail;

import java.util.ArrayList;

/**
 * Created by mithilesh on 9/11/16.
 */
public interface HomeContract {
    interface OnItemSelectedListener {
        void onItemSelected(int position);

    }

    interface View extends BaseView<Presenter> {
        interface LoadDataCallback {
            void onSuccess(ArrayList<CommitDetail> data);

            void failed(int errorCode, String msgError);
        }
    }

    interface Presenter extends BasePresenter {
        void loadData(
                int size, View.LoadDataCallback callback
        );
    }
}
