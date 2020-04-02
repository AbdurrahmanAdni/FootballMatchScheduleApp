package com.example.bolasepak.database.RoomDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.bolasepak.database.DAO.MatchnextDAO;
import com.example.bolasepak.database.DAO.MatchpastDAO;
import com.example.bolasepak.database.DAO.SubscriptionDAO;
import com.example.bolasepak.database.DAO.TeamDAO;
import com.example.bolasepak.database.Entity.Matchnext;
import com.example.bolasepak.database.Entity.Matchpast;
import com.example.bolasepak.database.Entity.Subscription;
import com.example.bolasepak.database.Entity.Team;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Matchpast.class, Subscription.class, Team.class, Matchnext.class}, version = 5)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract MatchpastDAO matchpastDAO();
    public abstract SubscriptionDAO subscriptionDAO();
    public abstract TeamDAO teamDAO();
    public abstract MatchnextDAO matchnextDAO();

    private static volatile AppRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppRoomDatabase.class, "bolasepak_database")
                            .addCallback(sAppRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


private static RoomDatabase.Callback sAppRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            databaseWriteExecutor.execute(() -> {
                MatchpastDAO mpDao = INSTANCE.matchpastDAO();
                TeamDAO tDAO = INSTANCE.teamDAO();
                SubscriptionDAO sDAO = INSTANCE.subscriptionDAO();
                MatchnextDAO mnDAO = INSTANCE.matchnextDAO();

                mpDao.deleteAllMatches();
                mnDAO.deleteAllMatches();
                tDAO.deleteAllTeams();
                sDAO.deleteAllSubscriptions();

                Integer id_team = new Integer(133604);
                Subscription subscription = new Subscription(id_team);

                sDAO.insertSubscription(subscription);
            });
        }
    };
}
