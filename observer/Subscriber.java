package com.example.unamed.mvc.observer;

import android.util.SparseArray;

import com.example.unamed.mvc.content.Item;
import java.util.Vector;

/**
 * An interface representing who wants to be notified from publisher.
 */
public interface Subscriber {
    void update(Vector<Item> toNotify);
   /* void newDataset(SparseArray<Item> dataset);*/
}
