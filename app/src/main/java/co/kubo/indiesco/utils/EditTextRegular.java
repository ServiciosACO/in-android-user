package co.kubo.indiesco.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by estacion on 31/01/18.
 */

public class EditTextRegular extends android.support.v7.widget.AppCompatEditText {

    Utils utils = new Utils();

    public EditTextRegular(Context context) {
        super(context);
        this.setTypeface(utils.fuenteRegular(context));
    }
    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(utils.fuenteRegular(context));
    }
    public EditTextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(utils.fuenteRegular(context));
    }
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}