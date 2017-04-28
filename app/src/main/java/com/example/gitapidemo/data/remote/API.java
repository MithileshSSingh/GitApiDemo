package com.example.gitapidemo.data.remote;

/**
 * Created by mithilesh on 3/30/17.
 */
public class API {

    public static class ServiceType{
        public static final String BASE_URL = "https://api.github.com";

        public static final String REPO_COMMITS = "/repos/rails/rails/commits";

    }

    public static class HttpErrorCode {
        public static final int NO_CODE = 0;
        public static final int UN_AUTH = 401;
    }
}
