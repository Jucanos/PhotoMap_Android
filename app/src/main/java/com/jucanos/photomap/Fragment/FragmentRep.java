package com.jucanos.photomap.Fragment;

/**
 * Created by user on 2016-12-26.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jucanos.photomap.R;

public class FragmentRep extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rep, container, false);
        return view;
    }

}