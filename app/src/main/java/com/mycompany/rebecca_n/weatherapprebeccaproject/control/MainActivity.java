package com.mycompany.rebecca_n.weatherapprebeccaproject.control;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.mycompany.rebecca_n.weatherapprebeccaproject.R;
import com.mycompany.rebecca_n.weatherapprebeccaproject.Utility.Utility;
import com.mycompany.rebecca_n.weatherapprebeccaproject.Utility.WeatherPreferences;

import com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract;
import com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants;
import com.mycompany.rebecca_n.weatherapprebeccaproject.networking.VolleyManager;
import com.mycompany.rebecca_n.weatherapprebeccaproject.services.WeatherService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.android.volley.Request.Method;
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
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.WEATHER_CONTENT_URI;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.APPID_PARAM;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.BASE_URL;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.DAYS_PARAM;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.OPEN_WEATHER_MAP_API_KEY;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.OWM_WINDSPEED;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.OWM_WIND_DIRECTION;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.QUERY_PARAM;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.UNITS_PARAM;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants.numDays;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, WeatherClickListener {


    private final String TAG = MainActivity.class.getSimpleName();
    private int Position = RecyclerView.NO_POSITION;


    public static final String[] PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON
    };




    public final int ADD_WEATHER_LOADER_ID = 169;
    private ForecastAdapter mForecastAdapter;
    private RecyclerView mRecyclerView;
    private ForecastCursorAdapter adapter;
    private TextView cityField;
    private TextView updatedField;
    private TextView detailsField;
    private TextView currentTemperatureField;
    private TextView humidity_field;
    private TextView pressure_field;
    private NetworkImageView weatherIcon;
    private String locationQuery;
    private String location;
    private String ImageURL;


    private TextView dateField;
    private Uri uri;
    private Context context;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  setupDbHandler();

        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) findViewById(R.id.humidity_field);
        pressure_field = (TextView) findViewById(R.id.pressure_field);
        weatherIcon = (NetworkImageView) findViewById(R.id.weather_icon);
        weatherIcon.setDefaultImageResId(R.drawable.sunny);
        dateField = (TextView) findViewById(R.id.date_field);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);


        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);


        adapter = new ForecastCursorAdapter(null, this);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setHasFixedSize(true);

        VolleyManager.getInstance(MainActivity.this).cancelAll(MainActivity.this, OmdbApiConstants.WEATHER_REQUEST_TAG);
        location = Utility.getPreferredLocation(this);
            uri = getIntent().getData();
        getLoaderManager().initLoader(ADD_WEATHER_LOADER_ID, null, this);

        WeatherSync.initialize(this);
    }

    

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), WeatherService.class));
    }

    private void getWeather(final String location) {

        String url = BASE_URL + QUERY_PARAM + location + APPID_PARAM + OPEN_WEATHER_MAP_API_KEY + UNITS_PARAM + DAYS_PARAM + numDays;
        final StringRequest request = new StringRequest(Method.GET, url,
                new Response.Listener<String>() {
                    public String dateTime;

                    @Override
                    public void onResponse(String responseString) {


                        //Try to create the JSON
                        JSONObject responseJson;
                        try {
                            responseJson = new JSONObject(responseString);
                            JSONObject forecastJson = new JSONObject(responseString);

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Couldn't Understand what the server was saying, please call the RESTful API again.", Toast.LENGTH_SHORT).show();
                            updateUiForBadResponse();
                            return;
                        }

                        //First step parse the result into movies JSON array
                        JSONArray WEATHERArray = responseJson.optJSONArray(OmdbApiConstants.OWM_LIST);


                        if (WEATHERArray == null) {
                            updateUiForBadResponse();
                            return;
                        }
                        Time dayTime = new Time();
                        dayTime.setToNow();

                        // we start at the day returned by local time. Otherwise this is a mess.
                        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                        // now we work exclusively in UTC
                        dayTime = new Time();
                        //Array was found, turn it into movies array
                        List<Weather> weathers = new ArrayList<>(numDays);

                        for (int i = 0; i < numDays; i++) {

                            String pressure;
                            String humidity;
                            String description;
                            String weatherId;


                            try {

                                //   ContentValues[] weatherContentValues = new ContentValues[WEATHERArray.length()];
                                JSONObject dayForecast = WEATHERArray.getJSONObject(i);
                                long dateofTime = dayTime.setJulianDay(julianStartDay + i);
                                JSONObject details = dayForecast.getJSONArray("weather").getJSONObject(0);
                                //     JSONObject main = dayForecast.getJSONObject("main");

                                // long dateTime= (int) dayTime.setJulianDay(julianStartDay+i);
                                String timeJson = dayForecast.optString("dt");
                                // String dateTime = convertTimeToDay(timeJson);

                                long timestamp = Long.parseLong(timeJson) * 1000L;
                                dateTime = (Utility.getDate(timestamp));

                                description = details.optString("description").toUpperCase(Locale.US);
                                String temperature = String.format("%.2f", dayForecast.optJSONObject("temp").optDouble("day")) + "°";
                                String high = String.format("%.2f", dayForecast.optJSONObject("temp").optDouble("max")) + "°";
                                String low = String.format("%.2f", dayForecast.optJSONObject("temp").optDouble("min")) + "°";
                                humidity = dayForecast.optString("humidity");
                                pressure = dayForecast.optString("pressure");
                                String cityName = responseJson.getJSONObject("city").optString("name").toUpperCase(Locale.US) + (",") + responseJson.getJSONObject("city").optString("country").toUpperCase(Locale.US);
                                weatherId = details.optString("id");
                                String windSpeed = dayForecast.optString(OWM_WINDSPEED);
                                String windDirection = dayForecast.optString(OWM_WIND_DIRECTION);
                                String iconText = details.optString("icon");
                                ImageURL = "http://openweathermap.org/img/w/" + iconText + ".png";


                                Weather weather = new Weather(dateTime, cityName,
                                        pressure,
                                        humidity,
                                        high, low, windSpeed, windDirection,
                                        description,
                                        ImageURL, weatherId);
                                weathers.add(weather);

                                ContentValues weatherValues = new ContentValues();
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, weather.getDateTime());
                                weatherValues.put(COLUMN_HUMIDITY, weather.getHumidity());
                                weatherValues.put(COLUMN_PRESSURE, weather.getPressure());
                                weatherValues.put(COLUMN_WIND_SPEED, windSpeed);
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, weather.getMaxTemperature());
                                weatherValues.put(COLUMN_MIN_TEMP, weather.getMinTemperature());
                                weatherValues.put(COLUMN_DESCRIPTION, weather.getDescription());
                                weatherValues.put(COLUMN_WEATHER_ID, weather.getWeatherId());

                                weatherValues.put(COLUMN_WEATHER_ICON, weather.getWeatherIcon());
                                getContentResolver().delete(WeatherContract.WeatherEntry.WEATHER_CONTENT_URI,null,null);
                                getContentResolver().insert(WeatherContract.WeatherEntry.WEATHER_CONTENT_URI, weatherValues);

                                // finish();
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, "Couldn't Understand what the server was saying, please call the RESTful API again.", Toast.LENGTH_SHORT).show();
                            }
                        }


                        //  cityField.setText(MainActivity.this.cursor.getString(MainActivity.this.cursor.getColumnIndex(COLUMN_LOCATION)));
                        cityField.setText(weathers.get(0).getLocation());
                        dateField.setText(weathers.get(0).getDateTime());
                        updatedField.setText(weathers.get(0).getDescription());
                        //detailsField = (TextView)findViewById(R.id.details_field);
                        currentTemperatureField.setText(weathers.get(0).getTemperature());
                        humidity_field.setText(weathers.get(0).getHumidity());
                        pressure_field.setText(weathers.get(0).getPressure());
                        weatherIcon.setImageUrl(weathers.get(0).getWeatherIcon(), VolleyManager.getInstance(MainActivity.this).getImageLoader());


                        //adapter.clear();

                        //adapter.addAll(weathers);

                    }

                    private void updateUiForBadResponse() {
                        // progressBar.setVisibility(View.GONE);
                        // adapter.clear();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                // progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "404", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(OmdbApiConstants.WEATHER_REQUEST_TAG);
        VolleyManager.getInstance(this).addRequest(this, request);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            locationQuery = Utility.getPreferredLocation(this);

            getWeather(locationQuery);
            return true;
        }
        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        } else if (id == R.id.action_refresh) {
            // refreshItem = item;
            locationQuery = Utility.getPreferredLocation(this);
            getWeather(locationQuery);
        } else if (id == R.id.action_share) {
            String playStoreLink = "https://play.google.com/store/apps/details?id=" +
                    getPackageName();

            String msg = getResources().getString(R.string.share_msg) + playStoreLink;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        double[] coords = WeatherPreferences.getLocationCoordinates(this);
        String posLat = Double.toString(coords[0]);
        String posLong = Double.toString(coords[1]);
        Uri geoLocation = Uri.parse("geo:" + posLat + "," + posLong);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        switch (id) {
            case ADD_WEATHER_LOADER_ID:
                Uri uri = WeatherContract.WeatherEntry.WEATHER_CONTENT_URI;
                /* Sort order: Ascending by date */
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                return new CursorLoader(this, //The Context calling (attached to) the manager
                        uri, //The uri for query a specific id
                        new String[]{
                                COLUMN_DATE,
                                COLUMN_MAX_TEMP,
                                COLUMN_MIN_TEMP ,
                                COLUMN_PRESSURE ,
                                COLUMN_HUMIDITY ,
                                COLUMN_LOCATION,
                                COLUMN_WIND_DIRECTION,
                                COLUMN_WEATHER_ICON,
                                COLUMN_WEATHER_ID,
                                COLUMN_WIND_DIRECTION,
                                COLUMN_WIND_SPEED,
                                COLUMN_DESCRIPTION
                        }, null, null, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
      cursor.moveToFirst();
      // if (cursor==null){

      Intent intentToSync = new Intent(this, WeatherService.class);
      startService(intentToSync);
       // }

        adapter.swapCursor(cursor);

       if (Position == RecyclerView.NO_POSITION) Position = 0;
        mRecyclerView.smoothScrollToPosition(Position);
        if (cursor.getCount() != 0)  mRecyclerView.setVisibility(View.VISIBLE);


        cursor.moveToFirst();
        cityField.setText(cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_LOCATION)));
        currentTemperatureField.setText(cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP)));
        dateField.setText(cursor.getString(cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE)));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onWeatherClicked(int position) {
        Intent weatherDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        weatherDetailIntent.setData(ContentUris.withAppendedId(WEATHER_CONTENT_URI, position));
        startActivity(weatherDetailIntent);
    }

    @Override
    public void onClick(long date) {
        Intent weatherDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        weatherDetailIntent.setData(ContentUris.withAppendedId(WEATHER_CONTENT_URI, date));
        startActivity(weatherDetailIntent);

    }


}
