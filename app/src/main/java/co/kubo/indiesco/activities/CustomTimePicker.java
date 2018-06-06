package co.kubo.indiesco.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TimePicker;

/**
 * Created by estacion on 6/06/18.
 */

public class CustomTimePicker extends TimePicker {

    private final static int TIME_PICKER_INTERVAL = 5;
    private TimePicker mTimePicker;


    public CustomTimePicker(Context context) {
        super(context);
    }

    
}
