package com.jucanos.photomap.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jucanos.photomap.R;

public class RepDialog extends Dialog implements View.OnClickListener {
    private RepDialogListener longClickDialogListner;
    private static final int LAYOUT = R.layout.dialog_rep;
    private Context context;
    private String groupName;


    public RepDialog(Context context) {
        super(context);
        this.context = context;
    }


    public void setDialogListener(RepDialogListener dialogListener) {
        this.longClickDialogListner = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button button_set = findViewById(R.id.button_set);
        Button button_delete = findViewById(R.id.button_delete);
        Button button_cancel = findViewById(R.id.button_cancel);

        button_set.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_set:
                longClickDialogListner.onSetClicked();
                dismiss();
                break;
            case R.id.button_delete:
                longClickDialogListner.onDeleteClicked();
                dismiss();
                break;
            case R.id.button_cancel:
                longClickDialogListner.onCancelClicked();
                dismiss();
                break;

        }
    }

}
