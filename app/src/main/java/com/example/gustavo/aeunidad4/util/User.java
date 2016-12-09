package com.example.gustavo.aeunidad4.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stevens Vera on 08/12/2016.
 */

public class User extends SQLiteOpenHelper {
    public User(Context context) {
        super(context, "user", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table user(userid integer primary key,username varchar(40),password varchar(40),phone varchar(20), neigborhood varchar(50), zipcode varchar(10),city varchar(50),country varchar(50), state varchar(50), region varchar(50),street varchar(50),email varchar(255), streetnumber varchar(7),photo varchar(255), cellphone varchar(15),companyid varchar(500), roleid varchar(500),gender varchar(1),apikey varchar(40))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
