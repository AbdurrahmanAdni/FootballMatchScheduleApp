package com.example.bolasepak.database.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subscription")
public class Subscription {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_team")
    private Integer id_team;

    public Subscription(Integer id_team) {
        this.id_team = id_team;
    }

    public void setId_team(Integer id_team) {
        this.id_team = id_team;
    }

    public Integer getId_team() {
        return id_team;
    }
}
