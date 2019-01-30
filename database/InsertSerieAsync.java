package com.example.unamed.mvc.database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.mainactivity.MainActivity;

public class InsertSerieAsync extends AsyncTask<Item, Void, Void> {

    private AppDatabase adbm;
    private Item serie;


    public InsertSerieAsync(Context context) {
        adbm = AppDatabase.getInstance(context);
    }

    @Override
    public Void doInBackground(Item... series) {
        if (series.length > 1) {
            cancel(true);
            throw new IllegalStateException("InsertSerieAsync: wrong input");
        }
        DateQuery.getInstance();

        for (Item s : series) {
            try {
                serie = s;
                serie.setMNextDate(DateQuery.retrofitNamedRequest(serie.getMOriginalName()));
                adbm.userDao().insert(serie);
            } catch (SQLiteConstraintException e) {
                Log.d("DB", s.getMOriginalName() + " e' gia' presente.");
                cancel(true);
            } catch (IllegalStateException e1) {
                Log.d("DB", s.getMOriginalName() + "json server error.");
                cancel(true);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //It's on UI thread, so I can access the adapter.
        if (!isCancelled()) {
            Log.d("ONBIND", serie.getMOriginalName() + " in posizione: " + AppDatabase
                    .mDatabaseIndex.get());
            //ItemList.putDatabaseMap(AppDatabase.mDatabaseIndex.get(), serie);
            /*NUOVA*/ItemList.putDatabaseMap( serie);
            if (MainActivity.adapter != null) {
                if (AppDatabase.mDatabaseIndex != null) {
                    MainActivity.adapter.notifyItemInserted(AppDatabase.mDatabaseIndex.get());
                    AppDatabase.mDatabaseIndex.addAndGet(1);
                }
            }
        }
    }

}
