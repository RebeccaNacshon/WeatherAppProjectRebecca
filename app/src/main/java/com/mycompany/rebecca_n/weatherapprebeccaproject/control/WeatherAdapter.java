package com.mycompany.rebecca_n.weatherapprebeccaproject.control;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mycompany.rebecca_n.weatherapprebeccaproject.view.WeatherViewHolder;

/**
 * Created by user on 3/11/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    //final private ForecastAdapterOnClickHandler mClickHandler;

    public WeatherAdapter() {
        mContext = null;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
