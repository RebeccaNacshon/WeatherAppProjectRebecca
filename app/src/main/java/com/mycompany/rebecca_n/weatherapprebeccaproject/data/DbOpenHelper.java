package com.mycompany.rebecca_n.weatherapprebeccaproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(Context context) {

        super(context, WeatherContract.DB_NAME, null, WeatherContract.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String createSql = "CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME + " (" + WeatherContract.WeatherEntry.COLUMN_DATE + " TEXT," +
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP + " TEXT,"+WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON + " TEXT," + WeatherContract.WeatherEntry.COLUMN_MIN_TEMP + " TEXT," +
                WeatherContract.WeatherEntry.COLUMN_PRESSURE + " TEXT," + WeatherContract.WeatherEntry.COLUMN_HUMIDITY + " TEXT," + WeatherContract.WeatherEntry.COLUMN_LOCATION + " TEXT," +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " TEXT," + WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION + " TEXT," + WeatherContract.WeatherEntry.COLUMN_WIND_SPEED + " TEXT," + WeatherContract.WeatherEntry.COLUMN_DESCRIPTION + " TEXT," +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT);";
        sqLiteDatabase.execSQL(createSql);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        String deleteSql = "DROP TABLE IF EXISTS " + WeatherContract.WeatherEntry.TABLE_NAME+ ";";
        sqLiteDatabase.execSQL(deleteSql);
        onCreate(sqLiteDatabase);
    }
}
