package me.rokevin.android.lib.sharesdk.businees.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;

import org.json.JSONException;
import org.json.JSONObject;

import me.rokevin.android.lib.sharesdk.businees.sina.api.LogoutAPI;
import me.rokevin.android.lib.sharesdk.businees.sina.api.UsersAPI;
import me.rokevin.android.lib.sharesdk.businees.sina.models.ErrorInfo;
import me.rokevin.android.lib.sharesdk.businees.sina.models.SinaUser;
import me.rokevin.android.lib.sharesdk.util.LogUtil;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class SinaLogin {

    private static final String TAG = SinaLogin.class.getSimpleName();
    private Context mContext;
    private IWeiboShareAPI mIWeiboShareAPI;
    private String mAppKey;

    /**
     * 授权认证所需要的信息
     */
    private AuthInfo mAuthInfo;

    /**
     * SSO 授权认证实例
     */
    private SsoHandler mSsoHandler;

    /**
     * 微博授权认证回调
     */
    private AuthListener mAuthListener = new AuthListener();

    /**
     * 登出操作对应的listener
     */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    /**
     * 登录 、 获取用户信息
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 用户信息接口
     */
    private UsersAPI mUsersAPI;

    public SinaLogin(Context context, IWeiboShareAPI iWeiboShareAPI, String appKey) {

        mContext = context;
        mIWeiboShareAPI = iWeiboShareAPI;
        mAppKey = appKey;

        // 创建授权认证信息
        mAuthInfo = new AuthInfo(mContext, appKey, Constants.REDIRECT_URL, Constants.SCOPE);

        // 获取本地存储的AccessToken如果没有则要登录下
        mAccessToken = SinaTokenKeeper.readAccessToken(mContext);
    }

    /**
     * 登录等接口回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResultData(int requestCode, int resultCode, Intent data) {

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void login(Activity activity, SinaAuthListener listener) {

        if (null == mSsoHandler && mAuthInfo != null) {
            mSsoHandler = new SsoHandler(activity, mAuthInfo);
        }

        if (mSsoHandler != null) {

            mSinaAuthListener = listener;

            mSsoHandler.authorize(mAuthListener);

        } else {
            LogUtil.e(TAG, "Please setWeiboAuthInfo(...) for first");
        }
    }

    public void logout() {

        /**
         * 注销按钮：该按钮未做任何封装，直接调用对应 API 接口
         */
        new LogoutAPI(mContext, mAppKey, SinaTokenKeeper.readAccessToken(mContext)).logout(mLogoutListener);
    }

    public void getUserInfo(SinaUserInfoListener listener) {

        if (mUsersAPI == null) {

            // 获取用户信息接口
            mUsersAPI = new UsersAPI(mContext, mAppKey, mAccessToken);
        }

        mSinaUserInfoListener = listener;

        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mUserInfoListener);
    }

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {

            if (values == null) {
                LogUtil.e(TAG, "bundle values is null");
                return;
            }

            mAccessToken = Oauth2AccessToken.parseAccessToken(values);

            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                LogUtil.e(TAG, "mAccessToken:" + mAccessToken.getToken());
                SinaTokenKeeper.writeAccessToken(mContext, mAccessToken);
            }

            if (mSinaAuthListener != null) {
                mSinaAuthListener.onAuth(mAccessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(mContext, "取消授权", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        SinaTokenKeeper.clear(mContext);
                    }

                    // tvToken.setText("token:");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }

    // 获取用户信息

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mUserInfoListener = new RequestListener() {

        @Override
        public void onComplete(String response) {

            if (!TextUtils.isEmpty(response)) {

                LogUtil.e(TAG, response);
                // 调用 SinaUser#parse 将JSON串解析成User对象
                SinaUser user = SinaUser.parse(response);

                if (mSinaUserInfoListener != null) {
                    mSinaUserInfoListener.onUserInfo(user);
                }

                LogUtil.e(TAG, "获取User信息成功，用户昵称：" + user.toString());
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(mContext, info.toString(), Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 刷新AccessToken重新获取accessToken
     *
     * @param appKey AppKey
     */
    public void refreshTokenRequest(String appKey) {

        Oauth2AccessToken token = SinaTokenKeeper.readAccessToken(mContext);
        RefreshTokenApi.create(mContext).refreshToken(appKey, token.getRefreshToken(), new RequestListener() {

            @Override
            public void onWeiboException(WeiboException exception) {
                Toast.makeText(mContext, "RefreshToken Result : " + exception.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete(String response) {
                Toast.makeText(mContext, "RefreshToken Result : " + response, Toast.LENGTH_LONG).show();
            }
        });
    }

    private SinaAuthListener mSinaAuthListener;

    private SinaUserInfoListener mSinaUserInfoListener;

    public interface SinaAuthListener {

        void onAuth(Oauth2AccessToken token);
    }

    public interface SinaUserInfoListener {

        void onUserInfo(SinaUser user);
    }
}
