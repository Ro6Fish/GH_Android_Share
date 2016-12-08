package me.rokevin.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.rokevin.share.douban.DouBanActivity;
import me.rokevin.share.other.EmailActivity;
import me.rokevin.share.qq.QQActivity;
import me.rokevin.share.sina.SinaActivity;
import me.rokevin.android.lib.sharesdk.util.LogUtil;
import me.rokevin.share.other.SMSActivity;
import me.rokevin.share.weixin.WXActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.e(TAG, "qqq");
                startActivity(new Intent(mContext, QQActivity.class));
            }
        });

        findViewById(R.id.btn_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, WXActivity.class));
            }
        });

        findViewById(R.id.btn_sina).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SinaActivity.class));
            }
        });

        findViewById(R.id.btn_douban).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DouBanActivity.class));
            }
        });

        findViewById(R.id.btn_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SMSActivity.class));
            }
        });

        findViewById(R.id.btn_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, EmailActivity.class));
            }
        });
    }
}
