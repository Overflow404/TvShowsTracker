package com.example.unamed.mvc.detailsview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.unamed.mvc.R;
import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.database.AppDatabase;
import com.example.unamed.mvc.database.InsertSerieAsync;
import com.example.unamed.mvc.error.NullChecker;
import com.example.unamed.mvc.searchactivity.SearchSeriesActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SearchSeriesActivity}.
 */
public class DetailActivity extends AppCompatActivity {

    //Singleton listener with lazy initialization.
    private static RequestListener<Drawable> sListener = null;

    private RequestOptions options = null;
    private Context mContext = null;
    private InsertSerieAsync mInsAync;
    private boolean oneInstance = false;
    private Item.ItemBuilder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        mBuilder = Item.ItemBuilder.getInstance();
        if (mContext == null) {
            mContext = getApplicationContext();
        }

        mInsAync = new InsertSerieAsync(mContext);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        NullChecker.isNotNullE(toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        NullChecker.isNotNullE(fab);

        ImageView backdrop = findViewById(R.id.bgheader);
        final ProgressBar progressBar = findViewById(R.id.backdropProgress);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            throw new IllegalStateException("Bundle passed to DetailActivity is null.");
        }
        boolean temp = bundle.getBoolean("temp");
        boolean db = bundle.getBoolean("db");
        final Item item;
        //An item is temp if is the result of a search.
       // if (db) {
       //     item = ItemList.getFromDatabase(bundle.getInt(DetailFragment.ARG_ITEM_ID));
       // }
       // else {
            if (temp) {
                item = ItemList.getFromFiltered(bundle.getInt(DetailFragment.ARG_ITEM_ID));
            } else {
                item = ItemList.get(bundle.getInt(DetailFragment.ARG_ITEM_ID));
            }
       // }


        NullChecker.isNotNullE(item);
        NullChecker.isNotNullE(backdrop);
        NullChecker.isNotNullE(progressBar);
        if (sListener == null) {
            sListener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                        target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            };
        }

        if (options == null) {
            options = new RequestOptions().error(R.drawable.backdrop_error);
        }

        Glide.with(this).load(item.getMBackdropPath()).thumbnail(0.2f).transition(withCrossFade
                (1000)).listener(sListener).apply(options).into(backdrop);
        setTitle(item.getMOriginalName());

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(DetailFragment.ARG_ITEM_ID, getIntent().getIntExtra(DetailFragment
                    .ARG_ITEM_ID, -1));
            if (temp) {
                arguments.putBoolean("temp", true);
            } else {
                arguments.putBoolean("temp", false);
            }
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container,
                    fragment).commit();
        }
        fab.setImageResource(R.drawable.he);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Same activity double tap on add to favourites.
                if (!oneInstance) {
                    Item serie = mBuilder.setId(item.getMId()).setOriginalName(item
                            .getMOriginalName()).setFirstAirDate(item.getMFirstAirDate())
                            .setLanguage(item.getMLanguage()).setBackdropPath(item
                                    .getMBackdropPath(), false).setPosterPath(item.getMPosterPath(),
                                    false).setOverview(item.getMOverview()).setRating(item
                                    .getMRating()).setLike(item.getMLike()).setItemPos
                                    (AppDatabase.mDatabaseIndex.get()).setTemp(item.getMIsTemp())
                            .build();
                    Log.d("QUII", item.getMBackdropPath());
                    //Aggiungo al database
                    mInsAync.execute(serie);
                    Snackbar.make(findViewById(android.R.id.content), "Adding to " +
                            "Favorites...", Snackbar.LENGTH_LONG).show();
                    oneInstance = true;
                }
            }
        });
    }
}
