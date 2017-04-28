package com.example.gitapidemo.mvp.screen_home;

import com.example.gitapidemo.data.DataSource;
import com.example.gitapidemo.data.Repository;
import com.example.gitapidemo.mvp.beans.CommitDetail;

import java.util.ArrayList;

/**
 * Created by mithilesh on 9/11/16.
 */
public class HomePresenter implements HomeContract.Presenter {
    private Repository mRepository;
    private HomeContract.View mView;

    private ArrayList<String> listSubReddits = new ArrayList<>();

    public HomePresenter(Repository repository, HomeContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadData(int size, final HomeContract.View.LoadDataCallback callback) {
        mRepository.loadData(
                size,
                new DataSource.LoadDataCallback() {
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
