package com.example.unamed.mvc.database;

import android.util.Log;

import com.example.unamed.mvc.searchactivity.SearchSeriesActivity;
import com.example.unamed.mvc.webservices.SecondApiDateWrapper;
import com.example.unamed.mvc.webservices.RestDateInterface;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DateQuery {
    private static final String QUERY = "https://www.episodate.com/";
    private static RestDateInterface restInterface = null;

    private static Retrofit setupRetrofit() {
        OkHttpClient client = createCachedClient();
        return new Retrofit.Builder().baseUrl(QUERY).client(client).addConverterFactory
                (GsonConverterFactory.create()).build();
    }

    private static void setupRestInterface(Retrofit retrofit) {
        restInterface = retrofit.create(RestDateInterface.class);
    }

    private static OkHttpClient createCachedClient() {
        File httpCacheDirectory = new File(SearchSeriesActivity.getCache(), "cache_file");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        return new OkHttpClient.Builder().cache(cache).build();
    }

    public static RestDateInterface getInstance() {
        if (restInterface == null) {
            setupRestInterface(setupRetrofit());
        }
        return restInterface;
    }

    public static String retrofitNamedRequest(String name) {
        Call<SecondApiDateWrapper> call = restInterface.dateSerie(name.replaceAll("\\s+","-"));
        String date = null;
        try {
            SecondApiDateWrapper fw = call.execute().body();

            if (fw != null) {
                if (fw.getTvShow() != null) {
                    if (fw.getTvShow().getCountdown() != null) {
                        if (fw.getTvShow().getCountdown().getAirDate() != null) {
                            date = fw.getTvShow().getCountdown().getAirDate();
                        }
                        else {
                            date = null;
                        }
                    }
                    else {
                        date = null;
                    }
                }
                else {
                    date = null;
                }
            }
            else {
                date = null;
            }
            Log.d("DATA", "La data e`: I" + date + "F");

        } catch (JsonSyntaxException e1) {
            Log.d("DATEQUERY","Invalid json from server");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (date == null) return "null";
        return date;
    }
}
