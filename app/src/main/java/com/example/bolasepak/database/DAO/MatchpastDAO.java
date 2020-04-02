package com.example.bolasepak.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bolasepak.database.Entity.Matchpast;

import java.util.List;

@Dao
public interface MatchpastDAO {

    @Query("SELECT * FROM matchpast")
    LiveData<List<Matchpast>> getAllMatches();

    @Query("SELECT * FROM matchpast WHERE idHomeTeam= :id_team or idAwayTeam= :id_team")
    LiveData<List<Matchpast>> findMatchByID(Integer id_team);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMatch(Matchpast... matchpasts);

    @Delete
    public void deleteMatch(Matchpast... matchpasts);

    @Query("DELETE FROM matchpast")
    void deleteAllMatches();
}
