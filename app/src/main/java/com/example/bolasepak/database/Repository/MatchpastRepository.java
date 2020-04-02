package com.example.bolasepak.database.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.DAO.MatchpastDAO;
import com.example.bolasepak.database.Entity.Matchpast;
import com.example.bolasepak.database.RoomDatabase.AppRoomDatabase;

import java.util.List;

public class MatchpastRepository {
    private MatchpastDAO mMatchpastDao;
    private LiveData<List<Matchpast>> mAllMatches;

    public MatchpastRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mMatchpastDao = db.matchpastDAO();
        mAllMatches = mMatchpastDao.getAllMatches();
    }

    public LiveData<List<Matchpast>> getAllMatches() {
        return mAllMatches;
    }

    public void insert(Matchpast matchpast) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMatchpastDao.insertMatch(matchpast);
        });
    }

    public LiveData<List<Matchpast>> findMatchByID(Integer id_team) {
        return mMatchpastDao.findMatchByID(id_team);
    }
}
