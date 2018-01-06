package com.mycompany.rebecca_n.weatherapprebeccaproject.control;

import android.content.Context;

import java.util.concurrent.TimeUnit;

/**
 * Created by user on 6/1/2017.
 */

public class WeatherSync {

private static final int REPEAT_HOURS=3;
    private static boolean Initialized;
    private static final int REPEAT_SECONDS = (int) TimeUnit.HOURS.toSeconds(REPEAT_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = REPEAT_SECONDS / 3;
    private static final String WEATHER_SYNC_TAG = "weather-sync";


    static void scheduleFirebaseJobDispatcher(Context context)
    {




    }
    public static void initialize(MainActivity mainActivity) {


    }
}
