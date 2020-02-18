package co.kubo.indiesco.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Home;

public class DialogUpdate extends Dialog implements View.OnClickListener {

    private AppCompatButton btnMasTarde;
    private AppCompatButton btnActualizarAhora;

    private Home context;

    public DialogUpdate(@NonNull Home context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_update);
        Window window = this.getWindow();
        if (window != null) {
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btnActualizarAhora = findViewById(R.id.btnActualizarAhora);
        btnMasTarde = findViewById(R.id.btnMasTarde);

        setListeners();
    }

    private void setListeners() {
        btnActualizarAhora.setOnClickListener(this);
        btnMasTarde.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActualizarAhora:
                updateNow();
                break;
            case R.id.btnMasTarde:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void updateNow() {
        dismiss();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=co.kubo.indiesco"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
