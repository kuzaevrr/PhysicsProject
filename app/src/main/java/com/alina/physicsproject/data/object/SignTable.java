package com.alina.physicsproject.data.object;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "sign")
public class SignTable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName;
    private String userMail;

    public SignTable(int id, String userName, String userMail) {
        this.id = id;
        this.userName = userName;
        this.userMail = userMail;
    }

    @Ignore
    public SignTable(String userName, String userMail) {
        this.userName = userName;
        this.userMail = userMail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userMail) {
        this.userName = userMail;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
}
