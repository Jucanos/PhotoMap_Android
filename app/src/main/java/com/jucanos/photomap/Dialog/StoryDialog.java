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

public class StoryDialog extends Dialog implements View.OnClickListener{
    private StoryDialogListener longClickDialogListner;
    private static final int LAYOUT = R.layout.story_dialog;
    private Context context;


    public StoryDialog(Context context){
        super(context);
        this.context = context;
    }


    public void setDialogListener(StoryDialogListener dialogListener){
        this.longClickDialogListner = dialogListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button button_delete = (Button) findViewById(R.id.button_delete);
        final Button button_edit = (Button) findViewById(R.id.button_edit);
        final Button button_exit = (Button) findViewById(R.id.button_exit);
        final Button button_cancel = (Button) findViewById(R.id.button_cancel);


        button_delete.setOnClickListener(this);
        button_edit.setOnClickListener(this);
        button_exit.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_delete:
                longClickDialogListner.onDeleteClicked();
                dismiss();
                break;
            case R.id.button_edit:
                longClickDialogListner.onEditClicked();
                dismiss();
                break;
            case R.id.button_exit:
                longClickDialogListner.onExitClicked();
                dismiss();
                break;
            case R.id.button_cancel:
                longClickDialogListner.onCancelClicked();
                dismiss();
                break;
        }
    }

}
