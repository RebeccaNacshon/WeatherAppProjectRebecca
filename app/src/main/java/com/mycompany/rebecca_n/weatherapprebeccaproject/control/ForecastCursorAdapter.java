package com.mycompany.rebecca_n.weatherapprebeccaproject.control;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.rebecca_n.weatherapprebeccaproject.R;
import com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract;
import com.mycompany.rebecca_n.weatherapprebeccaproject.networking.VolleyManager;
import com.mycompany.rebecca_n.weatherapprebeccaproject.view.WeatherViewHolder;

import static android.R.attr.name;
import static android.provider.BaseColumns._ID;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_DATE;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_DESCRIPTION;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_LOCATION;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_MAX_TEMP;
import static com.mycompany.rebecca_n.weatherapprebeccaproject.data.WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON;

/**
 * Created by Rebecca_N on 3/1/2017.
 */

public class ForecastCursorAdapter extends RecyclerView.Adapter<WeatherViewHolder>  {
    private Cursor cursor;
    private WeatherClickListener weatherClickListener;
    private Context context;

    public ForecastCursorAdapter(Cursor cursor, WeatherClickListener weatherClickListener) {

        this.cursor = cursor;
        this.weatherClickListener = weatherClickListener;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {

        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {

        if (cursor != null && cursor.moveToPosition(position))
            return cursor.getLong(cursor.getColumnIndex(_ID));
        return -1;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WeatherViewHolder holder, int position) {

        if (cursor == null)
            return;

        if (!cursor.moveToPosition(position))
            return;


        holder.cityField.setText(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
        holder.dateField.setText(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        holder.currentTemperatureField.setText(cursor.getString(cursor.getColumnIndex(COLUMN_MAX_TEMP)));
        holder.weatherIcon.setImageUrl(cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_ICON)), VolleyManager.getInstance(context).getImageLoader());
        holder.detailsField.setText(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               weatherClickListener.onClick(getItemId(holder.getAdapterPosition()));


            }
        });
    }

    @Override
    public int getItemCount() {

        if (cursor == null)
            return 0;
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        if (cursor != null)
            cursor.close();
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
