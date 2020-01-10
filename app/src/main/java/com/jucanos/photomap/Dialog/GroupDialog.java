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

public class GroupDialog extends Dialog implements View.OnClickListener{
    private GroupDialogListener longClickDialogListner;
    private static final int LAYOUT = R.layout.group_dialog;
    private Context context;
    private Button btn_title, btn_exit, btn_image;
    private TextView txtView_title;
    private String groupName;


    public GroupDialog(Context context, String groupName){
        super(context);
        this.context = context;
        this.groupName = groupName;
    }


    public void setDialogListener(GroupDialogListener dialogListener){
        this.longClickDialogListner = dialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_title = (Button) findViewById(R.id.btn_title);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_image = (Button) findViewById(R.id.btn_image);

        txtView_title = (TextView) findViewById(R.id.txtView_title);
        txtView_title.setText(groupName);

        btn_title.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_title:
                longClickDialogListner.onTitleClicked();
                dismiss();
                break;
            case R.id.btn_image:
                longClickDialogListner.onImageClicked();
                dismiss();
                break;
            case R.id.btn_exit:
                longClickDialogListner.onExitClicked();
                dismiss();
                break;
        }
    }

}
