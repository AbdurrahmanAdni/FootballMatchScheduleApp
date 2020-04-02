package com.example.bolasepak.database.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.Entity.Team;
import com.example.bolasepak.database.Repository.TeamRepository;

import java.util.List;

public class TeamViewModel extends AndroidViewModel {
    private TeamRepository mTeamRepository;
    private LiveData<List<Team>> mAllTeams;

    public TeamViewModel(Application application) {
        super(application);
        mTeamRepository = new TeamRepository(application);
        mAllTeams = mTeamRepository.getAllTeams();
    }

    public LiveData<List<Team>> getAllTeams() {return mAllTeams;};

    public void insert(Team subscription) {mTeamRepository.insert(subscription);}
}
