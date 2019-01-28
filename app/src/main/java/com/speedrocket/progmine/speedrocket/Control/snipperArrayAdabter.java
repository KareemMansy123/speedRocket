package com.speedrocket.progmine.speedrocket.Control;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.speedrocket.progmine.speedrocket.R;

/**
 * Created by Ibrahim on 8/6/2018.
 */

public class snipperArrayAdabter extends ArrayAdapter<String> {
    String[] counters ;
    String[] prices ;
    Activity context ;

    public snipperArrayAdabter(@NonNull Activity context, int resource , String[] counters , String[] prices) {
        super(context, resource);
        this.counters=counters ;
        this.prices = prices ;
        this.context = context ;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View row = convertView ;
       if(row != null){
           LayoutInflater inflater = context.getLayoutInflater();
           row = inflater.inflate(R.layout.snipper_item, parent, false);

        }

        String country = counters[position] ;
        String price = prices[position];
        TextView countryTxt = (TextView) row.findViewById(R.id.country);
        TextView priceTxt = (TextView) row.findViewById(R.id.price);

        countryTxt.setText(country);
        priceTxt.setText(price);

      return row ;
    }
}
