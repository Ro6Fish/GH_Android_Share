package me.rokevin.android.lib.sharesdk.businees.qq;

import android.app.Activity;
import android.content.Context;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import me.rokevin.android.lib.sharesdk.businees.qq.model.QQAccessToken;
import me.rokevin.android.lib.sharesdk.businees.qq.model.QQUserInfo;
import me.rokevin.android.lib.sharesdk.util.LogUtil;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class QQAuth {

    private static final String TAG = QQAuth.class.getSimpleName();
    private Context mContext;
    private Tencent mTencent;

    /**
     * 授权监听
     */
    private IUiListener mUIAuthListenr = new IUiListener() {

        @Override
        public void onComplete(Object o) {

            LogUtil.e(TAG, "QQ授权成功");

            JSONObject jsonObject = ((JSONObject) o);

            try {

                String openId = jsonObject.getString("openid");
                String accessToken = jsonObject.getString("access_token");
                long expiresIn = jsonObject.getLong("expires_in");

                LogUtil.e(TAG, "auth onComplete openId:" + openId);
                LogUtil.e(TAG, "auth onComplete accessToken:" + accessToken);

                mTencent.setOpenId(openId);
                mTencent.setAccessToken(accessToken, String.valueOf(expiresIn));

                QQAccessToken qqAccessToken = new QQAccessToken();
                qqAccessToken.setOpenid(openId);
                qqAccessToken.setAccess_token(accessToken);
                qqAccessToken.setExpires_in(expiresIn);

                QQTokenKeeper.writeAccessToken(mContext, qqAccessToken);

                if (mQQAuthListener != null) {
                    mQQAuthListener.onAuth(qqAccessToken);
                }

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

    /**
     * 获取用户信息监听
     */
    private IUiListener mUIUserInfoListenr = new IUiListener() {

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

                if (mQQUserInfoListener != null) {
                    mQQUserInfoListener.onUserInfo(qqUserInfo);
                }

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

    public QQAuth(Context context, Tencent tencent) {

        mContext = context;
        mTencent = tencent;
    }

    /**
     * 判断是否登录了
     *
     * @return true:登录 false:未登录
     */
    public boolean isLogin() {

        if (mTencent == null || mContext == null) {
            return false;
        }

        boolean isLogin = mTencent.isSessionValid() && mTencent.getQQToken().getOpenId() != null;

        return isLogin;
    }

    public void auth(Activity activity, String scope, QQAuthListener qqAuthListener) {

        if (mTencent == null || activity == null) {
            return;
        }

        mQQAuthListener = qqAuthListener;

        mTencent.login(activity, scope, mUIAuthListenr);
    }

    public void refreshToke() {

    }

    public void getUserInfo(QQUserInfoListener qqUserInfoListener) {

        if (!isLogin()) {
            LogUtil.e(TAG, "QQ 没有授权登录");
            return;
        }

        // 对外设置回调监听
        mQQUserInfoListener = qqUserInfoListener;

        QQToken qqToken = mTencent.getQQToken();

        LogUtil.e(TAG, "openId:" + qqToken.getOpenId());
        LogUtil.e(TAG, "accessToken:" + qqToken.getAccessToken());
        LogUtil.e(TAG, "ExpireTimeInSecond:" + qqToken.getExpireTimeInSecond());

        UserInfo info = new UserInfo(mContext, qqToken);
        info.getUserInfo(mUIUserInfoListenr);
    }

    public void logout() {

        if (isLogin()) {
            mTencent.logout(mContext);
        }
    }

    private QQAuthListener mQQAuthListener;

    private QQUserInfoListener mQQUserInfoListener;

    public interface QQAuthListener {

        void onAuth(QQAccessToken token);
    }

    public interface QQUserInfoListener {

        void onUserInfo(QQUserInfo user);
    }
}
