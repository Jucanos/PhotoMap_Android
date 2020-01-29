package com.jucanos.photomap.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.jucanos.photomap.Activity.GroupActivity;
import com.jucanos.photomap.R;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MemberListViewItem> listViewItemList = new ArrayList<MemberListViewItem>();

    // ListViewAdapter의 생성자
    public MemberListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item_group" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_member, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        RelativeLayout relativeLayout_add_member = convertView.findViewById(R.id.relativeLayout_add_member);
        CircleImageView imgView_thumbnail = convertView.findViewById(R.id.imageView_thumbnail);
        TextView txtView_groupName = convertView.findViewById(R.id.textView_groupName);
        // GetUserInfoData Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MemberListViewItem listViewItem = listViewItemList.get(position);

        final String mid = ((GroupActivity)GroupActivity.mContext).getMid();

        relativeLayout_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedTemplate params = FeedTemplate
                        .newBuilder(ContentObject.newBuilder("님이 photoMap에 초대했습니다",
                                "https://ifh.cc/g/ODD7n.png",
                                LinkObject.newBuilder().setWebUrl("https://www.naver.com")
                                        .setMobileWebUrl("https://www.naver.com").build())
                                .build())
                        .addButton(new ButtonObject("초대 받기", LinkObject.newBuilder()
                                //.setWebUrl("https://www.naver.com")
                                //.setMobileWebUrl("https://www.naver.com")
                                .setAndroidExecutionParams("mid="+"4305549b5048e3b79fd61328f40e25b7")
                                .setIosExecutionParams("mid="+mid)
                                .build()))
                        .build();

                //  콜백으로 링크 잘갔는지 확인
                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                serverCallbackArgs.put("user_id", "${current_user_id}");
                serverCallbackArgs.put("product_id", "${shared_product_id}");

                KakaoLinkService.getInstance().sendDefault(context, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Logger.e(errorResult.toString());
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                    }
                });
            }
        });

        // 아이템 내 각 위젯에 데이터 반영
        imgView_thumbnail.setClipToOutline(true);
        if (listViewItem.getThumbnail() == null) {
            imgView_thumbnail.setImageResource(R.drawable.ic_icon_add_button_inside_black);
        } else {
            imgView_thumbnail.setImageBitmap(listViewItem.getThumbnail());
        }
        txtView_groupName.setText(listViewItem.getName());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MemberListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(Bitmap thumnail, String title) {
        MemberListViewItem item = new MemberListViewItem();
        item.setThumbnail(thumnail);
        item.setNmae(title);
        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList = new ArrayList<MemberListViewItem>();
    }

}