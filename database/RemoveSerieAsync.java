package com.example.unamed.mvc.database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.mainactivity.MainActivity;

public class RemoveSerieAsync extends AsyncTask<Item, Void, Void> {

    private AppDatabase adbm;
    private int toRemove;

    public RemoveSerieAsync(Context context, int index) {
        adbm = AppDatabase.getInstance(context);
        toRemove = index;
    }

    @Override
    public Void doInBackground(Item... series) {
        if (series.length > 1) {
            cancel(true);
            throw new IllegalStateException("RemoveSerieAsync: wrong input");
        }
        DateQuery.getInstance();

        for (Item s : series) {
            try {
                Item serie = s;
                adbm.userDao().delete(serie);
            } catch (SQLiteConstraintException e) {
                Log.d("DB", s.getMOriginalName() + " non e' gia' presente.");
                cancel(true);
            } catch (IllegalStateException e1) {
                Log.d("DB", s.getMOriginalName() + "json server error.");
                cancel(true);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ItemList.removeFromDatabase(toRemove);
        MainActivity.adapter.notifyItemRemoved(toRemove);
    }
}
