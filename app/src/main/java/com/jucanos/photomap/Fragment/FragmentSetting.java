package com.jucanos.photomap.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jucanos.photomap.Activity.MainActivity;
import com.jucanos.photomap.Activity.NoticeActivity;
import com.jucanos.photomap.R;

public class FragmentSetting extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        TextView textView_notice= view.findViewById(R.id.textView_notice);
        textView_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectNoticeActivity();
            }
        });

        return view;
    }
    void redirectNoticeActivity(){
        final Intent intent = new Intent(getActivity(), NoticeActivity.class);
        getActivity().startActivity(intent);
    }
}