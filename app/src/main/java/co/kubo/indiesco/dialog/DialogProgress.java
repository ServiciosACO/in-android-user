package co.kubo.indiesco.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import co.kubo.indiesco.R;

/**
 * Created by Diego on 26/02/2018.
 */

public class DialogProgress extends Dialog {

    Activity activity;

    public DialogProgress(Activity activity) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setContentView(R.layout.dialog_progress);
        Window window = this.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
