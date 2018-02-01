package co.kubo.indiesco.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by estacion on 31/01/18.
 */

public class Bold extends android.support.v7.widget.AppCompatTextView {

    Utils utils = new Utils();

    public Bold(Context context) {
        super(context);
        this.setTypeface(utils.fuenteBold(context));
    }
    public Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(utils.fuenteBold(context));
    }
    public Bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(utils.fuenteBold(context));
    }
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}