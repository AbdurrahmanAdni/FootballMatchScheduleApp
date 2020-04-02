package com.example.bolasepak.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bolasepak.database.Entity.Team;

import java.util.List;

@Dao
public interface TeamDAO {

    @Query("SELECT * FROM `team`")
    LiveData<List<Team>> getAllTeams();

    @Query("SELECT * FROM `team` WHERE id_team= :id_team")
    LiveData<List<Team>> findMatchByID(Integer id_team);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTeam(Team... teams);

    @Delete
    public void deleteTeam(Team... teams);

    @Query("DELETE FROM `team`")
    void deleteAllTeams();
}
