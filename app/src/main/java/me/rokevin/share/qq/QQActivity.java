package me.rokevin.share.qq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.rokevin.android.lib.sharesdk.businees.qq.QQLogin;
import me.rokevin.android.lib.sharesdk.businees.qq.QQUserInfo;
import me.rokevin.android.lib.sharesdk.util.LogUtil;
import me.rokevin.android.lib.sharesdk.util.ShareUtil;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;
import me.rokevin.share.ShareConfig;

public class QQActivity extends BaseActivity {

    private String TAG = QQActivity.class.getSimpleName();

    private Button btnShare;
    private Button btnLogin;
    private Button btnLogout;
    private Button btnGetUserInfo;
    private TextView tvToken;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);

        btnShare = (Button) findViewById(R.id.btn_share);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnGetUserInfo = (Button) findViewById(R.id.btn_get_user_info);
        tvToken = (TextView) findViewById(R.id.tv_token);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = "这是分享到QQ的标题";
                String summary = "这是分享到QQ的内容";
                String audioUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
                String imageUrl = "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg";
                String targetUrl = "http://www.5ijoke.com";
                String appName = "就是笑话";
                String appSource = "就是笑话" + ShareConfig.QQ_OPEN_ID;

                ShareUtil.shareToQQ(QQActivity.this, title, summary, audioUrl, imageUrl, targetUrl, appName, appSource);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // scope 请求范围 all全部
                ShareUtil.loginQQ(QQActivity.this, "all", null);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.logoutQQ();
            }
        });

        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ShareUtil.isLoginQQ()) {

                    ShareUtil.loginQQ(QQActivity.this, "get_user_info", null);

                } else {

                    ShareUtil.getUserInfoQQ(new QQLogin.UserInfoQQListener() {
                        @Override
                        public void userInfo(QQUserInfo qqUserInfo) {

                            LogUtil.e(TAG, "获取到的用户信息:" + qqUserInfo.toString());
                            tvInfo.setText(qqUserInfo.toString());
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ShareUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
