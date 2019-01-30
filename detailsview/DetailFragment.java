package com.example.unamed.mvc.detailsview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.unamed.mvc.R;
import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.database.AppDatabase;
import com.example.unamed.mvc.database.InsertSerieAsync;
import com.example.unamed.mvc.error.NullChecker;
import com.example.unamed.mvc.searchactivity.SearchSeriesActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link SearchSeriesActivity}
 * in two-pane mode (on tablets) or a {@link SearchSeriesActivity}
 * on handsets.
 */
public class DetailFragment extends Fragment {

    private InsertSerieAsync mInsAync;
    private RequestOptions options = null;
    private boolean oneInstance = false;
    private Item.ItemBuilder mBuilder;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The content this fragment is presenting.
     */
    private Item mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the content specified by the fragment arguments.
            boolean temp = getArguments().getBoolean("temp");
            boolean db = getArguments().getBoolean("db");
          //  if (db) {
          //      mItem = ItemList.getFromDatabase(getArguments().getInt(ARG_ITEM_ID));
          //  } else {
                if (temp) {
                    mItem = ItemList.getFromFiltered(getArguments().getInt(ARG_ITEM_ID));
                } else {
                    mItem = ItemList.get(getArguments().getInt(ARG_ITEM_ID));
                }
           // }
        }

        if (mItem != null) {
            Activity activity = this.getActivity();
            if (activity != null) {
                CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.getMOriginalName());
                    appBarLayout.setTitleEnabled(true);
                }
            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mBuilder = Item.ItemBuilder.getInstance();
        mInsAync = new InsertSerieAsync(getActivity());
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        TextView tv = rootView.findViewById(R.id.item_detail);
        ImageView iv = rootView.findViewById(R.id.bgheader);
        FloatingActionButton fab = rootView.findViewById(R.id.fab2);
        NullChecker.isNotNullE(tv, mItem, mItem.getMOverview());

        if (options == null) {
            options = new RequestOptions().error(R.drawable.backdrop_error);
        }

        if (iv != null) {
            Glide.with(this).load(mItem.getMBackdropPath()).apply(options).thumbnail(0.2f)
                    .transition(withCrossFade(1000)).into(iv);
        }

        tv.setText(mItem.getMOverview());

        if (fab != null) {
            fab.setImageResource(R.drawable.he);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!oneInstance) {
                        Item serie = mBuilder.setId(mItem.getMId()).setOriginalName(mItem
                                .getMOriginalName()).setFirstAirDate(mItem.getMFirstAirDate())
                                .setLanguage(mItem.getMLanguage()).setBackdropPath(mItem
                                        .getMBackdropPath(), false).setPosterPath(mItem
                                                .getMPosterPath()
                                        , false).setOverview(mItem.getMOverview()).setRating
                                        (mItem.getMRating()).setLike(mItem.getMLike()).setItemPos
                                        (AppDatabase.mDatabaseIndex.get()).setTemp(mItem
                                        .getMIsTemp()).build();
                        mInsAync.execute(serie);
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Adding "
                                + "to " + "Favorites...", Snackbar.LENGTH_LONG).show();

                        oneInstance = true;
                    }
                }
            });
        }

        return rootView;
    }
}
