package com.example.bolasepak.database.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.Entity.Matchpast;
import com.example.bolasepak.database.Repository.MatchpastRepository;

import java.util.List;

public class MatchpastViewModel extends AndroidViewModel {

    private MatchpastRepository mMatchRepository;
    private LiveData<List<Matchpast>> mAllMatches;

    public MatchpastViewModel(Application application) {
        super(application);
        mMatchRepository = new MatchpastRepository(application);
        mAllMatches = mMatchRepository.getAllMatches();
    }

    public LiveData<List<Matchpast>> getAllMatches() {return mAllMatches;};

    public void insert(Matchpast matchpast) {mMatchRepository.insert(matchpast);}

    public LiveData<List<Matchpast>> findMatchByID(Integer id_team) {return mMatchRepository.findMatchByID(id_team);}
}
