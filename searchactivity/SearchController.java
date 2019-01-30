package com.example.unamed.mvc.searchactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.unamed.mvc.R;
import com.example.unamed.mvc.content.Item;
import com.example.unamed.mvc.content.ItemList;
import com.example.unamed.mvc.detailsview.DetailActivity;
import com.example.unamed.mvc.detailsview.DetailFragment;
import com.example.unamed.mvc.error.NullChecker;
import com.example.unamed.mvc.observer.Subscriber;

import java.util.Vector;

/**
 * Makes {@link SearchModel} and {@link SearchView} communicate.
 */
public class SearchController implements Subscriber {

    private SearchView mView;
    private static SearchModel mModel;
    private SimpleItemRecyclerViewAdapter mAdapter;
    static EndlessRecyclerViewScrollListener mScroll;

    SearchController(SearchView view, SearchModel model, boolean loadFirstBlock) {
        NullChecker.isNotNullE(view, model);
        mView = view;
        mModel = model;
        initializeController();
        if (loadFirstBlock) model.newBlock();
    }


    /**
     * Create {@link SearchView} listeners and adapters and subscribe to {@link SearchModel}.
     */
    private void initializeController() {
        mModel.subscribe(this);
        mScroll = new EndlessRecyclerViewScrollListener(mView.getLayoutManager());
        mView.setRecyclerViewScrollListener(mScroll);
        mAdapter = new SimpleItemRecyclerViewAdapter();
        mView.setRecyclerViewAdapter(mAdapter);
        mView.setAdapter(mAdapter);
        resetSeries();
    }

    /**
     * Called from {@link SearchModel} when data is ready, update the {@link SearchView}.
     */
    @Override
    public void update(Vector<Item> toNotify) {
        if (toNotify != null) {
            for (Item item : toNotify) {
                if (item != null) {
                    //Null check on item fields are inside addItem() method.
                    ItemList.addItem(item);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * Listeners and adapters class's.
     */
    class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        private boolean mLoading = true;
        private int mPreviousTotal = 0, mVisibleThreshold = 18;
        private int mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;
        private RecyclerView.LayoutManager mCurrentLayoutManager;
        private boolean mIsSearchedResults = false;

        EndlessRecyclerViewScrollListener(RecyclerView.LayoutManager lm) {
            NullChecker.isNotNullE(lm);
            mCurrentLayoutManager = SearchView.mCurrentLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mCurrentLayoutManager = SearchView.mCurrentLayoutManager;
            if (SearchView.getLayoutStatus() == 0) {
                LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager)
                        mCurrentLayoutManager;

                mVisibleItemCount = recyclerView.getChildCount();
                mTotalItemCount = mLinearLayoutManager.getItemCount();
                mFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                SearchView.itemPosition = mFirstVisibleItem;
                if (!mIsSearchedResults && mLoading && (mTotalItemCount >= mPreviousTotal)) {
                    mLoading = false;
                    mPreviousTotal = mTotalItemCount;
                }

                if (!mIsSearchedResults && !mLoading && (mFirstVisibleItem + mVisibleItemCount +
                        mVisibleThreshold >= mTotalItemCount)) {
                    mModel.newBlock();
                    mLoading = true;
                }
            }
            if (SearchView.getLayoutStatus() == 1) {
                GridLayoutManager mGridLayoutManager = (GridLayoutManager) mCurrentLayoutManager;
                int lastItem = mGridLayoutManager.findLastCompletelyVisibleItemPosition();
                //After narrow/landscape lastItem could be negative :(
                if (lastItem == -1) lastItem = SearchView.itemPosition;
                int currentTotalCount = mGridLayoutManager.getItemCount();
                SearchView.itemPosition = lastItem;
                if (!mIsSearchedResults && currentTotalCount <= lastItem + mVisibleThreshold) {
                    mModel.newBlock();
                }
            }
        }

        public void setSearchedResults(boolean flag) {
            mIsSearchedResults = flag;
        }
    }

    class SimpleItemRecyclerViewAdapter extends RecyclerView
            .Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {

        private boolean mTwoPane;
        private AppCompatActivity mContext;

        SimpleItemRecyclerViewAdapter() {
            mTwoPane = mView.getTwoPane();
            mContext = mView.getContext();
            setHasStableIds(true);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                    .item_list_content, parent, false));

        }

        //Free resources, GC can collect.
        @Override
        public void onViewRecycled(@NonNull final ViewHolder holder) {
            super.onViewRecycled(holder);
            mView.clearViewHolderImage(holder);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

            final Item item = ItemList.getFromFiltered(holder.getAdapterPosition());

            if (item != null) {
                holder.itemView.setTag(item);
                //Download on another thread.
                mView.loadViewHolderImage(holder, item);
                if (SearchView.getLayoutStatus() == 0) {
                    holder.originalName.setText(item.getMOriginalName());
                    holder.originalName.setVisibility(View.VISIBLE);

                    holder.date.setText(mContext.getString(R.string.first_air_date, item
                            .getMFirstAirDate()));
                    holder.date.setVisibility(View.VISIBLE);

                    holder.language.setText(mContext.getString(R.string.original_language, item
                            .getMLanguage()));
                    holder.language.setVisibility(View.VISIBLE);

                    holder.like.setText(mContext.getString(R.string.like_count, item.getMLike()
                            .toString()));
                    holder.like.setVisibility(View.VISIBLE);

                    holder.rating.setRating(item.getMRating().floatValue());
                    holder.rating.setVisibility(View.VISIBLE);
                } else if (SearchView.getLayoutStatus() == 1) {
                    holder.originalName.setVisibility(View.GONE);
                    holder.date.setVisibility(View.GONE);
                    holder.language.setVisibility(View.GONE);
                    holder.like.setVisibility(View.GONE);
                    holder.rating.setVisibility(View.GONE);
                }



                  /*Listener linked to an item of the RecyclerView.
                  If handset a {@link DetailActivity} is started.
                  If tablet-size devices a {@link DetailFragment} is started.*/

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            if (item.getMIsTemp()) {
                                arguments.putBoolean("temp", true);
                            } else {
                                arguments.putBoolean("temp", false);
                            }
                            arguments.putInt(DetailFragment.ARG_ITEM_ID, holder
                                    .getAdapterPosition());
                            DetailFragment fragment = new DetailFragment();
                            fragment.setArguments(arguments);
                            mContext.getSupportFragmentManager().beginTransaction().replace(R.id
                                    .item_detail_container, fragment).commit();
                        } else {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, DetailActivity.class);
                            //is temp da rinominare in ifSearched
                            if (item.getMIsTemp()) {
                                intent.putExtra("temp", true);
                            } else {
                                intent.putExtra("temp", false);
                            }
                            intent.putExtra(DetailFragment.ARG_ITEM_ID, holder.getAdapterPosition
                                    ());
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults = new FilterResults();
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mScroll.setSearchedResults(false);
                        filterResults.values = ItemList.mItemMap;
                        filterResults.count = ItemList.mItemMap.size();
                        return filterResults;

                    } else {
                        filterResults.values = mModel.newBlock(charString);
                        mScroll.setSearchedResults(true);
                        filterResults.count = ItemList.mFilteredMap.size();
                        return filterResults;

                    }

                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults
                        filterResults) {
                    ItemList.mFilteredMap = (SparseArray<Item>) filterResults.values;

                    mView.scrollRecyclerToStart();
                    notifyDataSetChanged();
                }
            };
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
            return ItemList.filteredSize();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

    }

    private void resetSeries() {
        mScroll.setSearchedResults(false);
        ItemList.mFilteredMap = ItemList.mItemMap;
        mAdapter.notifyDataSetChanged();

    }

}
