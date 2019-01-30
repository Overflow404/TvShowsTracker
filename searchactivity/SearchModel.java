package com.example.unamed.mvc.searchactivity;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.observer.Publisher;
import com.example.unamed.mvc.observer.Subscriber;
import com.example.unamed.mvc.webservices.FirstApiItemListWrapper;
import com.example.unamed.mvc.webservices.FirstApiItemWrapper;
import com.example.unamed.mvc.webservices.RestInterface;
import com.example.unamed.mvc.webservices.FirstApiSearchNameWrapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchModel implements Publisher {

    private Subscriber mSubscriber;
    private Vector<Item> mItem;
    private int mCurrentPage, mItemPos;
    private int mFilteredPos;
    private Item.ItemBuilder mBuilder;
    private RestInterface restInterface;

    //Eager initialization of singleton sModel.
    private static volatile SearchModel sModel = new SearchModel();

    private SearchModel() {
        mSubscriber = null;
        mItem = new Vector<>();
        mCurrentPage = 0;
        mItemPos = 0;
        mFilteredPos = 0;
        mBuilder = Item.ItemBuilder.getInstance();
        setupRestInterface(setupRetrofit());
    }

    private Retrofit setupRetrofit() {
        OkHttpClient client = createCachedClient();
        return new Retrofit.Builder().baseUrl("https://api.themoviedb.org/").client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void setupRestInterface(Retrofit retrofit) {
        restInterface = retrofit.create(RestInterface.class);
    }

    private OkHttpClient createCachedClient() {
        File httpCacheDirectory = new File(SearchSeriesActivity.getCache(), "cache_file");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String cacheHeaderValue = SearchSeriesActivity.isNetworkAvailable() ? "public, "
                        + "max-age=2419200" : "public, " + "only-if-cached, max-stale=2419200";
                Request request = originalRequest.newBuilder().build();
                Response response = chain.proceed(request);
                return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", cacheHeaderValue).build();
            }
        };

        Interceptor networkInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String cacheHeaderValue = SearchSeriesActivity.isNetworkAvailable() ? "public, "
                        + "max-age=2419200" : "public, " + "only-if-cached, max-stale=2419200";
                Request request = originalRequest.newBuilder().build();
                Response response = chain.proceed(request);
                return response.newBuilder().removeHeader("Pragma").removeHeader("Cache-Control")
                        .header("Cache-Control", cacheHeaderValue).build();
            }
        };

        return new OkHttpClient.Builder().cache(cache).addInterceptor(interceptor)
                .addNetworkInterceptor(networkInterceptor).build();
    }


    /**
     * Called when the {@link SearchModel} have to produce new items.
     */
    public void newBlock() {
        mItem.clear();
        mCurrentPage++;
        retrofitRequest(mCurrentPage);
    }


    public SparseArray<Item> newBlock(String query) {
        mFilteredPos = 0;
        return retrofitNamedRequest(query);
    }

    private SparseArray<Item> retrofitNamedRequest(String name) {
        final SparseArray<Item> newDataset = new SparseArray<>();
        Call<FirstApiSearchNameWrapper> call = restInterface.searchSerie
                ("780cf2ae1246709e314f1acc76af5433", name);
        try {
            FirstApiSearchNameWrapper gWrapper = call.execute().body();
            int count = 0;
            if (gWrapper != null) {
                List<FirstApiItemWrapper> sWrapper = gWrapper.getResults();
                if (sWrapper != null) {
                    for (FirstApiItemWrapper iw : sWrapper) {
                        if (iw != null) {
                            //Null check on item fields are inside addItem() method.
                            newDataset.put(count++, buildItem(iw, false));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newDataset;
    }

    private void retrofitRequest(Integer currentPage) {
        Call<FirstApiItemListWrapper> call = restInterface.discoverTv("780cf2ae1246709e314f1acc76af5433",
                currentPage);
        call.enqueue(new Callback<FirstApiItemListWrapper>() {
            @Override
            public void onResponse(@NonNull Call<FirstApiItemListWrapper> call, @NonNull
                    retrofit2.Response<FirstApiItemListWrapper> response) {
                FirstApiItemListWrapper gWrapper = response.body();
                if (gWrapper != null) {
                    List<FirstApiItemWrapper> sWrapper = gWrapper.getResults();
                    if (sWrapper != null) {
                        for (FirstApiItemWrapper iw : sWrapper) {
                            if (iw != null) {
                                mItem.add(buildItem(iw, true));
                            }
                        }
                        notifySubscriber();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FirstApiItemListWrapper> call, @NonNull Throwable t) {
                Log.d("[SearchModel]", "Retrofit request failed.");
            }
        });
    }

    private Item buildItem(FirstApiItemWrapper iw, boolean incr) {
        if (incr)
            return mBuilder.setId(iw.getId().toString()).setOriginalName(iw.getOriginalName())
                    .setFirstAirDate(iw.getFirstAirDate()).setLanguage(iw.getOriginalLanguage())
                    .setBackdropPath(iw.getBackdropPath(), true).setPosterPath(iw.getPosterPath(),
                            true)
                    .setOverview(iw.getOverview()).setRating(iw.getVoteAverage()).setLike(iw
                            .getVoteCount()).setItemPos(mItemPos++).setTemp(false).build();
        return mBuilder.setId(iw.getId().toString()).setOriginalName(iw.getOriginalName())
                .setFirstAirDate(iw.getFirstAirDate()).setLanguage(iw.getOriginalLanguage())
                .setBackdropPath(iw.getBackdropPath(), true).setPosterPath(iw.getPosterPath(),
                        true)
                .setOverview(iw.getOverview()).setRating(iw.getVoteAverage()).setLike(iw
                        .getVoteCount()).setItemPos(mFilteredPos++).setTemp(true)
                .build();
    }

    @Override
    public void subscribe(Subscriber sub) {
        mSubscriber = sub;
    }

    @Override
    public void notifySubscriber() {
        if (mSubscriber != null && mItem != null) {
            mSubscriber.update(mItem);
        }
    }

    //Return the singleton instance.
    public static SearchModel getInstance() {
        return sModel;
    }

}