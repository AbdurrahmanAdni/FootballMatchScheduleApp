package com.example.bolasepak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.database.Entity.Matchnext;
import com.example.bolasepak.database.Entity.Matchpast;
import com.example.bolasepak.database.Entity.Subscription;
import com.example.bolasepak.database.Entity.Team;
import com.example.bolasepak.database.ViewModel.MatchnextViewModel;
import com.example.bolasepak.database.ViewModel.MatchpastViewModel;
import com.example.bolasepak.database.ViewModel.SubscriptionViewModel;
import com.example.bolasepak.database.ViewModel.TeamViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.example.bolasepak.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity implements MatchScheduleAdapter.OnItemClickListener {

    private NotificationManagerCompat notificationManager;
    TextView steps;

    boolean running = false;

    private RecyclerView mRecyclerView;
    private MatchScheduleAdapter mMatchScheduleAdapter;
    private ArrayList<MatchSchedule> mMatchScheduleList;
    private RequestQueue mRequestQueue;
    private HashMap<String, String> teamMap;
    private SubscriptionViewModel svw;
    private TeamViewModel tvw;
    private MatchpastViewModel mpvw;
    private MatchnextViewModel mnvw;
    private ArrayList<ArrayList<String>> weatherData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        steps =  findViewById(R.id.steps);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMatchScheduleList = new ArrayList<>();
        teamMap = new HashMap<String, String>();
        weatherData = new ArrayList<ArrayList<String>>();

        mRequestQueue = Volley.newRequestQueue(this);

        InternetCheck ic = new InternetCheck();
        ic.execute();
        getWeatherData();
        launchNotification();

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint("Cari Pertandingan");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mMatchScheduleAdapter.getFilter().filter(newText);
                return false;
            }
        });

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        if(!isServiceRunning()){
            Intent serviceIntent = new Intent(this, StepService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
            Log.d("Service", "Send start");
        }
        notificationManager = NotificationManagerCompat.from(this);
    }

    public void launchEventDetail(View view) {
        Intent intent = new Intent(this, EventDetail.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("StepUpdate"));

    }

    public void startStepService(View view) {
        Intent serviceIntent = new Intent(this, StepService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
        Log.d("Service", "Send start");
    }

    public void stopStepService(View view) {
        Intent serviceIntent = new Intent(this, StepService.class);
        stopService(serviceIntent);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Step");
            steps.setText(message+"steps reached");
        }
    };

    private void getWeatherData(){
        String url_weather = "https://api.openweathermap.org/data/2.5/forecast?q=england&appid=c71912b40fda2a51ef24c2a3b6c29463";
        JsonObjectRequest requestWeather = new JsonObjectRequest(Request.Method.GET, url_weather, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray_w = response.getJSONArray("list");
                            String dtTimeStamp;
                            String weather;
                            for (int i = 0; i < jsonArray_w.length(); i++){
                                ArrayList<String> weatherArray = new ArrayList<String>(Arrays.asList("",""));
                                JSONObject dt = jsonArray_w.getJSONObject(i);
                                dtTimeStamp = dt.getString("dt");
                                Log.i("openweather", dtTimeStamp);
                                JSONObject weatherObject= dt.getJSONArray("weather").getJSONObject(0);
                                weather = weatherObject.getString("description");
                                Log.i("openWeather", weather);
                                weatherArray.set(0, dtTimeStamp);
                                weatherArray.set(1, weather);
                                weatherData.add(weatherArray);
                            }
                            for(int j=0; j<weatherData.size(); j++){
                                Log.i("openWeatherArray", weatherData.get(j).get(0));
                                Log.i("openWeatherArray", weatherData.get(j).get(1));
                            }
                            //getTeamData();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("onErrorResponse: ","gagal" );
            }
        });
        mRequestQueue.add(requestWeather);
    }

    private void getTeamData(){

        tvw = new ViewModelProvider(this).get(TeamViewModel.class);
        String url_1 = "http://134.209.97.218:5050/api/v1/json/1/search_all_teams.php?l=English_Premier_League";

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url_1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray_1 = response.getJSONArray("teams");

                            for (int i = 0; i < jsonArray_1.length(); i++) {
                                JSONObject team = jsonArray_1.getJSONObject(i);

                                String idTeam = team.getString("idTeam");

                                String teamLogo = team.getString("strTeamBadge");

                                Team t = new Team(Integer.parseInt(idTeam), teamLogo);
                                tvw.insert(t);

                                teamMap.put(idTeam, teamLogo);

                            }
                            getPastMatch();
                            getNextMatch();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("onErrorResponse: ", "gagal");
            }
        });
        mRequestQueue.add(request1);
    }

    private void getTeamDataOffline() {

        tvw = new ViewModelProvider(this).get(TeamViewModel.class);

        tvw.getAllTeams().observe(this, new Observer<List<Team>>() {
            @Override
            public void onChanged(List<Team> teams) {
                for (int i = 0 ; i < teams.size() ; i++) {
                    teamMap.put(teams.get(i).getId_team().toString(), teams.get(i).getTeam_image_url());
                }
            }
        });
        getPastMatchOffline();
        getNextMatchOffline();
    }

    private void getPastMatch(){

        mpvw = new ViewModelProvider(this).get(MatchpastViewModel.class);
        String url_2 = "http://134.209.97.218:5050/api/v1/json/1/eventspastleague.php?id=4328";
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url_2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray_2 = response.getJSONArray("events");

                            for (int j = 1; j < 6; j++) {
                                JSONObject match = jsonArray_2.getJSONObject(j);

                                String idEvent = match.getString("idEvent");

                                String homeTeamName = match.getString("strHomeTeam");
                                String awayTeamName = match.getString("strAwayTeam");
                                String homeTeamScore = match.getString("intHomeScore");
                                String awayTeamScore = match.getString("intAwayScore");
                                if (homeTeamScore == "null") {
                                    homeTeamScore = "-";
                                    awayTeamScore = "-";
                                }
                                String date = match.getString("dateEvent");

                                String idHomeTeam = match.getString("idHomeTeam");
                                String idAwayTeam = match.getString("idAwayTeam");

                                String homeImageUrl = teamMap.get(idHomeTeam);
                                String awayImageUrl = teamMap.get(idAwayTeam);

                                String homeShot = match.getString("intHomeShots");
                                String awayShot = match.getString("intAwayShots");

                                String homeScorer = match.getString("strHomeGoalDetails");
                                String awayScorer = match.getString("strAwayGoalDetails");

                                Matchpast m1 = new Matchpast(Integer.parseInt(idEvent), idHomeTeam, homeTeamName, homeTeamScore, homeImageUrl,
                                        homeShot, homeScorer, date, idAwayTeam, awayTeamName, awayTeamScore, awayImageUrl,
                                        awayShot, awayScorer);
                                mpvw.insert(m1);

                                mMatchScheduleList.add(new MatchSchedule(idHomeTeam, homeTeamName, homeTeamScore, homeImageUrl,
                                        homeShot, homeScorer, date, idAwayTeam, awayTeamName, awayTeamScore,
                                        awayImageUrl, awayShot, awayScorer));

                            }

                            mMatchScheduleAdapter = new MatchScheduleAdapter(MainActivity.this, mMatchScheduleList);
                            mRecyclerView.setAdapter(mMatchScheduleAdapter);
                            mMatchScheduleAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request2);
    }

    private void getPastMatchOffline(){
        mpvw = new ViewModelProvider(this).get(MatchpastViewModel.class);
        mpvw.getAllMatches().observe(this, new Observer<List<Matchpast>>() {
            @Override
            public void onChanged(List<Matchpast> matchpasts) {
                for (int i = 0; i < matchpasts.size() ; i++) {

                    String homeTeamName = matchpasts.get(i).getHomeTeamName();
                    String awayTeamName = matchpasts.get(i).getAwayTeamName();
                    String homeTeamScore = matchpasts.get(i).getHomeTeamScore();
                    String awayTeamScore = matchpasts.get(i).getAwayTeamScore();
                    if (homeTeamScore == "null") {
                        homeTeamScore = "-";
                        awayTeamScore = "-";
                    }
                    String date = matchpasts.get(i).getDate();

                    String idHomeTeam = matchpasts.get(i).getIdHomeTeam();
                    String idAwayTeam = matchpasts.get(i).getIdAwayTeam();

                    String homeImageUrl = teamMap.get(idHomeTeam);
                    String awayImageUrl = teamMap.get(idAwayTeam);

                    String homeShot = matchpasts.get(i).getHomeShot();
                    String awayShot = matchpasts.get(i).getAwayShot();

                    String homeScorer = matchpasts.get(i).getHomeScorer();
                    String awayScorer = matchpasts.get(i).getAwayScorer();

                    mMatchScheduleList.add(new MatchSchedule(idHomeTeam, homeTeamName, homeTeamScore, homeImageUrl,
                            homeShot, homeScorer, date, idAwayTeam, awayTeamName, awayTeamScore,
                            awayImageUrl, awayShot, awayScorer));
                }

            mMatchScheduleAdapter = new MatchScheduleAdapter(MainActivity.this, mMatchScheduleList);
            mRecyclerView.setAdapter(mMatchScheduleAdapter);
            mMatchScheduleAdapter.setOnItemClickListener(MainActivity.this);
            }
        });
    }

    private void getNextMatch(){

        mnvw = new ViewModelProvider(this).get(MatchnextViewModel.class);
        String url_3 = "http://134.209.97.218:5050/api/v1/json/1/eventsnextleague.php?id=4328";
        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, url_3, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray_3 = response.getJSONArray("events");

                            for (int k = 1; k < 6; k++) {
                                JSONObject match = jsonArray_3.getJSONObject(k);

                                String idEvent = match.getString("idEvent");

                                String homeTeamName = match.getString("strHomeTeam");
                                String awayTeamName = match.getString("strAwayTeam");
                                String homeTeamScore = match.getString("intHomeScore");
                                String awayTeamScore = match.getString("intAwayScore");

                                String date = match.getString("dateEvent");
                                String time = match.getString("strTimeLocal");

                                String idHomeTeam = match.getString("idHomeTeam");
                                String idAwayTeam = match.getString("idAwayTeam");

                                if (homeTeamScore == "null") {
                                    if(date=="null" | time=="null"){
                                        homeTeamScore = "-";
                                        awayTeamScore = "-";
                                    } else {
                                        String dateTime = date + " " + time;
                                        String matchWeather = getWeather(dateTime);
                                        date += "\n"+matchWeather;
                                        homeTeamScore = "-";
                                        awayTeamScore = "-";
                                    }
                                }

                                String homeImageUrl = teamMap.get(idHomeTeam);
                                String awayImageUrl = teamMap.get(idAwayTeam);

                                String homeShot = match.getString("intHomeShots");
                                String awayShot = match.getString("intAwayShots");

                                String homeScorer = match.getString("strHomeGoalDetails");
                                String awayScorer = match.getString("strAwayGoalDetails");

                                Matchnext m1 = new Matchnext(Integer.parseInt(idEvent), idHomeTeam, homeTeamName, homeTeamScore, homeImageUrl,
                                        homeShot, homeScorer, date, idAwayTeam, awayTeamName, awayTeamScore, awayImageUrl,
                                        awayShot, awayScorer);
                                mnvw.insert(m1);

                                mMatchScheduleList.add(new MatchSchedule(idHomeTeam, homeTeamName, homeTeamScore, homeImageUrl,
                                        homeShot, homeScorer, date, idAwayTeam, awayTeamName, awayTeamScore,
                                        awayImageUrl, awayShot, awayScorer));
                            }

                            mMatchScheduleAdapter = new MatchScheduleAdapter(MainActivity.this, mMatchScheduleList);
                            mRecyclerView.setAdapter(mMatchScheduleAdapter);
                            mMatchScheduleAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request3);
    }

    private void getNextMatchOffline(){
        mnvw = new ViewModelProvider(this).get(MatchnextViewModel.class);
        mnvw.getAllMatches().observe(this, new Observer<List<Matchnext>>() {
            @Override
            public void onChanged(List<Matchnext> matchnexts) {
                for (int i = 0; i < matchnexts.size() ; i++) {

                    String homeTeamName = matchnexts.get(i).getHomeTeamName();
                    String awayTeamName = matchnexts.get(i).getAwayTeamName();
                    String homeTeamScore = matchnexts.get(i).getHomeTeamScore();
                    String awayTeamScore = matchnexts.get(i).getAwayTeamScore();
                    if (homeTeamScore == "null") {
                        homeTeamScore = "-";
                        awayTeamScore = "-";
                    }
                    String date = matchnexts.get(i).getDate();

                    String idHomeTeam = matchnexts.get(i).getIdHomeTeam();
                    String idAwayTeam = matchnexts.get(i).getIdAwayTeam();

                    String homeImageUrl = teamMap.get(idHomeTeam);
                    String awayImageUrl = teamMap.get(idAwayTeam);

                    String homeShot = matchnexts.get(i).getHomeShot();
                    String awayShot = matchnexts.get(i).getAwayShot();

                    String homeScorer = matchnexts.get(i).getHomeScorer();
                    String awayScorer = matchnexts.get(i).getAwayScorer();

                    mMatchScheduleList.add(new MatchSchedule(idHomeTeam, homeTeamName, homeTeamScore, homeImageUrl,
                            homeShot, homeScorer, date, idAwayTeam, awayTeamName, awayTeamScore,
                            awayImageUrl, awayShot, awayScorer));
                }

                mMatchScheduleAdapter = new MatchScheduleAdapter(MainActivity.this, mMatchScheduleList);
                mRecyclerView.setAdapter(mMatchScheduleAdapter);
                mMatchScheduleAdapter.setOnItemClickListener(MainActivity.this);
            }
        });
    }

    private Date getUnixFormat(String dateString) throws ParseException {
        Log.d("unixDate", dateString);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse(dateString);
        Log.d("unixDate", String.valueOf(date.getTime()));
        return date;
    }

    private String getWeather(String dateString) throws ParseException {
        Date matchDate = getUnixFormat(dateString);
        String weather = "";
        boolean found = false;
        int i = 0;
        while(!found && i<weatherData.size()){
            Date temp = new Date(Long.parseLong(weatherData.get(i).get(0))*1000);
            if(matchDate.compareTo(temp)<0){
                Log.d("getWeather match", String.valueOf(matchDate));
                Log.d("getWeather openw", String.valueOf(temp));
                found = true;
            } else {
                i++;
            }
        }

        if(i==0){
            i=1;
        }
        weather = weatherData.get(i-1).get(1);
        Log.d("getWeather", weather);
        return weather;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, EventDetail.class);
        MatchSchedule clickedItem = mMatchScheduleList.get(position);

        intent.putExtra("homeTeamId", clickedItem.getHomeTeamId());
        intent.putExtra("homeTeamName", clickedItem.getHomeTeamName());
        intent.putExtra("homeTeamScore", clickedItem.getHomeScore());
        intent.putExtra("homeTeamLogo", clickedItem.getHomeImageURL());
        intent.putExtra("homeTeamShot", clickedItem.getHomeShot());
        intent.putExtra("homeTeamScorer", clickedItem.getHomeScorer());

        intent.putExtra("date", clickedItem.getDate());

        intent.putExtra("awayTeamId", clickedItem.getAwayTeamId());
        intent.putExtra("awayTeamName", clickedItem.getAwayTeamName());
        intent.putExtra("awayTeamScore", clickedItem.getAwayScore());
        intent.putExtra("awayTeamLogo", clickedItem.getAwayImageURL());
        intent.putExtra("awayTeamShot", clickedItem.getAwayShot());
        intent.putExtra("awayTeamScorer", clickedItem.getAwayScorer());

        startActivity(intent);
    }

    private boolean isServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.bolasepak.StepService".equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public void launchNotification() {
        svw = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        svw.getAllSubscription().observe(this, new Observer<List<Subscription>>() {
            @Override
            public void onChanged(List<Subscription> subscriptions) {
                for (int j = 0 ; j < subscriptions.size() ; j++) {
                    String url_event = "http://134.209.97.218:5050/api/v1/json/1/eventsnext.php?id="+subscriptions.get(j).getId_team();
                    JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url_event, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jsonArray_1 = response.getJSONArray("events");

                                        for (int i = 0; i < jsonArray_1.length(); i++){
                                            JSONObject event = jsonArray_1.getJSONObject(i);

                                            String dateEvent = event.getString("dateEvent");
                                            String time = event.getString("strTime");
                                            dateEvent = dateEvent + " " + time;

                                            Date d1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateEvent);

                                            Date currentTime = Calendar.getInstance().getTime();

                                            long diffInMiles = d1.getTime() - currentTime.getTime();
                                            if (diffInMiles * 0.001 <= 864000) {
                                                long day = diffInMiles / 60 / 60 / 24 / 1000;
                                                Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_2_ID)
                                                        .setContentTitle("BolaSepak")
                                                        .setContentText(event.getString("strEvent") + " match starting in " + day + " day")
                                                        .setSmallIcon(R.drawable.ic_android_black_24dp)
                                                        .build();

                                                notificationManager.notify(2+i, notification);
                                            }
                                        }
                                    }
                                    catch (JSONException | ParseException e){
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.e("onErrorResponse: ","gagal" );
                        }
                    });
                    mRequestQueue.add(request1);
                }
            }
        });
    }

    public class InternetCheck extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                getTeamData();
            } else {
                getTeamDataOffline();
            }

            launchNotification();
        }
    }
}
