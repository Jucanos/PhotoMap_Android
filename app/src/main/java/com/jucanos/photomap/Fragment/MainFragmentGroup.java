package com.jucanos.photomap.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.jucanos.photomap.Structure.GetMapListData;
import com.jucanos.photomap.Structure.RemoveUser;
import com.jucanos.photomap.Structure.RemoveUserRequest;
import com.jucanos.photomap.Structure.SetMapRep;
import com.jucanos.photomap.Structure.SetMapRepRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentGroup extends Fragment {
    private RelativeLayout noGroup, existGroup;
    private ListView listView_group;
    public GlobalApplication globalApplication;
    private GroupListViewAdapter adapter;
    private DynamicBox box;

    private final int ADD_GROUP = 1;
    private final int EDIT_GROUP = 2;

    private String mid;
    private String LOADING_ONLY_PROGRESS = "loading_only_progress";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_group, fragmentContainer, false);

        setToolbar(view);
        getIntentData();
        initMember(view);
        setBox();
        checkLink();

        // listView item click
        listView_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupListViewItem groupListViewItem = adapter.getItem(position);
                String mid = groupListViewItem.getMid();

                groupListViewItem.setActivated(true);
                globalApplication.mRefUser.child(mid).setValue(groupListViewItem.getCurLog());

                redirectGroupActivity(mid, groupListViewItem.getTitle());
            }
        });

        listView_group.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final GroupListViewItem groupListViewItem = adapter.getItem(position);
                final AddGroupDialog dialog = new AddGroupDialog(getContext(), groupListViewItem.getTitle());
                dialog.setDialogListener(new AddGroupDialogListener() {
                    @Override
                    public void onGroupNameClicked() {
                        dialog.dismiss();
                        redirectEditGroupNameActivity(adapter.getItem(position).getTitle(), adapter.getItem(position).getMid(), position);
                    }

                    @Override
                    public void onExitClicked() {
                        dialog.dismiss();
                        // 정말 나가시겠습니까?
                        final YesNoDialog yesNoDialog = new YesNoDialog(getContext(), getString(R.string.exit_group));
                        yesNoDialog.setDialogListener(new YesNoDialogListener() {
                            @Override
                            public void onPositiveClicked() {
                                yesNoDialog.dismiss();
                                Log.e("yesNoDialog", "onPositiveClicked");
                                userRemoveRequest(globalApplication.token, adapter.getItem(position).getMid(), "true", position);
                            }

                            @Override
                            public void onNegativeClicked() {
                                yesNoDialog.dismiss();
                            }
                        });
                        yesNoDialog.show();
                    }
                });
                dialog.show();
                return true;
            }
        });


        return view;
    }

    private void getIntentData() {
        mid = getActivity().getIntent().getStringExtra("mid");
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("그룹");
        toolbar.inflateMenu(R.menu.menu_fragment_group);
        setHasOptionsMenu(true);
    }

    private void initMember(View view) {
        globalApplication = GlobalApplication.getGlobalApplicationContext();

        noGroup = view.findViewById(R.id.layout_noGroup);
        existGroup = view.findViewById(R.id.layout_existGroup);
        listView_group = view.findViewById(R.id.listView_group);

        // groupListView
        adapter = new GroupListViewAdapter();
        listView_group.setAdapter(adapter);
    }

    private void setBox() {
        box = new DynamicBox(getActivity(), listView_group);
        box = new DynamicBox(getActivity(), listView_group);
        View customView = getLayoutInflater().inflate(R.layout.loading_only_progress, null, false);
        box.addCustomView(customView, LOADING_ONLY_PROGRESS);
    }

    private void checkLink() {
        boolean fromLink = false;
        if (mid != null) {
            fromLink = true;
        }
        if (fromLink) {
            YesNoDialog dialog = new YesNoDialog(getContext(), "그룹에 참여 하시겠습니까?");
            dialog.setDialogListener(new YesNoDialogListener() {
                @Override
                public void onPositiveClicked() {
                    Toast.makeText(globalApplication, "onPositiveClicked", Toast.LENGTH_SHORT).show();
                    userRemoveRequest(globalApplication.token, mid, "false", -1);
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
    }

    // toolbar menu
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


    // redirect
    private void redirectGroupActivity(String mid, String title) {
        final Intent intent = new Intent(getActivity(), GroupActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("title", title);
        startActivity(intent);
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

    // get intent result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ADD_GROUP:
                    box.showCustomView(LOADING_ONLY_PROGRESS);
                    String mapTokpen = data.getStringExtra("mapToken");
                    String mapName = data.getStringExtra("mapName");
                    Log.e("MainFragmentGroup", "[mapToken] : " + mapTokpen);
                    Log.e("MainFragmentGroup", "[mapName]" + mapName);
                    addGroup(mapName, mapTokpen, new Date(System.currentTimeMillis()), false);
                    adapter.notifyDataSetChanged();
                    setLayout();
                    break;
                case EDIT_GROUP:
                    String name = data.getStringExtra("title");
                    int pos = data.getIntExtra("pos", -1);
                    adapter.getItem(pos).setTitle(name);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    // addGroup
    public void addGroup(String title, String mid, Date updatedAt, boolean pushBack) {
        final GroupListViewItem groupListViewItem = new GroupListViewItem();
        groupListViewItem.setTitle(title);
        groupListViewItem.setMid(mid);
        groupListViewItem.setUpdatedAt(updatedAt);
        groupListViewItem.setCurLog((long) 0);
        groupListViewItem.setPastLog((long) 0);
        adapter.addItem(groupListViewItem, pushBack);
    }

    // request : getMapList
    public void getMapList(String token) {
        box.showCustomView(LOADING_ONLY_PROGRESS);
        final Call<GetMapList> res = NetworkHelper.getInstance().getService().getMapList(token);
        res.enqueue(new Callback<GetMapList>() {
            @Override
            public void onResponse(Call<GetMapList> call, Response<GetMapList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Collections.sort(response.body().getGetMapListDatas(), new Comparator<GetMapListData>() {
                            @Override
                            public int compare(GetMapListData o1, GetMapListData o2) {
                                return o1.getUpdatedAt().compareTo(o2.getUpdatedAt());
                            }
                        });
                        GetMapList getMapList = response.body();
                        for (int i = 0; i < getMapList.getGetMapListDatas().size(); i++) {
                            Log.e("LoginActivity", "[mid]" + getMapList.getGetMapListDatas().get(i).getMid());
                            Log.e("LoginActivity", "[getName]" + getMapList.getGetMapListDatas().get(i).getName());
                            Log.e("LoginActivity", "[updatedAt]" + getMapList.getGetMapListDatas().get(i).getUpdatedAt());
                            String name = getMapList.getGetMapListDatas().get(i).getName();
                            String mid = getMapList.getGetMapListDatas().get(i).getMid();
                            Date updatedAt = getMapList.getMapListDatas.get(i).getUpdatedAt();
                            addGroup(name, mid, updatedAt, true);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    setLayout();
                    box.hideAll();
                } else {
                    Log.e("LoginActivity", "[onResponse] " + Integer.toString(response.code()));
                    box.showExceptionLayout();
                }
            }

            @Override
            public void onFailure(Call<GetMapList> call, Throwable t) {
                Log.e("LoginActivity", "[onFailure] " + t.getLocalizedMessage());
                box.showExceptionLayout();
            }
        });
    }

    // request : uwerRemove
    public void userRemoveRequest(String token, final String mid, final String remove, final int position) {
        box.showLoadingLayout();
        final Call<RemoveUser> res = NetworkHelper.getInstance().getService().userRemove(token, mid, new RemoveUserRequest(remove));
        res.enqueue(new Callback<RemoveUser>() {
            @Override
            public void onResponse(Call<RemoveUser> call, Response<RemoveUser> response) {
                if (response.isSuccessful()) {
                    Log.e("MainFragmentGroup", "[onResponse] is Successful");
                    if (remove.equals("false")) {
                        getMapList(globalApplication.token);
                        adapter.notifyDataSetChanged();
                        box.hideAll();
                        setLayout();
                    } else {
                        setMapRepRequest(mid, "true");
                        adapter.delete(position);
                        adapter.notifyDataSetChanged();
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

    void setMapRepRequest(final String mid, final String remove) {
        final Call<SetMapRep> res = NetworkHelper.getInstance().getService().setMapRep(globalApplication.token, mid, new SetMapRepRequest(remove));
        res.enqueue(new Callback<SetMapRep>() {
            @Override
            public void onResponse(Call<SetMapRep> call, Response<SetMapRep> response) {
                if (response.isSuccessful()) {
                    Log.e("GroupActivity", "[setMapRepRequest] is success , mid : " + mid);
                    if (remove.equals("true")) {
                        Log.e("GroupActivity", "     globalApplication.authorization.getUserData().setPrimary(null)");
                        globalApplication.authorization.getUserData().setPrimary(null);
                    }
                    box.hideAll();
                } else {
                    Log.e("GroupActivity", "[setMapRepRequest] onResponse is fail : " + response.code());
                    box.hideAll();
                }
            }

            @Override
            public void onFailure(Call<SetMapRep> call, Throwable t) {
                Log.e("GroupActivity", "[setMapRepRequest] is fail : " + t.getLocalizedMessage());
                box.hideAll();
            }
        });
    }

    // decided by request result.
    void setLayout() {
        box.hideAll();
        if (adapter.getCount() == 0) {
            noGroup.setVisibility(View.VISIBLE);
            existGroup.setVisibility(View.GONE);
        } else {
            noGroup.setVisibility(View.GONE);
            existGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        adapter.setActivated(true);// adpater에 대해서 activated true로 바꿔줌으로써 firebase realtime db와 싱크를 맞춘다.
        super.onStop();
    }

    // lifeCycle
    @Override
    public void onResume() {
        adapter.setActivated(false); // adpater에 대해서 activated false로 바꿔줌으로써 firebase realtime db와 싱크를 맞춘다.
        adapter.resort();
        adapter.notifyDataSetChanged();
        Log.e("MainFragmentGroup", "[onResume] : after resort()");
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        super.onResume();

    }
}

