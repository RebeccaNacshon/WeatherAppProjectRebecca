package com.mycompany.rebecca_n.weatherapprebeccaproject.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public interface WeatherContract {

    int DB_VERSION = 6;
    String DB_NAME = "Weather.db";

    //Content Provider Contract
    String CONTENT_AUTHORITY = "com.mycompany.rebecca_n.weatherapprebeccaproject";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    String WEATHER_PATH = WeatherEntry.TABLE_NAME;

    interface WeatherEntry extends BaseColumns {


        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_DATE = "date";

        /* Weather ID as returned by API, used to identify the icon to be used */
        public static final String COLUMN_WEATHER_ID = "weather_id";

        /* Min and max temperatures in °C for the day (stored as floats in the database) */
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";

        /* Humidity is stored as a float representing percentage */
        public static final String COLUMN_HUMIDITY = "humidity";

        /* Pressure is stored as a float representing percentage */
        public static final String COLUMN_PRESSURE = "pressure";

        /* Wind speed is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_WIND_DIRECTION = "direction";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_WEATHER_ICON= "icon";
        /*
         * Degrees are meteorological degrees (e.g, 0 is north, 180 is south).
         * Stored as floats in the database.
         *
         * Note: These degrees are not to be confused with temperature degrees of the weather.
         */
        public static final String COLUMN_DEGREES = "degrees";

        //Content Provider Constants
        Uri WEATHER_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, WEATHER_PATH);
        String WEATHER_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + WEATHER_PATH;
        String WEATHER_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + WEATHER_PATH;
    }
}
