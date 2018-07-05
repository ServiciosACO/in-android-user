package co.kubo.indiesco.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by estacion on 31/01/18.
 */

public class AutoCompleteTextViewRegular extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    Utils utils = new Utils();

    public AutoCompleteTextViewRegular(Context context) {
        super(context);
        this.setTypeface(utils.fuenteRegular(context));
    }
    public AutoCompleteTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(utils.fuenteRegular(context));
    }
    public AutoCompleteTextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(utils.fuenteRegular(context));
    }
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}