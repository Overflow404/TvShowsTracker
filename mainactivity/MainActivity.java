package com.example.unamed.mvc.mainactivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.unamed.mvc.R;
import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.database.LoadSeriesAsync;
import com.example.unamed.mvc.database.RemoveSerieAsync;
import com.example.unamed.mvc.detailsview.DetailActivity;
import com.example.unamed.mvc.detailsview.DetailFragment;
import com.example.unamed.mvc.error.NullChecker;
import com.example.unamed.mvc.searchactivity.MyRecyclerView;
import com.example.unamed.mvc.searchactivity.SearchSeriesActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity {

    public Context mContext;
    public static SimpleItemRecyclerViewAdapter adapter;
    private AlertDialog alertDialog;
    private ScaleGestureDetector mScaleGestureDetector;
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    public static RecyclerView.LayoutManager mCurrentLayoutManager;
    public static int itemPosition;
    private boolean mTwoPane;
    private MyRecyclerView mRecyclerView;
    private static int layoutStatus = 0;
    private boolean orientation;

    @Override
    protected void onPause() {
        super.onPause();
        alertDialog.dismiss();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        mContext = this;
        orientation = mContext.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE;

        initializeUI(savedInstanceState);
        initializeBackgroundTask();
        swipeCallback();
    }

    private void swipeCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int toRemove = viewHolder.getAdapterPosition();
                (new RemoveSerieAsync(mContext, toRemove)).execute(ItemList.getFromDatabase
                        (toRemove));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<MainActivity
            .SimpleItemRecyclerViewAdapter.ViewHolder> {

        @NonNull
        @Override
        public MainActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainActivity.SimpleItemRecyclerViewAdapter.ViewHolder(LayoutInflater.from
                    (parent.getContext()).inflate(R.layout.item_list_content, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final MainActivity.SimpleItemRecyclerViewAdapter
                .ViewHolder holder, int position) {
            final Item serie = ItemList.getFromDatabase(holder.getAdapterPosition());
            itemPosition = holder.getAdapterPosition();
            if (serie != null) {
                holder.itemView.setTag(serie);
                Glide.with(holder.itemView).load(serie.getMPosterPath()).thumbnail(0.2f)
                        .transition(withCrossFade(1000)).into(holder.posterPath);
                if (layoutStatus == 0) {
                    holder.originalName.setText(serie.getMOriginalName());
                    holder.originalName.setVisibility(View.VISIBLE);

                    holder.date.setText(mContext.getString(R.string.next_air_date, serie
                            .getMNextDate()));
                    holder.date.setVisibility(View.VISIBLE);

                    holder.language.setText(mContext.getString(R.string.original_language, serie
                            .getMLanguage()));
                    holder.language.setVisibility(View.VISIBLE);


                    holder.like.setText(mContext.getString(R.string.like_count, serie.getMLike()
                            .toString()));
                    holder.like.setVisibility(View.VISIBLE);

                    holder.rating.setRating(serie.getMRating().floatValue());
                    holder.rating.setVisibility(View.VISIBLE);
                } else if (layoutStatus == 1) {
                    holder.originalName.setVisibility(View.GONE);
                    holder.date.setVisibility(View.GONE);
                    holder.language.setVisibility(View.GONE);
                    holder.like.setVisibility(View.GONE);
                    holder.rating.setVisibility(View.GONE);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putInt(DetailMainFragment.ARG_ITEM_ID, holder
                                    .getAdapterPosition());
                            DetailMainFragment fragment = new DetailMainFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction().replace(R.id
                                    .item_detail_container, fragment).commit();
                        } else {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, DetailMainActivity.class);
                            intent.putExtra(DetailMainFragment.ARG_ITEM_ID, holder
                                    .getAdapterPosition
                                    ());
                            startActivity(intent);
                        }
                    }
                });
/*                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        alertDialog.setTitle("Next date");

                        alertDialog.setMessage(serie.getMOriginalName() + " will be available " +
                                "on:" +
                                serie.getMNextDate());
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new
                                DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        alertDialog.show();
                        return false;
                    }

                });*/
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView posterPath;
            TextView originalName, date, language, like;
            RatingBar rating;

            ViewHolder(View view) {
                super(view);
                posterPath = itemView.findViewById(R.id.poster);
                originalName = itemView.findViewById(R.id.name);
                originalName.setSelected(true);
                date = itemView.findViewById(R.id.date);
                rating = itemView.findViewById(R.id.pop);
                language = itemView.findViewById(R.id.genre);
                like = itemView.findViewById(R.id.like);
                NullChecker.isNotNullE(posterPath, originalName, date, rating, language, like);
            }


        }

        @Override
        public int getItemCount() {
            return ItemList.getDatabaseSize();
        }

    }

    private void initializeUI(Bundle savedInstanceState) {
        alertDialog = new AlertDialog.Builder(mContext).create(); //Read
        FloatingActionButton mFab = findViewById(R.id.fab);

        View extendedView = findViewById(R.id.item_detail_container);
        if (extendedView != null) {
            mTwoPane = true;
        }
        NullChecker.isNotNullE(mFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SearchSeriesActivity.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        NullChecker.isNotNullE(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mRecyclerView = findViewById(R.id.item_list);
        NullChecker.isNotNullE(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        adapter = new SimpleItemRecyclerViewAdapter();
        mRecyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            (new LoadSeriesAsync(this)).execute();
        }
        if ((orientation && !isTablet(this)) || (isTablet(this) && !orientation)) {
            mGridLayoutManager = new GridLayoutManager(mContext, 6);
        } else mGridLayoutManager = new GridLayoutManager(mContext, 3);
        if (layoutStatus == 0) {
            mCurrentLayoutManager = mLayoutManager;
        } else if (layoutStatus == 1) {
            mCurrentLayoutManager = mGridLayoutManager;
        }
        mRecyclerView.setLayoutManager(mCurrentLayoutManager);
        mRecyclerView.smoothScrollToPosition(itemPosition);
        setRecyclerViewOnTouchListener();
    }

    private void initializeBackgroundTask() {
        Intent startIntent = new Intent(getBaseContext(), BootReceiver.class);
        PendingIntent startPIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
                startIntent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarm != null) {
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, 60000
                    * 60 * 24 /* 1 day */, startPIntent);

        }
    }

    private void setRecyclerViewOnTouchListener() {
        mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleGestureDetector
                .SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                if (detector.getCurrentSpan() > 200 && detector.getTimeDelta() > 200) {
                    if (detector.getCurrentSpan() - detector.getPreviousSpan() < -0.95) {
                        Log.d("PINCH", "Sono un pincino");
                        if (mCurrentLayoutManager == mLayoutManager) {
                            mCurrentLayoutManager = mGridLayoutManager;
                            mRecyclerView.setLayoutManager(mCurrentLayoutManager);
                            layoutStatus = 1;
                            mCurrentLayoutManager.scrollToPosition(itemPosition);
                            mRecyclerView.smoothScrollToPosition(itemPosition);
                            return true;

                        }

                    } else if (detector.getCurrentSpan() - detector.getPreviousSpan() > 1.5) {
                        if (mCurrentLayoutManager == mGridLayoutManager) {
                            mCurrentLayoutManager = mLayoutManager;
                            mRecyclerView.setLayoutManager(mCurrentLayoutManager);
                            layoutStatus = 0;
                            mRecyclerView.smoothScrollToPosition(itemPosition);
                            mCurrentLayoutManager.scrollToPosition(itemPosition);
                            return true;
                        }
                    }
                }

                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                super.onScaleEnd(detector);
            }
        });

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleGestureDetector.onTouchEvent(event);
                v.performClick();
                return false;
            }
        });

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration
                .SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}





