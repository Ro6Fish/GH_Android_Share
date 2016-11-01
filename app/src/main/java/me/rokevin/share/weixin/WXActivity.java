package me.rokevin.share.weixin;

import android.os.Bundle;
import android.view.View;

import me.rokevin.android.lib.sharesdk.util.ShareUtil;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;

public class WXActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = "这是标题";
                String description = "这是描述";
                String musicUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
                int imageId = R.mipmap.ic_launcher;

                ShareUtil.shareToWX(title, description, musicUrl, imageId);
            }
        });

        findViewById(R.id.btn_share_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = "这是标题";
                String description = "这是描述";
                String musicUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
                int imageId = R.mipmap.ic_launcher;

                ShareUtil.shareToCircle(title, description, musicUrl, imageId);
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        findViewById(R.id.btn_get_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
