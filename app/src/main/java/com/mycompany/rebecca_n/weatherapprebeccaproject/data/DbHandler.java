package com.mycompany.rebecca_n.weatherapprebeccaproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.mycompany.rebecca_n.weatherapprebeccaproject.control.Weather;



public class DbHandler {

    private DbOpenHelper dbOpenHelper;

    public DbHandler(Context context) {

        dbOpenHelper = new DbOpenHelper(context);
    }

    public void insert(Weather weather) {

        //Get The DB
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db == null)
            return;

        //Special type of KV object, perfect for working with DB.
        ContentValues values = generateContentValues(weather);

        //Perform the insertion
        long id = -1;
        try {
            id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, values);
        } finally {
            //Must be performed
            db.close();
        }
        if (id != -1)
            weather.setId(id);
    }

    public void update(Weather weather) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db == null)
            return;

        ContentValues values = generateContentValues(weather);
        try {
            db.update(WeatherContract.WeatherEntry.TABLE_NAME, values, BaseColumns._ID + "=?", new String[]{String.valueOf(weather.getId())});
        } finally {
            db.close();
        }
    }

    public void delete(int id) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db == null)
            return;

        try {
            db.delete(WeatherContract.WeatherEntry.TABLE_NAME, BaseColumns._ID + "=?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }

    public Weather query(String name) {

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if (db == null)
            return null;

        //The cursor holds all the results fetched from the db
        //If there are several names fetch the first one by its ID
        Cursor cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME, null, WeatherContract.WeatherEntry.COLUMN_DATE + "=?", new String[]{name}, null, null, "DESC");

        Weather weather = null;
        try {
            if (cursor.moveToFirst()) {
                //If there are results, fetch the data and create the robot!
                String weatherId = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
                String dateTime = cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE));
                String location= cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_LOCATION));
                String pressure= cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_PRESSURE));
                String humidity=cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_HUMIDITY));
                String high_temperature=cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
                String min_temperature=cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
                String description=cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DESCRIPTION));
                String weatherIcon=cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON));

            }
        } finally {
            cursor.close();
        }

        return weather;
    }

    public Cursor queryAll() {

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        if (db == null)
            return null;
        Cursor cursor = null;
        cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME, null, null, null, null, null, WeatherContract.WeatherEntry.COLUMN_DATE+ " DESC");
        return cursor;
    }

    public void clearAll() {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        if (db == null)
            return;

        try {
            db.delete(WeatherContract.WeatherEntry.TABLE_NAME, null, null);
        } finally {
            db.close();
        }
    }

    @NonNull
    private ContentValues generateContentValues(Weather weather) {

        ContentValues values = new ContentValues();
        values.put(WeatherContract.WeatherEntry.COLUMN_DATE, weather.getDateTime());
        values.put(WeatherContract.WeatherEntry.COLUMN_DESCRIPTION, weather.getDescription());
        values.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, weather.getPressure());
        values.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, weather.getHumidity());
        values.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, weather.getMaxTemperature());
        values.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, weather.getMinTemperature());
        values.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, weather.getWeatherIcon());
        values.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, weather.getWeatherId());
       values.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, weather.getWindSpeed());
        values.put(WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION, weather.getWindDirection());


        return values;
    }
}
