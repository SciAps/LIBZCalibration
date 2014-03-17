package com.sciaps.android.libscalibrate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sciaps.android.libscalibrate.data.CalibrationAlloy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MonkeyFish on 1/22/14.
 */
public class CalibrationListAdapter extends ArrayAdapter<CalibrationAlloy> {


    private static final String TAG = "FilterAdapter";
    private CalibrationAlloy[] items;

    public CalibrationListAdapter(Context context, int textViewResourceId,
                                  CalibrationAlloy[] items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(android.R.layout.simple_list_item_activated_2, null);
        }
        CalibrationAlloy calibrationAlloy = items[position];


        TextView t1 = (TextView) v.findViewById(android.R.id.text1);
        if (t1 != null) {
            t1.setText(calibrationAlloy.name);
            TextView t2 = (TextView) v.findViewById(android.R.id.text2);

            if (calibrationAlloy.wasTaken) {
                t1.setTextColor(getContext().getResources().getColor(R.color.D_Green));

                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");


                Date taken = new Date(calibrationAlloy.takenDateMS);
                t2.setText(df.format(taken.getTime()));
                t2.setTextColor(getContext().getResources().getColor(R.color.D_Green));



            } else {
                t1.setTextColor(getContext().getResources().getColor(R.color.L_Red));
                t2.setText("");
                t2.setTextColor(getContext().getResources().getColor(R.color.L_Red));

            }
        }
        return v;
    }

    @Override
    public int getCount() {
        return items.length;
    }
}
