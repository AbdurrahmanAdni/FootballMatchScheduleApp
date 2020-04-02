package com.example.bolasepak.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bolasepak.database.Entity.Subscription;

import java.util.List;

@Dao
public interface SubscriptionDAO {

    @Query("SELECT * FROM `subscription`")
    LiveData<List<Subscription>> getAllTeams();

    @Query("SELECT * FROM `subscription` WHERE id_team= :id_team")
    Subscription findSubscriptionByID(Integer id_team);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSubscription(Subscription... teams);

    @Delete
    public void deleteSubscription(Subscription... teams);

    @Query("DELETE FROM `subscription`")
    void deleteAllSubscriptions();
}
