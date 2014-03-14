package com.sciaps.android.libscalibrate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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


        TextView tt = (TextView) v.findViewById(android.R.id.text1);
        if (tt != null) {
            tt.setText(calibrationAlloy.name);

            if (calibrationAlloy.wasTaken) {
                tt.setTextColor(getContext().getResources().getColor(R.color.L_Green));
            } else {
                tt.setTextColor(getContext().getResources().getColor(R.color.L_Red));

            }
        }
        return v;
    }

    @Override
    public int getCount() {
        return items.length;
    }
}
