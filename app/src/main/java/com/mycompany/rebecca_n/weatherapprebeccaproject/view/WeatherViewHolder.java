package com.mycompany.rebecca_n.weatherapprebeccaproject.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mycompany.rebecca_n.weatherapprebeccaproject.R;


/**
 * Created by Rebecca_N on 02/23/2017.
 */
public class WeatherViewHolder extends RecyclerView.ViewHolder {
    public TextView cityField;
    private TextView updatedField;
    public TextView detailsField;
    public TextView currentTemperatureField;
    private TextView humidity_field;
    private TextView pressure_field;
    public NetworkImageView weatherIcon;
    public TextView dateField;
    /**
     * Creates a new item view holder.
     *
     * @param itemView The view that each item in the list holds (hint: findViewById will be called on this view)
     */
    public WeatherViewHolder(View itemView) {



        super(itemView);
        cityField = (TextView)itemView.findViewById(R.id.city_field);
        updatedField = (TextView)itemView.findViewById(R.id.updated_field);
        detailsField = (TextView)itemView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)itemView.findViewById(R.id.current_temperature_field);
        //humidity_field= (TextView)itemView.findViewById(humidity_field);
        //pressure_field = (TextView)itemView.findViewById(pressure_field);
        weatherIcon = (NetworkImageView)itemView.findViewById(R.id.weather_icon);
        dateField=(TextView)itemView.findViewById(R.id.date_field);
    }
}
