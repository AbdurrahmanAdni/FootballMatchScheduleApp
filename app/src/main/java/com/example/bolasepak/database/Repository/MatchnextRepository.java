package com.example.bolasepak.database.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.DAO.MatchnextDAO;
import com.example.bolasepak.database.Entity.Matchnext;
import com.example.bolasepak.database.RoomDatabase.AppRoomDatabase;

import java.util.List;

public class MatchnextRepository {
    private MatchnextDAO mMatchnextDao;
    private LiveData<List<Matchnext>> mAllMatches;

    public MatchnextRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mMatchnextDao = db.matchnextDAO();
        mAllMatches = mMatchnextDao.getAllMatches();
    }

    public LiveData<List<Matchnext>> getAllMatches() {
        return mAllMatches;
    }

    public void insert(Matchnext matchnext) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMatchnextDao.insertMatch(matchnext);
        });
    }
}
