package me.rokevin.share.sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;

import me.rokevin.android.lib.sharesdk.util.ShareUtil;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;
import me.rokevin.share.ShareConfig;
import me.rokevin.share.test.ShareData;
import me.rokevin.share.test.TestData;

public class SinaActivity extends BaseActivity implements IWeiboHandler.Response {

    private String TAG = SinaActivity.class.getSimpleName();

    private TextView tvToken;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sina);

        tvToken = (TextView) findViewById(R.id.tv_token);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareData shareData = TestData.getShareData();

                String title = shareData.getTitle();
                String description = shareData.getDescription();
                String musicUrl = shareData.getMusicUrl();
                int duration = shareData.getDuration();
                String webUrl = shareData.getWebUrl();
                String defaultText = shareData.getDefaultText();
                int imageId = shareData.getImageId();

                ShareUtil.shareToSina(SinaActivity.this, title, description, musicUrl, duration, webUrl, defaultText, imageId);
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.loginSina(SinaActivity.this);
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.logoutSina();
            }
        });

        findViewById(R.id.btn_refresh_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.refreshToken();
            }
        });

        /**
         * 获取用户信息
         */
        findViewById(R.id.btn_get_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShareUtil.getUserInfoSina();
            }
        });
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        ShareUtil.onNewIntent(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {

        if (baseResp != null) {

            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResp.errMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }
}
