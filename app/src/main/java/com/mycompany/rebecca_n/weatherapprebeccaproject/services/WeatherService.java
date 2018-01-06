package com.mycompany.rebecca_n.weatherapprebeccaproject.services;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mycompany.rebecca_n.weatherapprebeccaproject.Utility.Utility;
import com.mycompany.rebecca_n.weatherapprebeccaproject.control.MainActivity;
import com.mycompany.rebecca_n.weatherapprebeccaproject.control.Weather;

import com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract;
import com.mycompany.rebecca_n.weatherapprebeccaproject.networking.OmdbApiConstants;
import com.mycompany.rebecca_n.weatherapprebeccaproject.networking.VolleyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static com.mycompany.rebecca_n.weatherapprebeccaproject.R.id.humidity_field;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.R.id.pressure_field;
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

/**
 * Created by Rebecca_N on 2/25/2017.
 */
public class WeatherService extends IntentService {



    public WeatherService() {
        super(WeatherService.class.getSimpleName());

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String location = Utility.getPreferredLocation(this);

        getWeather(location);

    }

    private void getWeather(final String location) {

        String url=BASE_URL+QUERY_PARAM+location+APPID_PARAM+OPEN_WEATHER_MAP_API_KEY+UNITS_PARAM+DAYS_PARAM+numDays;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
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
                        //    Toast.makeText(MainActivity.this, "Couldn't Understand what the server was saying, please call the RESTful API again.", Toast.LENGTH_SHORT).show();
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


                        //Array was found, turn it into movies array
                        List<Weather> weathers = new ArrayList<>(numDays);
                        ContentValues[] weatherContentValues = new ContentValues[numDays];
                        for (int i = 0; i < numDays; i++) {

                            String pressure;
                            String humidity;
                            String description;
                            String weatherId;



                            try {

                                //   ContentValues[] weatherContentValues = new ContentValues[WEATHERArray.length()];
                                JSONObject dayForecast = WEATHERArray.getJSONObject(i);
                               // long dateofTime = dayTime.setJulianDay(julianStartDay + i);
                                JSONObject details = dayForecast.getJSONArray("weather").getJSONObject(0);
                                //     JSONObject main = dayForecast.getJSONObject("main");

                                // long dateTime= (int) dayTime.setJulianDay(julianStartDay+i);
                                String timeJson = dayForecast.optString("dt");
                                // String dateTime = convertTimeToDay(timeJson);

                                long timestamp = Long.parseLong(timeJson) * 1000L;
                                dateTime=(Utility.getDate(timestamp ));

                                description = details.optString("description").toUpperCase(Locale.US);
                                String temperature=String.format("%.2f", dayForecast.optJSONObject("temp").optDouble("day"))+ "°";
                                String high=String.format("%.2f", dayForecast.optJSONObject("temp").optDouble("max"))+ "°";
                                String low=String.format("%.2f", dayForecast.optJSONObject("temp").optDouble("min"))+ "°";
                                humidity = dayForecast.optString("humidity");
                                pressure = dayForecast.optString("pressure");
                                String cityName = responseJson.getJSONObject("city").optString("name").toUpperCase(Locale.US)+(",")+responseJson.getJSONObject("city").optString("country").toUpperCase(Locale.US);
                                weatherId=details.optString("id");
                                String windSpeed = dayForecast.optString(OWM_WINDSPEED);
                                String windDirection = dayForecast.optString(OWM_WIND_DIRECTION);
                                String iconText = details.optString("icon");
                                String ImageURL="http://openweathermap.org/img/w/"+iconText+".png";




                                Weather weather = new Weather(dateTime,cityName,
                                        pressure,
                                        humidity,
                                        high,low,windSpeed,windDirection,
                                        description,
                                        ImageURL,weatherId);
                                weathers.add(weather);

                                ContentValues weatherValues = new ContentValues();
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, weather.getDateTime());
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, weather.getHumidity());
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE,weather.getPressure());
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,weather.getMaxTemperature());
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,weather.getMinTemperature());
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DESCRIPTION, weather.getDescription());
                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weather.getWeatherId());

                                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON, weather.getWeatherIcon());

                                getContentResolver().delete(WeatherContract.WeatherEntry.WEATHER_CONTENT_URI,null,null);
                                getContentResolver().insert(WeatherContract.WeatherEntry.WEATHER_CONTENT_URI,weatherValues);


                            } catch (JSONException e) {
                                //Toast.makeText(context, "Couldn't Understand what the server was saying, please call the RESTful API again.", Toast.LENGTH_SHORT).show();
                            }
                        }
/*


                        //  cityField.setText(MainActivity.this.cursor.getString(MainActivity.this.cursor.getColumnIndex(COLUMN_LOCATION)));
                        // cityField.setText(weathers.get(0).getLocation());
                       dateField.setText(weathers.get(0).getDateTime());
                        updatedField.setText(weathers.get(0).getDescription());
                        //detailsField = (TextView)findViewById(R.id.details_field);
                        currentTemperatureField.setText(weathers.get(0).getTemperature());
                        humidity_field.setText(weathers.get(0).getHumidity());
                        pressure_field.setText(weathers.get(0).getPressure());
                        weatherIcon.setImageUrl(weathers.get(0).getWeatherIcon(), VolleyManager.getInstance(MainActivity.this).getImageLoader());

*/
                        //adapter.clear();

                        // adapter.addAll(weathers);

                    }

                    private void updateUiForBadResponse() {
                        // progressBar.setVisibility(View.GONE);
                        // adapter.clear();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag(OmdbApiConstants.WEATHER_REQUEST_TAG);
        VolleyManager.getInstance(this).addRequest(this, request);
    }

}