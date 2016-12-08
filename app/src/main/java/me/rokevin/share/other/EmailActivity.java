package me.rokevin.share.other;

import android.os.Bundle;
import android.view.View;

import me.rokevin.android.lib.sharesdk.util.ShareUtil;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;

public class EmailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_email);

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = "订单更多，还有补贴，快来叮当到！邀请码：XXXX，下载地址：";
                String content = "订单更多，还有补贴，快来叮当到！邀请码：XXXX，下载地址：";
                String url = "http://www.baidu.com";

                ShareUtil.shareToMail(EmailActivity.this, subject, content, url);
            }
        });
    }
}
