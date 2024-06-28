package com.gelora.absensi.support;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.gelora.absensi.R;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class CustomDecoratedBarcodeView extends DecoratedBarcodeView {
    public CustomDecoratedBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDecoratedBarcodeView(Context context) {
        super(context);
    }

    public void hideStatusText() {
        getStatusView().setText("");
    }

}