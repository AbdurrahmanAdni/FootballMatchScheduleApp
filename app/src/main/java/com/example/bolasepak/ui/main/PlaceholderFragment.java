package com.example.bolasepak.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bolasepak.MainActivity;
import com.example.bolasepak.MatchSchedule;
import com.example.bolasepak.MatchScheduleAdapter;
import com.example.bolasepak.R;
import com.example.bolasepak.TeamDetail;
import com.example.bolasepak.database.Entity.Matchpast;
import com.example.bolasepak.database.Entity.Team;
import com.example.bolasepak.database.ViewModel.MatchpastViewModel;
import com.example.bolasepak.database.ViewModel.TeamViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private RecyclerView mRecyclerView;
    private MatchScheduleAdapter mMatchScheduleAdapter;
    private RequestQueue mRequestQueue;
    private HashMap<String, String> teamMap;
    private ArrayList<MatchSchedule> mMatchScheduleList;
    private String idTeam;
    private ArrayList<ArrayList<String>> weatherData;
    private TeamViewModel tvw;
    private MatchpastViewModel mpvw;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        Intent intent = getActivity().getIntent();
        idTeam = intent.getStringExtra("teamId");
        pageViewModel.setIndex(index);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_team_detail, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        mMatchScheduleList = new ArrayList<MatchSchedule>();
        teamMap = new HashMap<String, String>();
        weatherData = new ArrayList<ArrayList<String>>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        mRecyclerView = root.findViewById(R.id.detailRecycleView);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            Log.d("rV", "potrait");
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            Log.d("rV", "landscape");
        }

        InternetCheck ic = new InternetCheck();
        ic.execute();

        pageViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

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
                            getTeamData();
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

        String url_1 = "http://134.209.97.218:5050/api/v1/json/1/search_all_teams.php?l=English_Premier_League";

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url_1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray_1 = response.getJSONArray("teams");

                            for (int i = 0; i < jsonArray_1.length(); i++){
                                JSONObject team = jsonArray_1.getJSONObject(i);

                                String idTeam = team.getString("idTeam");

                                String teamLogo = team.getString("strTeamBadge");

                                teamMap.put(idTeam, teamLogo);

                            }
                            getPastMatch();
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
        mRequestQueue.add(request1);
    }

    private void getPastMatch(){
        String url_2 = "";

        if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
            url_2 = "http://134.209.97.218:5050/api/v1/json/1/eventsnext.php?id="+ TeamDetail.getTeamId;
        }else{
            url_2 = "http://134.209.97.218:5050/api/v1/json/1/eventslast.php?id="+ TeamDetail.getTeamId;
        }

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url_2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray_2;
                            if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
                                jsonArray_2 = response.getJSONArray("events");
                            }else{
                                jsonArray_2 = response.getJSONArray("results");
                            }

                            for (int j = 0; j < jsonArray_2.length(); j++) {
                                JSONObject match = jsonArray_2.getJSONObject(j);

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

                                mMatchScheduleList.add(new MatchSchedule(idHomeTeam, homeTeamName, homeTeamScore, homeImageUrl,
                                        homeShot, homeScorer, date, idAwayTeam, awayTeamName, awayTeamScore,
                                        awayImageUrl, awayShot, awayScorer));

                            }

                            mMatchScheduleAdapter = new MatchScheduleAdapter(getActivity(), mMatchScheduleList);
                            mRecyclerView.setAdapter(mMatchScheduleAdapter);

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
        mRequestQueue.add(request2);
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
    }

    private void getPastMatchOffline(){

        mpvw = new ViewModelProvider(this).get(MatchpastViewModel.class);
        mpvw.findMatchByID(Integer.parseInt(TeamDetail.getTeamId)).observe(this, new Observer<List<Matchpast>>() {
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

                mMatchScheduleAdapter = new MatchScheduleAdapter(getActivity(), mMatchScheduleList);
                mRecyclerView.setAdapter(mMatchScheduleAdapter);
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
                getWeatherData();
            } else {
                getTeamDataOffline();
            }
        }
    }
}

