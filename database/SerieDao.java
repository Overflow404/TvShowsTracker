package com.example.unamed.mvc.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.unamed.mvc.content.Item;

import java.util.List;

@Dao
public interface SerieDao {

    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Query("DELETE FROM item")
    void  emptyAll();

    @Insert
    void insert(Item serie);

    @Delete
    void delete(Item serie);

   @Update(onConflict = OnConflictStrategy.REPLACE)
   void update(Item serie);
}



