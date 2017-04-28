package com.example.gitapidemo.data.fake;

import android.content.Context;


import com.example.gitapidemo.data.DataSource;


public class FakeDataSource implements DataSource {
    private static FakeDataSource INSTANCE;

    private Context mContext;

    public FakeDataSource(Context context) {
        mContext = context;
    }

    public static FakeDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FakeDataSource(context);
        }
        return INSTANCE;
    }

    private static class Error {
        private static final int ERROR_UNKNOWN = 0;
    }

    @Override
    public void loadData(
            int from,
            LoadDataCallback callback) {
        
    }

}
