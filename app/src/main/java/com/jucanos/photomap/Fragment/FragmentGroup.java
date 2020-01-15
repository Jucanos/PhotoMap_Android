package com.jucanos.photomap.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.jucanos.photomap.Activity.GroupActivity;
import com.jucanos.photomap.Dialog.GroupDialog;
import com.jucanos.photomap.Dialog.GroupDialogListener;
import com.jucanos.photomap.ListView.GroupListViewAdapter;
import com.jucanos.photomap.ListView.GroupListViewItem;
import com.jucanos.photomap.R;

public class FragmentGroup extends Fragment {
    private RelativeLayout noGroup, existGroup;
    private ListView listView_group;

    private int groupCnt = 0;
    private GroupListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, fragmentContainer, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("그룹");
        setHasOptionsMenu(true);


        noGroup = (RelativeLayout) view.findViewById(R.id.layout_noGroup);
        existGroup = (RelativeLayout) view.findViewById(R.id.layout_existGroup);
        listView_group = (ListView) view.findViewById(R.id.listView_group);

        // Adapter 생성
        adapter = new GroupListViewAdapter();
        // 리스트뷰 참조 및 Adapter달기
        listView_group.setAdapter(adapter);

        // addGroup 버튼

        // listView item click
        listView_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupListViewItem groupListViewItem = (GroupListViewItem) parent.getItemAtPosition(position);
                redirectGroupActivity();
            }
        });

        listView_group.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                GroupDialog dialog = new GroupDialog(getContext(), "그룹이름");
                dialog.setDialogListener(new GroupDialogListener() {
                    @Override
                    public void onGroupNameClicked() {
                        Toast.makeText(getContext(), "change onGroupNameClicked is clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onThumbnailClicked() {
                        Toast.makeText(getContext(), "change onThumbnailClicked is clicked", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onExitClicked() {
                        Toast.makeText(getContext(), "exit onExitClicked is clicked", Toast.LENGTH_SHORT).show();
                    }

                });
                dialog.show();
                return true;
            }
        });

        setLayout();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_group, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                addGroupTest();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void redirectGroupActivity() {
        final Intent intent = new Intent(getActivity(), GroupActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void setLayout() {
        if (groupCnt == 0) {
            noGroup.setVisibility(View.VISIBLE);
            existGroup.setVisibility(View.GONE);
        } else {
            noGroup.setVisibility(View.GONE);
            existGroup.setVisibility(View.VISIBLE);
        }
    }

    void addGroupTest() {
        Drawable drawable = getResources().getDrawable(R.drawable.test_image);
        Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
        adapter.addItem(bm, "Group Name");
        adapter.notifyDataSetChanged();
        groupCnt += 1;
        setLayout();
    }
}

