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

public class EditStoryFragmentTitle extends Fragment {
    private EditText editText_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_stroy_title, fragmentContainer, false);
        Toast.makeText(getActivity().getApplicationContext(), "AddStoryFragmentTitle onCreateView", Toast.LENGTH_SHORT).show();

        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("제목 수정");
        toolbar.inflateMenu(R.menu.menu_fragment_add_story_title);
        setHasOptionsMenu(true);

        String title =getActivity().getIntent().getStringExtra("title");
        editText_title = view.findViewById(R.id.editText_title);
        editText_title.setHint(title);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_add_story_title, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_next:
                ((EditStoryActivity) getActivity()).setFrag(1);
                return true;
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getTitle() {
        return editText_title.getText().toString();
    }
}
