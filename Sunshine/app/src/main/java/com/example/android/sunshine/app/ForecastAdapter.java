package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * Created by chhavi on 19/7/16.
 */
public class ForecastAdapter extends CursorAdapter {

   Context mContext;

    public static class ViewHolder {

        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descView;
        public final TextView MaxtempView;
        public final TextView minTmpView;

        public ViewHolder(View view){
            iconView=(ImageView)view.findViewById(R.id.list_item_icon);
            dateView=(TextView)view.findViewById(R.id.list_item_date_textView);
            descView=(TextView)view.findViewById(R.id.list_item_forecast_textView);
            MaxtempView=(TextView)view.findViewById(R.id.list_item_high_textView);
            minTmpView=(TextView)view.findViewById(R.id.list_item_low_textView);
        }

    }
    public ForecastAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
        mContext=context;
    }


    private static final int VIEW_TYPE_TODAY=0;
    private static final int VIEW_TYPE_FUTURE_DAY=1;


//    private String formatHighLows(double high,double low){
//
//            boolean isMetric=Utility.isMetric(mContext);
//            String highlowstr=Utility.formatTemperature(high,isMetric)+"/"+Utility.formatTemperature(low,isMetric);
//
//            return highlowstr;
//    }
//
//    private String ConvertCursorRowtoUXFormat(Cursor cursor){
//
////        int max_temp_indx=cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
////        int min_temp_indx=cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
////        int date=cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
////        int short_desc=cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
//
//        String highLow=formatHighLows(cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
//
//        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE))+"-"+cursor.getString(ForecastFragment.COL_WEATHER_DESC)+"-"+highLow;
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

          //choose layout type
            int viewType=getItemViewType(cursor.getPosition());
            int layoutID=-1;
        switch (viewType){

            case VIEW_TYPE_TODAY:layoutID=R.layout.list_item_forecast_today;
                                    break;
            case VIEW_TYPE_FUTURE_DAY:layoutID=R.layout.list_item_forecast;
                                        break;
        }


          View view= LayoutInflater.from(context).inflate(layoutID,parent,false);

            ViewHolder holder=new ViewHolder(view);
            view.setTag(holder);

        return view;


    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        TextView tv=(TextView)view;
//        tv.setText(ConvertCursorRowtoUXFormat(cursor));

        ViewHolder viewHolder=(ViewHolder) view.getTag();

        //Use placeholder image for now
        //ImageView icon=(ImageView) view.findViewById(R.id.list_item_icon);
//        viewHolder.iconView.setImageResource(R.drawable.ic_launcher);

        int viewType=getItemViewType(cursor.getPosition());

        switch (viewType){

            case VIEW_TYPE_TODAY:viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                                    break;
                case VIEW_TYPE_FUTURE_DAY:viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                                            break;

            }

        //Read date from cursor
        long dateMillis=cursor.getLong(ForecastFragment.COL_WEATHER_DATE);

//        TextView dateView=(TextView) view.findViewById(R.id.list_item_date_textView);
        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateMillis));

        String desc=cursor.getString(ForecastFragment.COL_WEATHER_DESC);

//        TextView descView=(TextView)view.findViewById(R.id.list_item_forecast_textView);
        viewHolder.descView.setText(desc);

        boolean isMetric=Utility.isMetric(context);
        double high=cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);

//        TextView highTextView=(TextView)view.findViewById(R.id.list_item_high_textView);
        viewHolder.MaxtempView.setText(Utility.formatTemperature(context,high,isMetric));

        double low=cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);

//        TextView lowTextView=(TextView)view.findViewById(R.id.list_item_low_textView);
        viewHolder.minTmpView.setText(Utility.formatTemperature(context,low,isMetric));




    }

    @Override
    public int getItemViewType(int position) {

        return (position==0)?VIEW_TYPE_TODAY:VIEW_TYPE_FUTURE_DAY;

    }

    @Override
    public int getViewTypeCount() {

        return 2;
    }
}
