package com.example.gitapidemo.data.remote;


import com.example.gitapidemo.data.beans.RepoCommit;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mithilesh on 8/22/16.
 */
public interface ApiClient {

    @GET(API.ServiceType.REPO_COMMITS)
    Call<ArrayList<RepoCommit>> getCommits(
            @Query("page") Integer pageNo
    );

}
