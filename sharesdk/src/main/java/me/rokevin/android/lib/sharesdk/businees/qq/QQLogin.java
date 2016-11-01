package me.rokevin.android.lib.sharesdk.businees.qq;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import me.rokevin.android.lib.sharesdk.businees.sina.QQTokenKeeper;
import me.rokevin.android.lib.sharesdk.util.LogUtil;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class QQLogin {

    private static final String TAG = QQLogin.class.getSimpleName();
    private Context mContext;
    private Tencent mTencent;
    private IUiListener mUserInfoListenr = new IUiListener() {
        @Override
        public void onComplete(Object o) {

            LogUtil.e(TAG, "获取QQ信息成功");
            JSONObject jsonObject = ((JSONObject) o);
            String s = jsonObject.toString();
            LogUtil.e(TAG, "qqUserInfo:" + s);

            QQUserInfo qqUserInfo = null;

            try {

                String nickname = jsonObject.getString("nickname");
                String gender = jsonObject.getString("gender");
                String province = jsonObject.getString("province");
                String city = jsonObject.getString("city");
                String figureurl_qq_1 = jsonObject.getString("figureurl_qq_1");

                qqUserInfo = new QQUserInfo();
                qqUserInfo.setNickname(nickname);
                qqUserInfo.setGender(gender);
                qqUserInfo.setProvince(province);
                qqUserInfo.setCity(city);
                qqUserInfo.setFigureurl_qq_1(figureurl_qq_1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mUserInfoQQListener != null) {
                mUserInfoQQListener.userInfo(qqUserInfo);
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    private IUiListener mLoginListenr = new IUiListener() {
        @Override
        public void onComplete(Object o) {

            LogUtil.e(TAG, "QQ登录成功");

            JSONObject jsonObject = ((JSONObject) o);
            String s = jsonObject.toString();

            try {

                String openId = jsonObject.getString("openid");
                String accessToken = jsonObject.getString("access_token");
                long expiresIn = jsonObject.getLong("expires_in");

                LogUtil.e(TAG, "login onComplete openId:" + openId);
                LogUtil.e(TAG, "login onComplete accessToken:" + accessToken);

                mTencent.setOpenId(openId);
                mTencent.setAccessToken(accessToken, String.valueOf(expiresIn));

                me.rokevin.android.lib.sharesdk.businees.qq.QQToken qqToken = new me.rokevin.android.lib.sharesdk.businees.qq.QQToken();
                qqToken.setOpenid(openId);
                qqToken.setAccess_token(accessToken);
                qqToken.setExpires_in(expiresIn);

                QQTokenKeeper.writeAccessToken(mContext, qqToken);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    public IUiListener getUserInfoListenr() {
        return mUserInfoListenr;
    }

    public IUiListener getLoginListenr() {
        return mLoginListenr;
    }

    public QQLogin(Context context, Tencent tencent) {

        mContext = context;
        mTencent = tencent;
    }

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


    public boolean ready(Context context) {
        if (mTencent == null) {
            return false;
        }
        boolean ready = mTencent.isSessionValid()
                && mTencent.getQQToken().getOpenId() != null;
        if (!ready) {
            Toast.makeText(context, "loginQQ and get openId first, please!",
                    Toast.LENGTH_SHORT).show();
        }
        return ready;
    }

    private void onClickIsSupportSSOLogin(Activity activity) {

        if (mTencent.isSupportSSOLogin(activity)) {
            Toast.makeText(mContext, "支持SSO登陆", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "不支持SSO登陆", Toast.LENGTH_SHORT).show();
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

    public void login(Activity activity, String scope, IUiListener iUiListener) {

        if (mTencent == null || activity == null) {
            return;
        }

        mTencent.login(activity, scope, mLoginListenr);
    }

    public void logout() {

        if (mTencent == null || mContext == null) {
            return;
        }

        mTencent.logout(mContext);
    }

    public boolean isLogin() {

        if (mTencent == null || mContext == null) {
            return false;
        }

        return mTencent.isSessionValid();
    }

    public void getUserInfo(UserInfoQQListener userInfoQQListener) {

        mUserInfoQQListener = userInfoQQListener;

        if (mTencent == null || mContext == null) {
            return;
        }

        if (!mTencent.isSessionValid()) {
            LogUtil.e(TAG, "QQ 没有授权登录");
            return;
        }

        QQToken qqToken = mTencent.getQQToken();

        LogUtil.e(TAG, "openId:" + qqToken.getOpenId());
        LogUtil.e(TAG, "accessToken:" + qqToken.getAccessToken());
        LogUtil.e(TAG, "ExpireTimeInSecond:" + qqToken.getExpireTimeInSecond());

        UserInfo info = new UserInfo(mContext, qqToken);
        info.getUserInfo(mUserInfoListenr);
    }

    private UserInfoQQListener mUserInfoQQListener;

    public void setUserInfoQQListener(UserInfoQQListener userInfoQQListener) {
        mUserInfoQQListener = userInfoQQListener;
    }

    public interface UserInfoQQListener {

        void userInfo(QQUserInfo qqUserInfo);
    }
}
