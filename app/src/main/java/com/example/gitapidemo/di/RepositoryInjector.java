package com.example.gitapidemo.di;

import android.content.Context;

import com.example.gitapidemo.data.Repository;
import com.example.gitapidemo.data.fake.FakeDataSource;
import com.example.gitapidemo.data.local.LocalDataSource;
import com.example.gitapidemo.data.remote.RemoteDataSource;


/**
 * Created by mithilesh on 9/4/16.
 */
public class RepositoryInjector {

    public static Repository provideRepository(Context context) {
        return Repository.getInstance(
                context,
                LocalDataSource.getInstance(context),
                RemoteDataSource.getInstance(context),
                FakeDataSource.getInstance(context)
        );
    }
}
