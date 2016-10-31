package me.rokevin.share.qq;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;
import me.rokevin.share.util.LogUtil;

public class QQActivity extends BaseActivity {

    private String TAG = QQActivity.class.getSimpleName();

    private Button btnShare;
    private Button btnLogin;
    private Button btnLogout;
    private Button btnGetUserInfo;
    private TextView tvToken;
    private TextView tvInfo;

    private Tencent mTencent;
    private IUiListener mUIListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);

        mTencent = Tencent.createInstance(me.rokevin.share.qq.Constants.OPEN_ID, this.getApplicationContext());

        btnShare = (Button) findViewById(R.id.btn_share);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnGetUserInfo = (Button) findViewById(R.id.btn_get_user_info);
        tvToken = (TextView) findViewById(R.id.tv_token);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        /**
         *
         {
         "ret":0,
         "pay_token":"xxxxxxxxxxxxxxxx",
         "pf":"openmobile_android",
         "expires_in":"7776000",
         "openid":"xxxxxxxxxxxxxxxxxxx",
         "pfkey":"xxxxxxxxxxxxxxxxxxx",
         "msg":"sucess",
         "access_token":"xxxxxxxxxxxxxxxxxxxxx"
         }
         */
        mUIListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {

                // {"ret":0,"openid":"0CDC2A16C071142C59062FDEAE560B5B","access_token":"AD5131A5542646CF7FB5B8B1F4183552","pay_token":"A0FF38CFA36B99A2A6D5926259B17C84","expires_in":7776000,"pf":"desktop_m_qq-10000144-android-2002-","pfkey":"45c11c0035aa2568048cf7e30186bf87","msg":"","login_cost":415,"query_authority_cost":321,"authority_cost":-18214433}
                String s = ((JSONObject) o).toString();
                LogUtil.e(TAG, "s:" + s);

                JSONObject jsonObject = ((JSONObject) o);
                try {
                    String openId = jsonObject.getString("openid");
                    String accessToken = jsonObject.getString("access_token");

                    LogUtil.e(TAG, "onComplete openId:" + openId);
                    LogUtil.e(TAG, "onComplete accessToken:" + accessToken);

                    mTencent.setOpenId("26E82E07D51EEEB6D172B5B66AE6F71D");
                    mTencent.setAccessToken("2627995CD46AD8CC12EC6058C120DE37", "7776000");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvInfo.setText(s);
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };

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

                mTencent.login(QQActivity.this, "all", mUIListener);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTencent.logout(mContext);
            }
        });

        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // UserInfo info = new UserInfo(mContext, new QQToken("AD5131A5542646CF7FB5B8B1F4183552"));

                boolean isLogin = mTencent.isSessionValid();

                QQToken qqToken = mTencent.getQQToken();

                LogUtil.e(TAG, "isLogin:" + isLogin);
                LogUtil.e(TAG, "openId:" + qqToken.getOpenId());
                LogUtil.e(TAG, "accessToken:" + qqToken.getAccessToken());
                LogUtil.e(TAG, "ExpireTimeInSecond:" + qqToken.getExpireTimeInSecond());

                UserInfo info = new UserInfo(mContext, qqToken);
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {

                        String s = ((JSONObject) o).toString();
                        LogUtil.e(TAG, "sss:" + s);
                        tvInfo.setText(s);
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
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

        mTencent.shareToQQ(this, bundle, mUIListener);
    }

    public void shareToQzone() {

        //分享类型
        Bundle bundle = new Bundle();
        // bundle.putString(QzoneShare.SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填 params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "跳转 URL");//必填
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, new ArrayList<String>());
        mTencent.shareToQzone(QQActivity.this, bundle, mUIListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Tencent.onActivityResultData(requestCode, resultCode, data, mUIListener);
    }

    public boolean ready(Context context) {
        if (mTencent == null) {
            return false;
        }
        boolean ready = mTencent.isSessionValid()
                && mTencent.getQQToken().getOpenId() != null;
        if (!ready) {
            Toast.makeText(context, "login and get openId first, please!",
                    Toast.LENGTH_SHORT).show();
        }
        return ready;
    }

    private void onClickIsSupportSSOLogin() {
        if (mTencent.isSupportSSOLogin(QQActivity.this)) {
            Toast.makeText(QQActivity.this, "支持SSO登陆", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(QQActivity.this, "不支持SSO登陆", Toast.LENGTH_SHORT).show();
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        response.getString("nickname");
                        // mUserInfo.setVisibility(android.view.View.VISIBLE);
                        // mUserInfo.setText(response.getString("nickname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == 1) {
//                Bitmap bitmap = (Bitmap)msg.obj;
//                mUserLogo.setImageBitmap(bitmap);
//                mUserLogo.setVisibility(android.view.View.VISIBLE);
            }
        }

    };

    public void doComplete(final JSONObject response) {

        Message msg = new Message();
        msg.obj = response;
        msg.what = 0;
        mHandler.sendMessage(msg);
        new Thread() {

            @Override
            public void run() {
                JSONObject json = (JSONObject) response;
                if (json.has("figureurl")) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                    } catch (JSONException e) {

                    }
                    Message msg = new Message();
                    msg.obj = bitmap;
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }

        }.start();
    }
}
