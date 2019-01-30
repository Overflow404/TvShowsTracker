package com.example.unamed.mvc.searchactivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.example.unamed.mvc.R;
import com.example.unamed.mvc.detailsview.DetailActivity;

import java.io.File;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SearchSeriesActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private static ConnectivityManager sConnectivityManager = null;
    private SearchView view;
    private static File sCacheDir = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup main UI.
        setContentView(R.layout.activity_item_list_search);

        //Singleton initializations.
        if (sConnectivityManager == null) {
            sConnectivityManager = (ConnectivityManager) getSystemService(Context
                    .CONNECTIVITY_SERVICE);
        }
        if (sCacheDir == null) {
            sCacheDir = getCacheDir();
        }

        //MVC
        SearchModel model = SearchModel.getInstance();
        view = new SearchView(this);
        if (savedInstanceState != null) {
            new SearchController(view, model, true);
        } else {
            new SearchController(view, model, true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("Query da salvare", view.getSearchedQuery().toString());
        savedInstanceState.putCharSequence("searchQuery", view.getSearchedQuery());
        //savedInstanceState.putInt("itemPosition", SearchView.getLayoutStatus());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CharSequence myString = savedInstanceState.getCharSequence("searchQuery");
        view.setSearchQuery(myString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        view.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static boolean isNetworkAvailable() {
        if (sConnectivityManager != null) {
            NetworkInfo activeNetworkInfo = sConnectivityManager.getActiveNetworkInfo();
            return (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());
        }
        return false;
    }

    public static File getCache() {
        return sCacheDir;
    }

}
