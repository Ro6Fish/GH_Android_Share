package me.rokevin.share.qq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;

public class QQActivity extends BaseActivity {

    private String TAG = QQActivity.class.getSimpleName();

    private Button btnShare;
    private Button btnLogin;
    private TextView tvInfo;

    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);

        mTencent = Tencent.createInstance("1105515096", this.getApplicationContext());

        btnShare = (Button) findViewById(R.id.btn_share);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareToQQ();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String openId = mTencent.getOpenId();
                String accessToken = mTencent.getAccessToken();
                Log.e(TAG, "openId:" + openId);
                Log.e(TAG, "accessToken:" + accessToken);

                mTencent.login(QQActivity.this, "get_user_info", mBaseUiListener);
            }
        });

        mBaseUiListener = new BaseUiListener();
    }

    public void shareToQQ() {

        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.5ijoke.com");
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最你少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "给你来个笑话");
        bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, "http://112.74.78.105:8080/HelloWorld/download/voice2.amr");

        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "苏清上山打老虎");
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "就是笑话");
        //标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(SocialConstants.PARAM_APP_SOURCE, "就是笑话" + "1105515096");

        mTencent.shareToQQ(this, bundle, mBaseUiListener);
    }

    public void shareToQzone() {

        //分享类型
        Bundle bundle = new Bundle();
        // bundle.putString(QzoneShare.SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填 params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "跳转 URL");//必填
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, new ArrayList<String>());
        mTencent.shareToQzone(QQActivity.this, bundle, mBaseUiListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                mTencent.handleLoginData(data, mBaseUiListener);
            }

            if (mTencent != null) {
                mTencent.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private BaseUiListener mBaseUiListener;

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.e(TAG, "onComplete:" + o.toString());
        }

        @Override
        public void onError(UiError uiError) {
            Log.e(TAG, "onError:" + uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            Log.e(TAG, "onCancel:");
        }
    }
}
