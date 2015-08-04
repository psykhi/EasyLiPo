package com.theboredengineers.easylipo.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

/**
 * Created by Benoit on 10/01/2015.
 */
public class RichNumberPicker extends NumberPicker {


    int minValue;
    int maxValue;
    int interval;
    int defaultValue;
    String units;

    public RichNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        minValue = attrs.getAttributeIntValue(null, "min", 0);
        maxValue = attrs.getAttributeIntValue(null, "max", 0);
        interval = attrs.getAttributeIntValue(null, "interval", 1);
        defaultValue = attrs.getAttributeIntValue(null, "defaultValue", 0);
        units = attrs.getAttributeValue(null, "units");
        if (units == null)
            units = "";

        setMinValue(0);
        int maxRealValue = (maxValue - minValue) / interval;
        setMaxValue(maxRealValue);
        setValue((defaultValue - minValue) / interval);
        String values[] = new String[(maxValue - minValue) / interval + 1];
        for (int i = 0; i <= (maxValue - minValue) / interval; i++) {
            values[i] = "" + (minValue + i * interval) + units;
        }
        setDisplayedValues(values);

    }

    public int getTrueValue() {
        int i = getValue();
        return minValue + i * interval;
    }

    public void setTrueValue(int value) {
        setValue((value - minValue) / interval);
    }
}
