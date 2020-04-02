package com.example.bolasepak.database.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.Entity.Matchnext;
import com.example.bolasepak.database.Repository.MatchnextRepository;

import java.util.List;

public class MatchnextViewModel extends AndroidViewModel {

    private MatchnextRepository mMatchnextRepository;
    private LiveData<List<Matchnext>> mAllMatches;

    public MatchnextViewModel(Application application) {
        super(application);
        mMatchnextRepository = new MatchnextRepository(application);
        mAllMatches = mMatchnextRepository.getAllMatches();
    }

    public LiveData<List<Matchnext>> getAllMatches() {return mAllMatches;};

    public void insert(Matchnext matchnext) {mMatchnextRepository.insert(matchnext);}
}
