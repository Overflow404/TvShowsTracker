package com.example.unamed.mvc.mainactivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;

import com.example.unamed.mvc.R;
import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.database.AppDatabase;
import com.example.unamed.mvc.database.DateQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AsyncNotify extends AsyncTask<Context, Void, Void> {
    private static SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private BroadcastReceiver.PendingResult mPendingResult;

    AsyncNotify(BroadcastReceiver.PendingResult pendingResult) {
        this.mPendingResult = pendingResult;
    }


    @Override
    protected Void doInBackground(Context... contexts) {
        if (contexts.length > 1) {
            return null;
        }
        Context mContext = contexts[0];
        //Config notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext,
                "notify_001");
        int index = 0;
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setPriority(Notification.PRIORITY_MAX);


        NotificationManager mNotificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001", "myNotification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        DateQuery.getInstance();

        Date current = new Date();
        String toDisplay;
        SparseArray<Item> favouriteSeries = new SparseArray<>();

        List<Item> tmp = AppDatabase.getInstance(mContext).userDao().getAll();

        for (Item se : tmp) {
            favouriteSeries.put(index++, se);
        }

        for (int i = 0; i < favouriteSeries.size(); i++) {
            String nuova = DateQuery.retrofitNamedRequest(favouriteSeries.get(i).getMOriginalName
                    ());
            if (nuova != null && !nuova.equals("null")) {
                try {
                    Date a = pattern.parse(nuova);
                    Item toUpdate = favouriteSeries.get(i);
                    toUpdate.setMNextDate(nuova);
                    AppDatabase.getInstance(mContext).userDao().update(toUpdate);

                    long diff = a.getTime() - current.getTime();
                    Log.d("NOTIFICO", TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+"");
                    if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) <= 1 /* 1 day */) {
                        toDisplay = (toUpdate.getMOriginalName() + " will be available tomorrow!");
                        bigText.bigText(toDisplay);
                        bigText.setBigContentTitle("Next episodes tomorrow!");
                        mBuilder.setStyle(bigText);
                        mBuilder.setContentTitle("Next episodes tomorrow!").setContentText
                                (toDisplay);
                        if (mNotificationManager != null) {
                            mNotificationManager.notify(i, mBuilder.build());
                        }
                    }

                } catch (ParseException e) {
                    Log.d("PARSING", "Error with json parsing.");
                }
            }

        }
        mPendingResult.finish();
        return null;
    }
}
