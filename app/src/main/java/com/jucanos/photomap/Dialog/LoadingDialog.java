package com.jucanos.photomap.Dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jucanos.photomap.R;


public class LoadingDialog extends DialogFragment {
    public LoadingDialog(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_loading,container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // remove dialog background
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return v;
    }
}
//    private static final int LAYOUT = R.layout.dialog_loading;
//
//    private Context context;
//
//    public LoadingDialog(Context context) {
//        super(context);
//        this.context = context;
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(LAYOUT);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        setCanceledOnTouchOutside(false);
//        setCancelable(false);
//    }