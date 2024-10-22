package com.gelora.absensi.support;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return String.valueOf((int) value);
    }
}