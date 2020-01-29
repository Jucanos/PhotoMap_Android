package com.jucanos.photomap.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

import com.jucanos.photomap.Activity.AddGroupActivity;
import com.jucanos.photomap.Activity.EditGroupNameActivity;
import com.jucanos.photomap.Activity.GroupActivity;
import com.jucanos.photomap.Dialog.AddGroupDialog;
import com.jucanos.photomap.Dialog.AddGroupDialogListener;
import com.jucanos.photomap.Dialog.YesNoDialog;
import com.jucanos.photomap.Dialog.YesNoDialogListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.ListView.GroupListViewAdapter;
import com.jucanos.photomap.ListView.GroupListViewItem;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapList;
import com.jucanos.photomap.Structure.RemoveUserRequest;
import com.jucanos.photomap.Structure.RemoveUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentGroup extends Fragment {
    private RelativeLayout noGroup, existGroup;
    private ListView listView_group;
    public GlobalApplication globalApplication;
    private int groupCnt = 0;
    private GroupListViewAdapter adapter;

    private final int ADD_GROUP = 1;
    private final int EDIT_GROUP = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_group, fragmentContainer, false);
        globalApplication = GlobalApplication.getGlobalApplicationContext();

        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("그룹");
        toolbar.inflateMenu(R.menu.menu_fragment_group);
        setHasOptionsMenu(true);

        final String mid = getActivity().getIntent().getStringExtra("mid");
        boolean fromLink = false;
        if (mid != null) {
            fromLink = true;
        }
        Log.e("MainFragmentGroup", "[mid] : " + mid);

        noGroup = view.findViewById(R.id.layout_noGroup);
        existGroup = view.findViewById(R.id.layout_existGroup);
        listView_group = view.findViewById(R.id.listView_group);

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
                String mid = groupListViewItem.getMid();
                redirectGroupActivity(mid);
            }
        });

        listView_group.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AddGroupDialog dialog = new AddGroupDialog(getContext(), "그룹이름");
                dialog.setDialogListener(new AddGroupDialogListener() {
                    @Override
                    public void onGroupNameClicked() {
                        Toast.makeText(getContext(), "change onGroupNameClicked is clicked", Toast.LENGTH_SHORT).show();
                        redirectEditGroupNameActivity(adapter.getItem(position).getTitle(), adapter.getItem(position).getMid(), position);
                    }

                    @Override
                    public void onThumbnailClicked() {
                        Toast.makeText(getContext(), "change onThumbnailClicked is clicked", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onExitClicked() {
                        userRemove(globalApplication.token, adapter.getItem(position).getMid(), "true");
                        adapter.delete(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "exit onExitClicked is clicked", Toast.LENGTH_SHORT).show();
                    }

                });
                dialog.show();
                return true;
            }
        });
        if (fromLink) {
            YesNoDialog dialog = new YesNoDialog(getContext(), "그룹에 참여 하시겠습니까?");
            dialog.setDialogListener(new YesNoDialogListener() {
                @Override
                public void onPositiveClicked() {
                    Toast.makeText(globalApplication, "onPositiveClicked", Toast.LENGTH_SHORT).show();
                    userRemove(globalApplication.token, mid, "false");
                }

                @Override
                public void onNegativeClicked() {
                    Toast.makeText(globalApplication, "onNegativeClicked", Toast.LENGTH_SHORT).show();
                    getMapList(globalApplication.token);
                }
            });
            dialog.show();
        } else {
            getMapList(globalApplication.token);
        }

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
                redirectAddGroupActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void redirectGroupActivity(String mid) {
        final Intent intent = new Intent(getActivity(), GroupActivity.class);
        intent.putExtra("mid", mid);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_not_move);
    }

    public void redirectAddGroupActivity() {
        Intent intent = new Intent(getActivity(), AddGroupActivity.class);
        getActivity().startActivityForResult(intent, ADD_GROUP);
        getActivity().overridePendingTransition(R.anim.anim_not_move, R.anim.anim_not_move);
    }

    private void redirectEditGroupNameActivity(String name, String mid, int pos) {
        final Intent intent = new Intent(getActivity(), EditGroupNameActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("mid", mid);
        intent.putExtra("pos", pos);
        getActivity().startActivityForResult(intent, EDIT_GROUP);
        getActivity().overridePendingTransition(R.anim.anim_not_move, R.anim.anim_not_move);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case ADD_GROUP:
                    String mapTokpen = data.getStringExtra("mapToken");
                    String mapName = data.getStringExtra("mapName");
                    Log.e("MainFragmentGroup", "[mapToken] : " + mapTokpen);
                    Log.e("MainFragmentGroup", "[mapName]" + mapName);
                    addGroup(mapName, mapTokpen);
                    break;
                case EDIT_GROUP:
                    String name = data.getStringExtra("name");
                    int pos = data.getIntExtra("pos", -1);
                    adapter.getItem(pos).setTitle(name);
                    break;
                default:
                    break;
            }
        }
    }

    public void addGroup(String name, String mid) {
        adapter.addItem(new GroupListViewItem(mid, name));
        adapter.notifyDataSetChanged();
        setLayout();
    }

    public void getMapList(String token) {
        final Call<GetMapList> res = NetworkHelper.getInstance().getService().getMapList("Bearer " + token);
        res.enqueue(new Callback<GetMapList>() {
            @Override
            public void onResponse(Call<GetMapList> call, Response<GetMapList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        GetMapList getMapList = response.body();
                        for (int i = 0; i < getMapList.getGetMapListDatas().size(); i++) {
                            Log.e("LoginActivity", "[mid]" + getMapList.getGetMapListDatas().get(i).getMid());
                            Log.e("LoginActivity", "[getName]" + getMapList.getGetMapListDatas().get(i).getName());
                            String name = getMapList.getGetMapListDatas().get(i).getName();
                            String mid = getMapList.getGetMapListDatas().get(i).getMid();
                            addGroup(name, mid);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("LoginActivity", "[onResponse] " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<GetMapList> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }

    public void userRemove(String token, String mid, final String remove) {
        final Call<RemoveUser> res = NetworkHelper.getInstance().getService().userRemove("Bearer " + token, mid, new RemoveUserRequest(remove));
        res.enqueue(new Callback<RemoveUser>() {
            @Override
            public void onResponse(Call<RemoveUser> call, Response<RemoveUser> response) {
                if (response.isSuccessful()) {
                    Log.e("MainFragmentGroup", "[onResponse] is Successful");
                    if (remove.equals("false")) {
                        getMapList(globalApplication.token);
                    }
                } else {
                    Log.e("MainFragmentGroup", "[onResponse] " + Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(Call<RemoveUser> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }


    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void setLayout() {
        if (adapter.getCount() == 0) {
            noGroup.setVisibility(View.VISIBLE);
            existGroup.setVisibility(View.GONE);
        } else {
            noGroup.setVisibility(View.GONE);
            existGroup.setVisibility(View.VISIBLE);
        }
    }
}
