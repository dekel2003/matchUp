package com.com.fragments;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.models.RangeSeekBar;
import com.peek.matchup.ui2.R;

/**
 * Created by User on 06/12/2015.
 */
public class Setting extends Fragment {

    public Setting() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_setting,container,false);

        final TextView maxAge = (TextView) v.findViewById(R.id.maxAge);
        final TextView minAge = (TextView) v.findViewById(R.id.minAge);

        // create RangeSeekBar as Integer range between 20 and 75
        RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(18, 82, getActivity().getApplicationContext());
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
//                Log.i("Settings: ", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
                maxAge.setText(Integer.toString(maxValue));
                minAge.setText(Integer.toString(minValue));
            }
        });
        seekBar.setNotifyWhileDragging(true);


// add RangeSeekBar to pre-defined layout
        ((LinearLayout) v).addView(seekBar, 2);







        return v;
    }

}
