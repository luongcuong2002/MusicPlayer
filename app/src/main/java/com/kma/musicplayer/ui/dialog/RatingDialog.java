package com.kma.musicplayer.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import com.kma.musicplayer.R;

public class RatingDialog extends Dialog {

    private RatingBar rtb;
    private Activity activity;
    private Button btnRate, btnLater;

    public RatingDialog(Activity activity) {
        super(activity, R.style.CustomAlertDialog);
        this.activity = activity;
        setContentView(R.layout.dialog_rating_app);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(attributes);
        getWindow().setSoftInputMode(16);
        rtb = findViewById(R.id.rtb);
        btnRate = findViewById(R.id.btnRate);
        btnLater = findViewById(R.id.btnLater);
        onclick();
        rtb.setRating(5);
    }

    public void onclick() {
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rtb.getRating() == 0) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.please_rate_us), Toast.LENGTH_SHORT).show();
                    return;
                }
                dismiss();
            }
        });
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
