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

public class AddGroupDialog extends Dialog implements View.OnClickListener {
    private AddGroupDialogListener longClickDialogListner;
    private static final int LAYOUT = R.layout.dialog_group;
    private Context context;
    private String groupName;


    public AddGroupDialog(Context context, String groupName) {
        super(context);
        this.context = context;
        this.groupName = groupName;
    }


    public void setDialogListener(AddGroupDialogListener dialogListener) {
        this.longClickDialogListner = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button button_name = (Button) findViewById(R.id.button_name);
        final Button button_exit = (Button) findViewById(R.id.button_rep);
        final Button button_thumbnail = (Button) findViewById(R.id.button_thumbnail);
        final TextView textView_groupName = (TextView) findViewById(R.id.textView_groupName);
        textView_groupName.setText(groupName);

        button_name.setOnClickListener(this);
        button_exit.setOnClickListener(this);
        button_thumbnail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_name:
                longClickDialogListner.onGroupNameClicked();
                dismiss();
                break;
            case R.id.button_thumbnail:
                longClickDialogListner.onThumbnailClicked();
                dismiss();
                break;
            case R.id.button_rep:
                longClickDialogListner.onExitClicked();
                dismiss();
                break;
        }
    }

}
