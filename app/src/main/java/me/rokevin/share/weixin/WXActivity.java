package me.rokevin.share.weixin;

import android.os.Bundle;
import android.view.View;

import de.greenrobot.event.EventBus;
import me.rokevin.android.lib.sharesdk.businees.wx.WXLogin;
import me.rokevin.android.lib.sharesdk.businees.wx.WXTokenKeeper;
import me.rokevin.android.lib.sharesdk.businees.wx.model.WXAccessToken;
import me.rokevin.android.lib.sharesdk.businees.wx.model.WXUserInfo;
import me.rokevin.android.lib.sharesdk.util.LogUtil;
import me.rokevin.android.lib.sharesdk.util.ShareUtil;
import me.rokevin.eventbus.WXCodeEvent;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;
import me.rokevin.share.ShareConfig;

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
                ShareUtil.shareToWXWebPage(title, description, url, imageId);
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

        findViewById(R.id.btn_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WXAccessToken token = WXTokenKeeper.readAccessToken(mContext);

                String state = "123";
                ShareUtil.getWXCode(state);

            }
        });

        findViewById(R.id.btn_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    public void onEvent(WXCodeEvent event) {

        String appId = ShareConfig.WX_APP_ID;
        String secret = ShareConfig.WX_SECRET;

        String code = event.getCode();

        ShareUtil.getWXToken(appId, secret, code, new WXLogin.WXGetTokenListener() {
            @Override
            public void onToken(WXAccessToken token) {

                String accessToken = token.getAccessToken();
                String openid = token.getOpenid();

                LogUtil.e(TAG, "getUserInfo accessToken:" + accessToken);
                LogUtil.e(TAG, "getUserInfo openid:" + openid);

                ShareUtil.getWXUserInfo(accessToken, token.getOpenid(), new WXLogin.WXGetUserInfoListener() {

                    @Override
                    public void onUserInfo(WXUserInfo userInfo) {

                        LogUtil.e(TAG, "userInfo：" + userInfo);
                    }
                });
            }
        });
    }
}
