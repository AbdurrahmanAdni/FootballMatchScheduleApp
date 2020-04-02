package com.example.bolasepak;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.bolasepak.database.Entity.Subscription;
import com.example.bolasepak.database.ViewModel.SubscriptionViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bolasepak.ui.main.SectionsPagerAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ObjectStreamException;
import java.util.List;

public class TeamDetail extends AppCompatActivity {

    private SubscriptionViewModel svw;
    LiveData<List<Subscription>> ls;

    public static String getTeamId;
    public static String getTeamName;
    public static String getTeamLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Intent intent = getIntent();

        getTeamId = intent.getStringExtra("teamId");
        getTeamName = intent.getStringExtra("teamName");
        getTeamLogo = intent.getStringExtra("teamLogo");

        Log.i("TeamUrl", getTeamLogo);

        ImageView teamLogo = findViewById(R.id.team_logo);
        TextView teamName = findViewById(R.id.team_name);

        Picasso.get().load(getTeamLogo).fit().centerInside().into(teamLogo);
        teamName.setText(getTeamName);

        svw = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        svw.getAllSubscription().observe(this, new Observer<List<Subscription>>() {
            @Override
            public void onChanged(List<Subscription> subscriptions) {
                // TODO : set adapters
            }
        });

        fetchDataFromDB fdb = new fetchDataFromDB();
        fdb.execute(Integer.parseInt(getTeamId));
    }

    public class fetchDataFromDB extends AsyncTask<Integer, Void, Subscription> {
        int flag = 0;

        @Override
        protected Subscription doInBackground(Integer... integers) {
            Subscription s_result;
            s_result = svw.findSubscriptionByID(integers[0]);

            if (s_result == null) {
                // belum subscribe
                s_result = new Subscription(integers[0]);
            } else {
                // sudah subscribe
                flag = 1;
            }

            return s_result;
        }

        @Override
        protected void onPostExecute(Subscription subscription) {
            super.onPostExecute(subscription);
            Button subscribeButton = findViewById(R.id.button2);
            if (flag == 1) {
                subscribeButton.setText("Unsubscribe");
            } else {
                subscribeButton.setText("Subscribe");
            }

            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag == 0) {
                        svw.insert(subscription);
                        subscribeButton.setText("Unsubscribe");
                        flag = 1;
                    } else {
                        svw.delete(subscription);
                        subscribeButton.setText("Subscribe");
                        flag = 0;
                    }
                }
            });
        }
    }
}