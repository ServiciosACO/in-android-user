package co.kubo.indiesco.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by estacion on 31/01/18.
 */

public class ButtonMedium extends android.support.v7.widget.AppCompatButton {

    Utils utils = new Utils();

    public ButtonMedium(Context context) {
        super(context);
        this.setTypeface(utils.fuenteMedium(context));
    }
    public ButtonMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(utils.fuenteMedium(context));
    }
    public ButtonMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(utils.fuenteMedium(context));
    }
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}