package com.mycompany.rebecca_n.weatherapprebeccaproject.networking;

/**
 * Created by Rebecca_N on 02/20/2016.
 */
public interface OmdbApiConstants {

    String WEATHER_REQUEST_TAG = "weatherRequest";

    final String BASE_URL =
            "http://api.openweathermap.org/data/2.5/forecast/daily?";
    final String QUERY_PARAM = "q=";
    final String FORMAT_PARAM = "mode";
    final String UNITS_PARAM = "&units=metric";
    final String DAYS_PARAM = "&cnt=";
    final String APPID_PARAM = "&APPID=";

    String locationQuery ="london";


    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;

    String units = "metric";
    String format = "json";
    int numDays = 7;
    String  OPEN_WEATHER_MAP_API_KEY="6bfb45086fe7c9887548fe11fa9cb66f";
    // Location information
    final String OWM_CITY = "city";
    final String OWM_CITY_NAME = "name";
    final String OWM_COORD = "coord";

    // Location coordinate
    final String OWM_LATITUDE = "lat";
    final String OWM_LONGITUDE = "lon";

    // Weather information.  Each day's forecast info is an element of the "list" array.
    final String OWM_LIST = "list";

    final String OWM_PRESSURE = "pressure";
    final String OWM_HUMIDITY = "humidity";
    final String OWM_WINDSPEED = "speed";
    final String OWM_WIND_DIRECTION = "deg";

    // All temperatures are children of the "temp" object.
    final String OWM_TEMPERATURE = "temp";
    final String OWM_MAX = "max";
    final String OWM_MIN = "min";

    final String OWM_WEATHER = "weather";
    final String OWM_DESCRIPTION = "main";
    final String OWM_WEATHER_ID = "id";






}

   // String BASE_URL = "http://www.omdbapi.com/?";
    //String SEARCH_BY_TITLE = "s=";
    //String SEARCH_BY_IMDB_ID = "i=";

    //String MOVIE_TITLE_KEY = "Title";
    //String MOVIE_YEAR_KEY = "Year";
    //String MOVIE_POSTER_URL_KEY = "Poster";
    //String MOVIES_LIST_KEY = "Search";

