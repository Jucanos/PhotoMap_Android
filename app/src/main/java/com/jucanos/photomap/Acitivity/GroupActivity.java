package com.jucanos.photomap.Acitivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.jucanos.photomap.ListView.GroupListViewAdapter;
import com.jucanos.photomap.ListView.MemberListViewAdapter;
import com.jucanos.photomap.R;

public class GroupActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout_drawer;
    private ListView listView_member;
    private MemberListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group Name");

        drawerLayout_drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout_drawer.openDrawer(Gravity.RIGHT);
        drawerLayout_drawer.closeDrawer(GravityCompat.END);

        adapter = new MemberListViewAdapter();
        listView_member = (ListView) findViewById(R.id.listView_member);
        listView_member.setAdapter(adapter);
        adapter.addItem(null, "그룹멤버 초대");
        adapter.notifyDataSetChanged();
        addGroupTest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_group, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // 오른쪽 상단 메뉴 버튼
            case R.id.menu_menu:
                drawerLayout_drawer.openDrawer(Gravity.END);
                return true;
            // 뒤로가기 버튼
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_not_move, R.anim.anim_slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ====================================================================== for test Code
    // ====================================================================== for test Code
    void addGroupTest(){
        Drawable drawable = getResources().getDrawable(R.drawable.test_image);
        Bitmap bm = ((BitmapDrawable)drawable).getBitmap();
        adapter.addItem(bm, "일청원");
        adapter.addItem(bm, "이청원");
        adapter.addItem(bm, "삼청원");
        adapter.addItem(bm, "사청원");
        adapter.notifyDataSetChanged();
    }
}
