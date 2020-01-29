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


public class YesNoDialog extends Dialog implements View.OnClickListener{
    private YesNoDialogListener yesNoListener;
    private static final int LAYOUT = R.layout.dialog_yes_no;

    private Context context;

    private Button btn_ok, btn_cancel;
    private String title;
    public YesNoDialog(Context context,String title){
        super(context);
        this.context = context;
        this.title = title;
    }

    public void setDialogListener(YesNoDialogListener dialogListener){
        this.yesNoListener = dialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView_title = findViewById(R.id.textView_title);
        textView_title.setText(title);

        btn_ok = (Button) findViewById(R.id.button_ok);
        btn_cancel = (Button) findViewById(R.id.button_cancel);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cancel:
                cancel();
                break;
            case R.id.button_ok:
                yesNoListener.onPositiveClicked();
                dismiss();
                break;
        }
    }

}
