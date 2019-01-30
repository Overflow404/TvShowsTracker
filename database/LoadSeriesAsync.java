package com.example.unamed.mvc.database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.mainactivity.MainActivity;

import java.util.List;

import static com.example.unamed.mvc.database.AppDatabase.mDatabaseIndex;

public class LoadSeriesAsync extends AsyncTask<Item, Void, Void> {

    private AppDatabase adbm;
    private List<Item> tmp;

    public LoadSeriesAsync(Context context) {
        adbm = AppDatabase.getInstance(context);
    }

    @Override
    protected Void doInBackground(Item... series) {
        tmp = adbm.userDao().getAll();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        for (Item se : tmp) {
            se.setmItemPos(mDatabaseIndex.get());
           // ItemList.putDatabaseMap(mDatabaseIndex.getAndAdd(1), se);
            /*NUOVA*/ItemList.putDatabaseMap(se); mDatabaseIndex.getAndAdd(1);
        }
        //Better notify a range than notify all dataset.
        MainActivity.adapter.notifyItemRangeInserted(0, mDatabaseIndex.get());
    }
}
