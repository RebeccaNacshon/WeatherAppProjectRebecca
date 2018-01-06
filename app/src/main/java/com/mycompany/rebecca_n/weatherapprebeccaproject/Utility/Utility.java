package com.mycompany.rebecca_n.weatherapprebeccaproject.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.text.format.Time;

import com.mycompany.rebecca_n.weatherapprebeccaproject.R;
import com.mycompany.rebecca_n.weatherapprebeccaproject.control.Weather;
import com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract;

import java.util.Date;
import java.util.Locale;


/**
 * Created by Rebecca_N on 14/02/2017.
 */




    public class Utility {
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric))
                .equals(context.getString(R.string.pref_units_metric));
    }


    @NonNull
    public static ContentValues generateContentValues(Weather weather) {

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, weather.getDateTime());
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, weather.getHumidity());
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE,weather.getPressure());
        //weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
        // weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,weather.getMaxTemperature());
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,weather.getMinTemperature());
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DESCRIPTION, weather.getDescription());
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weather.getWeatherId());

        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, weather.getWeatherIcon());

        return weatherValues;
    }

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";



    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat;
        if (Utility.isMetric(context)) {
            windFormat = R.string.format_wind_kmh;
        } else {
            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        }
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }
    public static String getDate(long timeStamp){

        try{
            java.text.DateFormat sdf = new java.text.SimpleDateFormat("E, d MMMM \n   hh:mm aaa", Locale.getDefault());
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "Wrong Timestamp";
        }
    }


}
