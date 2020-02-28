package com.jucanos.photomap.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.jucanos.photomap.Dialog.LoadingDialog;
import com.jucanos.photomap.Dialog.YesNoDialog;
import com.jucanos.photomap.Dialog.YesNoDialogListener;
import com.jucanos.photomap.GlobalApplication;
import com.jucanos.photomap.ListView.GroupListViewAdapter;
import com.jucanos.photomap.ListView.GroupListViewItem;
import com.jucanos.photomap.R;
import com.jucanos.photomap.RestApi.NetworkHelper;
import com.jucanos.photomap.Structure.GetMapList;
import com.jucanos.photomap.Structure.RemoveUser;
import com.jucanos.photomap.Structure.RemoveUserRequest;
import com.jucanos.photomap.Structure.SetMapRep;
import com.jucanos.photomap.Structure.SetMapRepRequest;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import mehdi.sakout.dynamicbox.DynamicBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentGroup extends Fragment {

    private GlobalApplication globalApplication;

    private RelativeLayout noGroup, existGroup;
    private ListView listView_group;
    private GroupListViewAdapter adapter;

    // request Code
    private final int ADD_GROUP = 1;
    private final int EDIT_GROUP = 2;

    // intentData
    private String mid;

    // for loading
    private DynamicBox box;
    private LoadingDialog loadingDialog;
    private String LOADING_ONLY_PROGRESS = "loading_only_progress";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup fragmentContainer, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_group, fragmentContainer, false);

        setToolbar(view);
        getIntentData();
        initMember(view);
        setBox();
        checkLink(mid);

        listView_group.setOnItemClickListener((parent, view1, position, id) -> {
            GroupListViewItem groupListViewItem = adapter.getItem(position);
            String mid = groupListViewItem.getMid();

            groupListViewItem.setActivated(true);
            globalApplication.mRefUser.child(mid).setValue(groupListViewItem.getCurLog());
            groupListViewItem.setLog(groupListViewItem.getCurLog(), true);
            redirectGroupActivity(mid, groupListViewItem.getTitle());
        });

        listView_group.setOnItemLongClickListener((parent, view12, position, id) -> {
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
                    final YesNoDialog yesNoDialog = new YesNoDialog(getContext(), getString(R.string.exit_group));
                    yesNoDialog.setDialogListener(new YesNoDialogListener() {
                        @Override
                        public void onPositiveClicked() {
                            yesNoDialog.dismiss();
                            userRemoveRequest(adapter.getItem(position).getMid(), position);
                        }

                        @Override
                        public void onNegativeClicked() {
                            yesNoDialog.dismiss();
                        }
                    });
                    yesNoDialog.show();
                }

                @Override
                public void onCancelClicked() {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        });
        return view;
    }

    private void getIntentData() {
        mid = Objects.requireNonNull(getActivity()).getIntent().getStringExtra("mid");
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_tb);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("그룹");
        toolbar.inflateMenu(R.menu.menu_fragment_group);
        setHasOptionsMenu(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMember(View view) {
        globalApplication = GlobalApplication.getGlobalApplicationContext();
        loadingDialog = new LoadingDialog(getActivity());
        noGroup = view.findViewById(R.id.layout_noGroup);
        existGroup = view.findViewById(R.id.layout_existGroup);
        listView_group = view.findViewById(R.id.listView_group);
        adapter = new GroupListViewAdapter();
        listView_group.setAdapter(adapter);
    }

    private void setBox() {
        box = new DynamicBox(getActivity(), listView_group);
        View customView = getLayoutInflater().inflate(R.layout.loading_only_progress, null, false);
        box.addCustomView(customView, LOADING_ONLY_PROGRESS);
    }

    private void checkLink(String mid) {
        boolean fromLink = false;
        if (mid != null) {
            fromLink = true;
        }
        Log.e("MainFragmentGroup", "fromLink : " + fromLink);

        if (fromLink) {
            YesNoDialog dialog = new YesNoDialog(getContext(), "그룹에 참여 하시겠습니까?");
            dialog.setDialogListener(new YesNoDialogListener() {
                @Override
                public void onPositiveClicked() {
                    Toast.makeText(getActivity(), "onPositiveClicked", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadingDialog.show();
                    userAddRequest(globalApplication.token, mid, -1);
                }

                @Override
                public void onNegativeClicked() {
                    Toast.makeText(getActivity(), "onNegativeClicked", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    getMapList(globalApplication.token);
                }
            });
            dialog.show();
        } else {
            getMapList(globalApplication.token);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_group, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_add) {
            redirectAddGroupActivity();
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

    private void redirectAddGroupActivity() {
        Intent intent = new Intent(getActivity(), AddGroupActivity.class);
        getActivity().startActivityForResult(intent, ADD_GROUP);
        getActivity().overridePendingTransition(R.anim.anim_not_move, R.anim.anim_not_move);
    }

    private void redirectEditGroupNameActivity(String name, String mid, int pos) {
        final Intent intent = new Intent(getActivity(), EditGroupNameActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("mid", mid);
        intent.putExtra("pos", pos);
        Objects.requireNonNull(getActivity()).startActivityForResult(intent, EDIT_GROUP);
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
    private void addGroup(String title, String mid, Date updatedAt, boolean pushBack) {
        final GroupListViewItem groupListViewItem = new GroupListViewItem();
        groupListViewItem.setTitle(title);
        groupListViewItem.setMid(mid);
        groupListViewItem.setUpdatedAt(updatedAt);
        groupListViewItem.setCurLog((long) 0);
        groupListViewItem.setPastLog((long) 0);
        adapter.addItem(groupListViewItem, pushBack);
    }

    // request : getMapList
    private void getMapList(String token) {
        final Call<GetMapList> res = NetworkHelper.getInstance().getService().getMapList(token);
        res.enqueue(new Callback<GetMapList>() {
            @Override
            public void onResponse(Call<GetMapList> call, Response<GetMapList> response) {
                adapter.clear();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Collections.sort(response.body().getGetMapListDatas(), (o1, o2) -> o1.getUpdatedAt().compareTo(o2.getUpdatedAt()) * -1);
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
                } else {
                    Log.e("LoginActivity", "[onResponse] " + response.code());
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

    private void userRemoveRequest(final String mid, final int position) {
        loadingDialog.show();
        final Call<RemoveUser> res = NetworkHelper.getInstance().getService().userRemove(globalApplication.token, mid, new RemoveUserRequest("true"));
        res.enqueue(new Callback<RemoveUser>() {
            @Override
            public void onResponse(Call<RemoveUser> call, Response<RemoveUser> response) {
                if (response.isSuccessful()) {
                    Log.e("MainFragmentGroup", "[onResponse] is Successful");
                    adapter.delete(position);
                    adapter.notifyDataSetChanged();
                    setMapRepRequest(mid, "true");
                } else {
                    Log.e("MainFragmentGroup", "[onResponse] " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RemoveUser> call, Throwable t) {
                Log.e("[onFailure]", t.getLocalizedMessage());
            }
        });
    }

    private void userAddRequest(String token, final String mid, final int position) {
        box.showCustomView(LOADING_ONLY_PROGRESS);
        final Call<RemoveUser> res = NetworkHelper.getInstance().getService().userRemove(token, mid, new RemoveUserRequest("false"));
        res.enqueue(new Callback<RemoveUser>() {
            @Override
            public void onResponse(Call<RemoveUser> call, Response<RemoveUser> response) {
                if (response.isSuccessful()) {
                    getMapList(globalApplication.token);
                } else {
                    Log.e("MainFragmentGroup", "userAddRequest isNotSuccessful() : " + response.code());
                    Toast.makeText(getActivity(), "이미 참여한 그룹입니다", Toast.LENGTH_SHORT).show();
                    getMapList(globalApplication.token);
                }
            }

            @Override
            public void onFailure(Call<RemoveUser> call, Throwable t) {
                Log.e("MainFragmentGroup", "userAddRequest onFailure : " + t.getLocalizedMessage());
                Toast.makeText(getActivity(), "그룹에 참여할 수 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMapRepRequest(final String mid, final String remove) {
        final Call<SetMapRep> res = NetworkHelper.getInstance().getService().setMapRep(globalApplication.token, mid, new SetMapRepRequest(remove));
        res.enqueue(new Callback<SetMapRep>() {
            @Override
            public void onResponse(Call<SetMapRep> call, Response<SetMapRep> response) {
                if (response.isSuccessful()) {
                    if (remove.equals("true")) {
                        globalApplication.authorization.getUserData().setPrimary(null);
                        loadingDialog.dismiss();
                    }
                } else {
                    Toast.makeText(getActivity(), "처리 실패", Toast.LENGTH_SHORT).show();
                    Log.e("GroupActivity", "[setMapRepRequest] onResponse is fail : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SetMapRep> call, Throwable t) {
                Toast.makeText(getActivity(), "처리 실패", Toast.LENGTH_SHORT).show();
                Log.e("GroupActivity", "[setMapRepRequest] is fail : " + t.getLocalizedMessage());
            }
        });
    }

    // decided by request result.
    void setLayout() {
        box.hideAll();
        loadingDialog.dismiss();
        if (adapter.getCount() == 0) {
            noGroup.setVisibility(View.VISIBLE);
            existGroup.setVisibility(View.GONE);
        } else {
            noGroup.setVisibility(View.GONE);
            existGroup.setVisibility(View.VISIBLE);
        }
    }


    public void onNewIntent(String mid) {
        this.mid = mid;
    }

    @Override
    public void onStop() {
        adapter.setActivated(true);
        super.onStop();
    }

    @Override
    public void onResume() {
        if (mid != null) {
            checkLink(mid);
            mid = null;
        } else {
            adapter.setActivated(false);
            Log.e("MainFragmentGroup", "[onResume] : after resort()");
            Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        }
        super.onResume();
    }
}

