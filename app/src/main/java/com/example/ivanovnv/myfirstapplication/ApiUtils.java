package com.example.ivanovnv.myfirstapplication;

import android.support.annotation.Nullable;

import com.example.ivanovnv.myfirstapplication.model.RegistrationError;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static Gson gson;
    private static AcademyApi api;

    public static OkHttpClient getBasicAuthClient(final String email, final String password, boolean newInstance) {
        if (newInstance || okHttpClient == null) {

            okHttpClient = null;
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.authenticator(new Authenticator() {
                @Nullable
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credentials = Credentials.basic(email, password);
                    return response.request().newBuilder().addHeader("Authorization", credentials).build();
                }
            });

            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public static Retrofit getRetrofit(final String email, final String password, boolean newInstance){
        if (gson == null)  {
            gson = new Gson();
        }
        if (retrofit == null || newInstance) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL)
                    //need for interceptors
                    .client(getBasicAuthClient(email, password, newInstance))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static AcademyApi getApi() {
        if (api == null) {
            api = getRetrofit("", "", false).create(AcademyApi.class);
        }
        return api;
    }

    public static AcademyApi getApi(final String email, final String password) {
        api = getRetrofit(email, password, true).create(AcademyApi.class);
        return api;
    }

    public static RegistrationError parseRegistrationError (retrofit2.Response<Void> response) {
        Converter<ResponseBody, RegistrationError> converter =
                retrofit.responseBodyConverter(RegistrationError.class, new Annotation[0]);

        RegistrationError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new RegistrationError();
        }

        return error;
    }
}
