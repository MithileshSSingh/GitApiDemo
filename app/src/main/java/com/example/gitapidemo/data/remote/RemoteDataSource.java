package com.example.gitapidemo.data.remote;

import android.content.Context;
import android.text.TextUtils;

import com.example.gitapidemo.R;
import com.example.gitapidemo.data.DataSource;
import com.example.gitapidemo.data.beans.RepoCommit;
import com.example.gitapidemo.mvp.beans.CommitDetail;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class RemoteDataSource implements DataSource {


    private static RemoteDataSource INSTANCE = null;

    private Context mContext;

    private RemoteDataSource() {

    }

    private RemoteDataSource(Context context) {
        mContext = context;
    }

    public static RemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(context);
        }

        return INSTANCE;
    }

    private static Retrofit retrofit;
    private static ApiClient apiClient;

    private class ApiError {
        public int errorCode;
        public String msgError;
    }

    public synchronized ApiClient getApiClient() {
        if (apiClient == null) {
            apiClient = getRetrofitInstance().create(ApiClient.class);
        }

        return apiClient;
    }

    private synchronized Retrofit getRetrofitInstance() {
        //XXX
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original;
                    String token = "";
                    if (!TextUtils.isEmpty(token)) {
                        request = original.newBuilder()
                                .header("Authorization", token)
                                .method(original.method(), original.body())
                                .build();
                    }
                    return chain.proceed(request);
                }
            });
            client.setReadTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setConnectTimeout(10, TimeUnit.SECONDS);

            /**
             * For debugging network call via Stetho
             */

            retrofit = new Retrofit.Builder()
                    .baseUrl(API.ServiceType.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(
                            new ErrorHandlingExecutorCallAdapterFactory(
                                    new ErrorHandlingExecutorCallAdapterFactory.MainThreadExecutor()))
                    .build();
        }

        return retrofit;
    }

    private ApiError getApiError(Throwable t) {
        RetrofitException exception = (RetrofitException) t;
        ApiError apiError = new ApiError();

        if (exception.getErrorResponse() != null) {
            ApiErrorResponse apiErrorResponse = exception.getErrorResponse();
            if (apiErrorResponse != null) {

                apiError.errorCode = apiErrorResponse.getStatusCode();
                apiError.msgError = apiErrorResponse.getMessage();

            }
        } else {

            apiError.errorCode = API.HttpErrorCode.NO_CODE;
            apiError.msgError = "Error";

        }

        return apiError;
    }

    @Override
    public void loadData(int pageNo, final LoadDataCallback callback) {

        if (!NetworkUtils.isNetworkAvailable(mContext)) {

            callback.failed(
                    API.HttpErrorCode.NO_CODE,
                    mContext.getResources().getString(R.string.text_err_no_network)
            );
            return;
        }

        Call<ArrayList<RepoCommit>> response = getApiClient().getCommits(pageNo);

        response.enqueue(new Callback<ArrayList<RepoCommit>>() {
            @Override
            public void onResponse(retrofit.Response<ArrayList<RepoCommit>> response, Retrofit retrofit) {

                ArrayList<RepoCommit> dataList = response.body();
                ArrayList<CommitDetail> listToReturn = new ArrayList<>();

                if (dataList != null) {
                    for (RepoCommit repoCommit : dataList) {

                        CommitDetail commitDetail = new CommitDetail();

                        if (repoCommit != null && repoCommit.getCommit() != null)
                            commitDetail.setCommitMessage(repoCommit.getCommit().getMessage());
                        if (repoCommit != null && repoCommit.getAuthor() != null)
                            commitDetail.setCommiterAvatar(repoCommit.getAuthor().getAvatarUrl());
                        if (repoCommit != null && repoCommit.getCommit() != null && repoCommit.getCommit().getCommitter() != null)
                            commitDetail.setCommiterName(repoCommit.getCommit().getCommitter().getName());


                        listToReturn.add(commitDetail);
                    }
                }

                callback.onSuccess(listToReturn);
            }

            @Override
            public void onFailure(Throwable t) {
                ApiError apiError = getApiError(t);
                callback.failed(apiError.errorCode, apiError.msgError);
            }
        });
    }
}
