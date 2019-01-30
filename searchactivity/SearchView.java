package com.example.unamed.mvc.searchactivity;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.unamed.mvc.R;
import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.error.NullChecker;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class SearchView {

    private ScaleGestureDetector mScaleGestureDetector;
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    public static RecyclerView.LayoutManager mCurrentLayoutManager;
    private MyRecyclerView mRecyclerView;
    private AppCompatActivity mContext;
    private android.widget.SearchView mSearchView;
    private boolean mTwoPane, restoredSession = false;
    private CharSequence toRestore;
    private static int layoutStatus = 0;
    public static int itemPosition;
    private SearchController.SimpleItemRecyclerViewAdapter mAdapter;
    private static RequestOptions mOptions = new RequestOptions().error(R.drawable.error)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

    public SearchView(AppCompatActivity context) {
        mContext = context;
        initializeGUI();
    }

    public void scrollRecyclerToStart() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * Look up and setup main gui component.
     */
    private void initializeGUI() {
        View extendedView = mContext.findViewById(R.id.item_detail_container);
        if (extendedView != null) {
            mTwoPane = true;
        }

        mRecyclerView = mContext.findViewById(R.id.item_list);
        NullChecker.isNotNullE(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mContext);


        if (mContext.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE && !mTwoPane)
            mGridLayoutManager = new GridLayoutManager(mContext, 6);
        else mGridLayoutManager = new GridLayoutManager(mContext, 3);
        if (layoutStatus == 1) {
            mCurrentLayoutManager = mGridLayoutManager;
        } else if (layoutStatus == 0) {
            mCurrentLayoutManager = mLayoutManager;
        }
        mRecyclerView.setLayoutManager(mCurrentLayoutManager);
        mCurrentLayoutManager.scrollToPosition(itemPosition);
        //mRecyclerView.smoothScrollToPosition(itemPosition);
        setRecyclerViewOnTouchListener();

    }

    private void setRecyclerViewOnTouchListener() {
        mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleGestureDetector
                .SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                //correct logic
                if (detector.getCurrentSpan() > 200 && detector.getTimeDelta() > 200) {
                    if (detector.getCurrentSpan() - detector.getPreviousSpan() < -0.95) {
                        if (mCurrentLayoutManager == mLayoutManager) {
                            mCurrentLayoutManager = mGridLayoutManager;
                            mRecyclerView.setLayoutManager(mCurrentLayoutManager);
                            layoutStatus = 1;
                            mCurrentLayoutManager.scrollToPosition(itemPosition);
                            return true;

                        }

                    } else if (detector.getCurrentSpan() - detector.getPreviousSpan() > 1.5) {
                        if (mCurrentLayoutManager == mGridLayoutManager) {
                            mCurrentLayoutManager = mLayoutManager;
                            mRecyclerView.setLayoutManager(mCurrentLayoutManager);
                            layoutStatus = 0;
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
                v.performClick();
                mScaleGestureDetector.onTouchEvent(event);
                return false;
            }
        });

    }

    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        NullChecker.isNotNullE(adapter);
        mRecyclerView.setAdapter(adapter);
    }

    public static int getLayoutStatus() {
        return layoutStatus;
    }

    public void setRecyclerViewScrollListener(SearchController.EndlessRecyclerViewScrollListener
                                                      listener) {
        NullChecker.isNotNullE(listener);
        mRecyclerView.addOnScrollListener(listener);


    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mCurrentLayoutManager;
    }

    public boolean getTwoPane() {
        return mTwoPane;
    }

    public AppCompatActivity getContext() {
        return mContext;
    }

    public void clearViewHolderImage(SearchController.SimpleItemRecyclerViewAdapter.ViewHolder
                                             holder) {
        if (NullChecker.isNotNullB(holder, holder.itemView, holder.posterPath)) {
            Glide.with(holder.itemView).clear(holder.posterPath);
        }
    }

    public void loadViewHolderImage(final SearchController.SimpleItemRecyclerViewAdapter
            .ViewHolder holder, Item item) {
        if (NullChecker.isNotNullB(holder, holder.itemView, holder.posterPath, item)) {
            Glide.with(holder.itemView).load(item.getMPosterPath()).thumbnail(0.2f).apply
                    (mOptions).transition(withCrossFade(1000)).into(holder.posterPath);

        }
    }

    public void onCreateOptionsMenu(Menu menu) {
        mContext.getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (android.widget.SearchView) searchMenuItem.getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        mSearchView.setOnQueryTextListener(new QueryListener());
        if (restoredSession) {
            mSearchView.setQuery(toRestore, true);
            restoredSession = false;
        }
        mSearchView.setIconifiedByDefault(false);
    }

    public CharSequence getSearchedQuery() {
        return mSearchView.getQuery();
    }

    public void setSearchQuery(CharSequence query) {
        toRestore = query;
        restoredSession = true;
    }

    public void setAdapter(SearchController.SimpleItemRecyclerViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }


    class QueryListener implements android.widget.SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            mAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (newText.isEmpty()) {
                ItemList.mFilteredMap = ItemList.mItemMap;
                mAdapter.notifyDataSetChanged();
                SearchController.mScroll.setSearchedResults(false);
            }
            return false;
        }
    }

}
