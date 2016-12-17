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

                String title = "叮当到";
                String description = "叮当到喊你来抢红包啦！";
                String url = "http://www.baidu.com";
                int imageId = R.mipmap.ic_launcher;

                // ShareUtil.shareToWX(title, description, url, imageId);
                ShareUtil.shareToWXWebPage(title, description, url);
            }
        });

        findViewById(R.id.btn_share_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = "订单更多，还有补贴，快来叮当到！";
                String description = "叮当到喊你来抢红包啦！";
                String url = "http://www.baidu.com";
                int imageId = R.mipmap.ic_launcher;

                //ShareUtil.shareToCircle(title, description, musicUrl, imageId);
                ShareUtil.shareToCircleWebPage(title, description, url, imageId);
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
