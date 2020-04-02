package com.example.bolasepak.database.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "team")
public class Team {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_team")
    private Integer id_team;
    @ColumnInfo(name = "team_image_url")
    private String team_image_url;

    public Team(Integer id_team, String team_image_url) {
        this.id_team = id_team;
        this.team_image_url = team_image_url;
    }


    public void setId_team(Integer id_team) {
        this.id_team = id_team;
    }

    public Integer getId_team() {
        return id_team;
    }

    public String getTeam_image_url() {
        return team_image_url;
    }

    public void setTeam_image_url(String team_image_url) {
        this.team_image_url = team_image_url;
    }
}
