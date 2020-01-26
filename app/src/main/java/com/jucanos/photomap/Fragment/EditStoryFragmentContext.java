package com.jucanos.photomap.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.jucanos.photomap.Activity.AddStoryActivity;
import com.jucanos.photomap.Activity.EditStoryActivity;
import com.jucanos.photomap.R;

import java.io.IOException;

public class EditStoryFragmentContext extends Fragment {
    private EditText editText_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_story_description, fragmentContainer, false);
        Toast.makeText(getActivity().getApplicationContext(), "AddStoryFragmentDescription onCreateView", Toast.LENGTH_SHORT).show();

        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("내용 수정");
        setHasOptionsMenu(true);

        String context = getActivity().getIntent().getStringExtra("context");
        editText_description = view.findViewById(R.id.editText_description);
        editText_description.setHint(context);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_add_story_description, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_ok:
                ((EditStoryActivity) getActivity()).editStory();
                return true;
            case android.R.id.home:
                ((EditStoryActivity) getActivity()).setFrag(0);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public String getDescription() {
        return editText_description.getText().toString();
    }
}
