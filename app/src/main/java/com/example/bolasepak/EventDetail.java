package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class EventDetail extends AppCompatActivity {

    String getHomeId;
    String getHomeName;
    String getHomeScore;
    String getHomeUrl;
    String getHomeShot;
    String getHomeScorer;

    String date;

    String getAwayId;
    String getAwayName;
    String getAwayScore;
    String getAwayUrl;
    String getAwayShot;
    String getAwayScorer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();

        getHomeId = intent.getStringExtra("homeTeamId");
        getHomeName = intent.getStringExtra("homeTeamName");
        getHomeScore = intent.getStringExtra("homeTeamScore");
        getHomeUrl = intent.getStringExtra("homeTeamLogo");
        getHomeShot = intent.getStringExtra("homeTeamShot");
        getHomeScorer = intent.getStringExtra("homeTeamScorer");

        date = intent.getStringExtra("date");

        getAwayId = intent.getStringExtra("awayTeamId");
        getAwayName = intent.getStringExtra("awayTeamName");
        getAwayScore = intent.getStringExtra("awayTeamScore");
        getAwayUrl = intent.getStringExtra("awayTeamLogo");
        getAwayShot = intent.getStringExtra("awayTeamShot");
        getAwayScorer = intent.getStringExtra("awayTeamScorer");

        Log.i("HomeScore", getHomeScore);
        Log.i("AwayScore", getAwayScore);

        TextView homeName = (TextView) findViewById(R.id.homeTeamName);
        TextView awayName = (TextView) findViewById(R.id.awayTeamName);

        TextView homeScore = (TextView) findViewById(R.id.homeTeamScore);
        TextView awayScore = (TextView) findViewById(R.id.awayTeamScore);

        ImageView homeLogo = (ImageView) findViewById(R.id.homeTeamLogo);
        ImageView awayLogo = (ImageView) findViewById(R.id.awayTeamLogo);

        TextView homeShot = (TextView) findViewById(R.id.homeTeamShot);
        TextView awayShot = (TextView) findViewById(R.id.awayTeamShot);

        TextView dateText = (TextView) findViewById(R.id.date);

        TextView homeScorer = findViewById(R.id.home_scorer);
        TextView awayScorer = findViewById(R.id.away_scorer);

        TextView shots = findViewById(R.id.shots);
        TextView goals = findViewById(R.id.goals);

        homeName.setText(getHomeName);
        homeScore.setText(getHomeScore);
        Picasso.get().load(getHomeUrl).fit().centerInside().into(homeLogo);
        //homeShot.setText(getHomeShot);

        awayName.setText(getAwayName);
        awayScore.setText(getAwayScore);
        Picasso.get().load(getAwayUrl).fit().centerInside().into(awayLogo);
        //awayShot.setText(getAwayShot);

        dateText.setText(date);

        if (getHomeScore.charAt(0) == '-'){
            goals.setVisibility(View.INVISIBLE);

            homeShot.setVisibility(View.INVISIBLE);
            awayShot.setVisibility(View.INVISIBLE);
        } else {
            homeShot.setText(getHomeShot);
            awayShot.setText(getAwayShot);

            if (Integer.parseInt(getHomeScore) != 0){
                homeScorer.setText(getDaftarScorer(getHomeScorer));
            }

            if (Integer.parseInt(getAwayScore) != 0){
                awayScorer.setText(getDaftarScorer(getAwayScorer));
            }
        }
    }

    public String getDaftarScorer(String getScorer) {

        String stringOfScorer = "";

        String[] arrayOfStr = getScorer.split(";");
        String[] arrayOfScorer;

        int n = arrayOfStr.length;

        for (int i = 0; i < n; i++) {

            arrayOfScorer = arrayOfStr[i].split(":");

            stringOfScorer += (arrayOfScorer[1] + " " + arrayOfScorer[0] + "\n");
        }

        return stringOfScorer;

    }

    public void launchHomeTeamDetail(View view) {
        Intent intent = new Intent(this, TeamDetail.class);

        Log.i("TeamUrl", getHomeUrl);
        intent.putExtra("teamId", getHomeId);
        intent.putExtra("teamName", getHomeName);
        intent.putExtra("teamLogo", getHomeUrl);


        startActivity(intent);
    }

    public void launchAwayTeamDetail(View view) {
        Intent intent = new Intent(this, TeamDetail.class);

        Log.i("TeamUrl", getAwayUrl);
        intent.putExtra("teamId", getAwayId);
        intent.putExtra("teamName",getAwayName);
        intent.putExtra("teamLogo", getAwayUrl);

        startActivity(intent);
    }
}
