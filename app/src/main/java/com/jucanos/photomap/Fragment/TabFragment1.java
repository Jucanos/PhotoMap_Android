package com.jucanos.photomap.Fragment;

/**
 * Created by user on 2016-12-26.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;

import com.jucanos.photomap.R;


public class TabFragment1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_fragment_1, fragmentContainer, false);
        return view;
    }
}

