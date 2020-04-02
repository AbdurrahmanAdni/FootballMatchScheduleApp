package com.example.bolasepak.database.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.DAO.TeamDAO;
import com.example.bolasepak.database.Entity.Team;
import com.example.bolasepak.database.RoomDatabase.AppRoomDatabase;

import java.util.List;

public class TeamRepository {
    private TeamDAO mTeamDao;
    private LiveData<List<Team>> mAllTeams;

    public TeamRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mTeamDao = db.teamDAO();
        mAllTeams = mTeamDao.getAllTeams();
    }

    public LiveData<List<Team>> getAllTeams() {
        return mAllTeams;
    }

    public void insert(Team team) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTeamDao.insertTeam(team);
        });
    }
}
