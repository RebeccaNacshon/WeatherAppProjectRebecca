package com.mycompany.rebecca_n.weatherapprebeccaproject.control;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.rebecca_n.weatherapprebeccaproject.R;
import com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract;

import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_DATE;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_DESCRIPTION;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_HUMIDITY;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_LOCATION;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_MAX_TEMP;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_MIN_TEMP;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_PRESSURE;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_WEATHER_ID;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_WIND_DIRECTION;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_WIND_SPEED;

/**
 * Created by Rebecca_N on 2/26/2017.
 */
public class DetailActivity extends AppCompatActivity implements

        LoaderManager.LoaderCallbacks<Cursor> {

    private android.net.Uri Uri;
    private TextView DescrWeather;
    private TextView Location;
    private TextView Temperature;
    private TextView TempUnit;
    private TextView TempMin;
    private TextView TempMax;
    private TextView Hum;
    private TextView WindSpeed;
    private TextView WindDeg;
    private TextView Press;
    private TextView PressStatus;
    private TextView Visibility;
    private ImageView weatherImage;
    private TextView Sunrise;
    private TextView Sunset;
    public final int ID_DETAIL_LOADER=172;
    private Uri uri;
    private String[] projection= {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);
        DescrWeather = (TextView) findViewById(R.id.descrWeather);

        Location = (TextView) findViewById(R.id.location);

        Temperature = (TextView) findViewById(R.id.temp);

        TempUnit = (TextView) findViewById(R.id.tempUnit);

        TempMin = (TextView) findViewById(R.id.tempMin);

        TempMax = (TextView) findViewById(R.id.tempMax);

        Hum = (TextView) findViewById(R.id.humidity);

        WindSpeed = (TextView) findViewById(R.id.windSpeed);

        WindDeg = (TextView) findViewById(R.id.windDeg);

        Press = (TextView) findViewById(R.id.pressure);

        PressStatus = (TextView) findViewById(R.id.pressureStat);

        Visibility = (TextView) findViewById(R.id.visibility);

        weatherImage = (ImageView) findViewById(R.id.imgWeather);

        Sunrise = (TextView) findViewById(R.id.sunrise);

        Sunset = (TextView) findViewById(R.id.sunset);

        uri = getIntent().getData();
        
        if (uri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        /* This connects our Activity into the loader lifecycle. */
        getLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

        switch (loaderId) {

            case ID_DETAIL_LOADER:
                Uri uri = WeatherContract.WeatherEntry.WEATHER_CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

                return new CursorLoader(this, //The Context calling (attached to) the manager
                       uri, //The uri for query a specific id
                      projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
