package com.example.bolasepak.database.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bolasepak.database.DAO.SubscriptionDAO;
import com.example.bolasepak.database.Entity.Subscription;
import com.example.bolasepak.database.RoomDatabase.AppRoomDatabase;

import java.util.List;

public class SubscriptionRepository {
    private SubscriptionDAO mSubscriptionDAO;
    private LiveData<List<Subscription>> mAllSubscription;

    public SubscriptionRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mSubscriptionDAO = db.subscriptionDAO();
        mAllSubscription = mSubscriptionDAO.getAllTeams();
    }

    public LiveData<List<Subscription>> getAllSubscription() {
        return mAllSubscription;
    }

    public void insert(Subscription subscription) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSubscriptionDAO.insertSubscription(subscription);
        });
    }

    public Subscription findSubscriptionByID(Integer id_team) {
            return mSubscriptionDAO.findSubscriptionByID(id_team);
    }

    public void deleteSubs(Subscription... subscriptions) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSubscriptionDAO.deleteSubscription(subscriptions);
        });
    }
}
