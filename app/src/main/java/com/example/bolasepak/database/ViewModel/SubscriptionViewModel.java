package com.example.bolasepak.database.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.Entity.Subscription;
import com.example.bolasepak.database.Repository.SubscriptionRepository;

import java.util.List;

public class SubscriptionViewModel extends AndroidViewModel {
    private SubscriptionRepository mSubscriptionRepository;
    private LiveData<List<Subscription>> mAllSubscription;

    public SubscriptionViewModel(Application application) {
        super(application);
        mSubscriptionRepository = new SubscriptionRepository(application);
        mAllSubscription = mSubscriptionRepository.getAllSubscription();
    }

    public LiveData<List<Subscription>> getAllSubscription() {return mAllSubscription;};

    public void insert(Subscription subscription) {mSubscriptionRepository.insert(subscription);}

    public Subscription findSubscriptionByID(Integer id_team) {
        return mSubscriptionRepository.findSubscriptionByID(id_team);
    }

    public void delete(Subscription subscription) {mSubscriptionRepository.deleteSubs(subscription);}
}
