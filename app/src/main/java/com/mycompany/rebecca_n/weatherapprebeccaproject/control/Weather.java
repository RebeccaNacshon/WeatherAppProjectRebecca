package com.mycompany.rebecca_n.weatherapprebeccaproject.control;

/**
 * Created by Rebecca_N on 14/02/2017.
 */
public class Weather {

   String dateTime;
    String pressure;
    String humidity;
    String temperature;
    String description;
    String weatherIcon;
    private String weatherId;

    private long id;
    private String location;
    private String windSpeed;
    private String windDirection;
    private String maxTemperature;
    private String minTemperature;


    public Weather(String dateTime,
                   String location,
                   String pressure,
                   String humidity,
                   String max_temperature,
                   String min_temperature,
                   String WindSpeed,
                   String WindDirection,
                   String description,
                   String weatherIcon,
                   String weatherId) {

        this.dateTime = dateTime;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temperature=temperature;
        this.location=location;
        this.description=description;
        this.weatherIcon=weatherIcon;
        this.weatherId=weatherId;
    }



    public String getPressure() {
        return pressure;
    }
    public String getHumidity() {
        return humidity;
    }
    public String getTemperature() {
        return temperature;
    }
    public String getDescription() {
        return description;
    }
    public String getWeatherIcon() {
        return weatherIcon;
    }
    public String getWeatherId() {
        return weatherId;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
    public void setHumidity(String humidity) {
        this.humidity=humidity;
    }
    public void setTemperature(String high) {
        this.temperature=high;
    }
    public void setDescription(String description) {
        this.description=description;
    }
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon=weatherIcon;
    }
    public void setWeatherId(String weatherId) {
        this.weatherId=weatherId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

       Weather weather = (Weather) o;

        return weatherId == weather.weatherId;

    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location=location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime=dateTime;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }


    public void setWindSpeed(String windSpeed) {
        this.windSpeed=windSpeed;
    }
    public void setWindDirection(String windDirection) {
        this.windDirection=windDirection;
    }
    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature=maxTemperature;
    }
    public void setMinTemperature(String minTemperature) {
        this.minTemperature=minTemperature;
    }
}
