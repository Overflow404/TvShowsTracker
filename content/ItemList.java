package com.example.unamed.mvc.content;

import android.util.SparseArray;

import com.example.unamed.mvc.error.NullChecker;

import java.util.ArrayList;

/**
 * Provide content for user interfaces.
 */

public class ItemList {

    /**
     * A SparseArray of items, by position. And a filtered SparseArray of items, by position.
     **/
    public static SparseArray<Item> mItemMap = new SparseArray<>();
    public static SparseArray<Item> mFilteredMap = mItemMap;
    private static ArrayList<Item> mDatabaseMap = new ArrayList<>();
    private static int mDatabaseSize= 0;


    public static void addItem(Item item) {
        if (item != null) {
            boolean notNull = NullChecker.isNotNullB(item.getMFirstAirDate(), item.getMId(), item
                    .getMLanguage(), item.getMLike(), item.getMOriginalName(), item.getMOverview
                    (), item.getMPosterPath(), item.getMBackdropPath(), item.getMRating());
            if (item.getMItemPos() >= 0 && notNull) {
                mItemMap.put(item.getMItemPos(), item);
            }
        }
    }

    public static void putDatabaseMap(Item value) {
        if (value != null) {
            mDatabaseMap.add(value);
            mDatabaseSize++;
        }
    }

    public static int getDatabaseSize() {
        return mDatabaseSize;
    }


    public static Item get(Integer key) {
        if (key != null) {
            return mItemMap.get(key);
        }
        return null;
    }

    public static Item getFromFiltered(Integer key) {
        if (key != null) {
            return mFilteredMap.get(key);
        }
        return null;
    }


    public static int filteredSize() {
        return mFilteredMap.size();
    }

    public static Item getFromDatabase(Integer key) {
        if (key != null) {
            return mDatabaseMap.get(key);
        }
        return null;
    }


    public static void removeFromDatabase(int toRemove) {
        mDatabaseMap.remove(toRemove);
        decreaseDbSize();
    }

    private static void decreaseDbSize() {
        mDatabaseSize--;
    }

}

