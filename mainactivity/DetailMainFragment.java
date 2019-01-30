package com.example.unamed.mvc.mainactivity;

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
public class DetailMainFragment extends Fragment {

    private RequestOptions options = null;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id_new";

    /**
     * The content this fragment is presenting.
     */
    private Item mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailMainFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
                    mItem = ItemList.getFromDatabase(getArguments().getInt(ARG_ITEM_ID));
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
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        TextView tv = rootView.findViewById(R.id.item_detail);
        ImageView iv = rootView.findViewById(R.id.bgheader);
        NullChecker.isNotNullE(tv, mItem, mItem.getMOverview());
        FloatingActionButton fab = rootView.findViewById(R.id.fab2);
        if (fab != null)
            fab.setVisibility(View.GONE);

        if (options == null) {
            options = new RequestOptions().error(R.drawable.backdrop_error);
        }

        if (iv != null) {
            Glide.with(this).load(mItem.getMBackdropPath()).apply(options).thumbnail(0.2f)
                    .transition(withCrossFade(1000)).into(iv);
        }

        tv.setText(mItem.getMOverview());
        return rootView;
    }
}
