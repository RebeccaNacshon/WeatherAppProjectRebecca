package com.mycompany.rebecca_n.weatherapprebeccaproject.control;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.rebecca_n.weatherapprebeccaproject.R;
import com.mycompany.rebecca_n.weatherapprebeccaproject.networking.VolleyManager;
import com.mycompany.rebecca_n.weatherapprebeccaproject.view.WeatherViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rebecca_N on 14/02/2017.
 */
class ForecastAdapter extends RecyclerView.Adapter<WeatherViewHolder>{

    private final Context context;
    private List<Weather> weathers;
    private WeatherClickListener weatherClickListener;

    public ForecastAdapter(Context context) {

        this.weathers = new ArrayList<>();
        this.context = context;
        WeatherClickListener weatherClickListener = this.weatherClickListener;
    }

    public void setWeatherClickListener(WeatherClickListener weatherClickListener) {
        this.weatherClickListener = weatherClickListener;
    }


    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        return new WeatherViewHolder(view);
    }


    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {

        // In anonymous class variables must be final. to not change the method's signature we create a final copy of the position and call it
        final int pos = position;
        Weather weatherpos = weathers.get(position);
        holder.currentTemperatureField.setText(weatherpos.getTemperature());
       holder.detailsField.setText(String.valueOf(weatherpos.getWeatherId()));
        holder.weatherIcon.setImageUrl(weatherpos.getWeatherIcon(), VolleyManager.getInstance(context).getImageLoader());
        holder.detailsField.setText(weatherpos.description);
        holder.dateField.setText(weatherpos.getDateTime());


    }



    public int dataSize() {

        return weathers.size();
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public void clear() {

        weathers.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Weather> weathers) {

        //this.weathers=weathers;
      this.weathers.addAll(weathers);
        notifyDataSetChanged();
    }



}
