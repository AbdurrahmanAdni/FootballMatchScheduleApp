package com.example.bolasepak.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bolasepak.database.Entity.Matchnext;

import java.util.List;

@Dao
public interface MatchnextDAO {

    @Query("SELECT * FROM matchnext")
    LiveData<List<Matchnext>> getAllMatches();

    @Query("SELECT * FROM matchnext WHERE id_match= :id_match")
    LiveData<List<Matchnext>> findMatchByID(Integer id_match);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMatch(Matchnext... matchnexts);

    @Delete
    public void deleteMatch(Matchnext... matchnexts);

    @Query("DELETE FROM matchnext")
    void deleteAllMatches();
}
