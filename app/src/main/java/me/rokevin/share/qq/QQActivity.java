package me.rokevin.share.qq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tauth.UiError;

import me.rokevin.android.lib.sharesdk.businees.qq.QQAuth;
import me.rokevin.android.lib.sharesdk.businees.qq.model.QQAccessToken;
import me.rokevin.android.lib.sharesdk.businees.qq.model.QQUserInfo;
import me.rokevin.android.lib.sharesdk.listener.IShareQQ;
import me.rokevin.android.lib.sharesdk.util.LogUtil;
import me.rokevin.android.lib.sharesdk.util.ShareUtil;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;
import me.rokevin.share.ShareConfig;

public class QQActivity extends BaseActivity {

    private String TAG = QQActivity.class.getSimpleName();

    private Button btnShare;
    private Button btnShareQQZone;
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
        btnShareQQZone = (Button) findViewById(R.id.btn_share_qqzone);
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

                ShareUtil.shareToQQ(QQActivity.this, title, summary, audioUrl, imageUrl, targetUrl, appName, appSource, new IShareQQ() {
                    @Override
                    public void onComplete(Object o) {

                        LogUtil.e(TAG, o.toString());
                        Toast.makeText(mContext, R.string.share_succ, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(UiError uiError) {

                        LogUtil.e(TAG, uiError.errorCode + " " + uiError.errorMessage);
                        Toast.makeText(mContext, R.string.share_fail, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                        Toast.makeText(mContext, R.string.share_cancel, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnShareQQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = "这是分享到QQ空间的标题";
                String summary = "这是分享到QQ空间的内容";
                String audioUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
                String imageUrl = "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg";
                String targetUrl = "http://www.5ijoke.com";
                String appName = "就是笑话";
                String appSource = "就是笑话" + ShareConfig.QQ_OPEN_ID;

                ShareUtil.shareToQQZoneTextImage(QQActivity.this, title, audioUrl, new IShareQQ() {
                    @Override
                    public void onComplete(Object o) {

                        LogUtil.e(TAG, o.toString());
                        Toast.makeText(mContext, R.string.share_succ, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(UiError uiError) {

                        LogUtil.e(TAG, uiError.errorCode + " " + uiError.errorMessage);
                        Toast.makeText(mContext, R.string.share_fail, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                        Toast.makeText(mContext, R.string.share_cancel, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // scope 请求范围 all全部
                ShareUtil.loginQQ(QQActivity.this, "all", new QQAuth.QQAuthListener() {

                    @Override
                    public void onAuth(QQAccessToken token) {

                        LogUtil.e(TAG, "获取到的授权信息:" + token.toString());
                    }
                });
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

                    ShareUtil.getUserInfoQQ(new QQAuth.QQUserInfoListener() {

                        @Override
                        public void onUserInfo(QQUserInfo user) {

                            LogUtil.e(TAG, "获取到的用户信息:" + user.toString());
                            tvInfo.setText(user.toString());
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
