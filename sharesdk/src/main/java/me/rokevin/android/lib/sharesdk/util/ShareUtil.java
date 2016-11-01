package me.rokevin.android.lib.sharesdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import me.rokevin.android.lib.sharesdk.R;
import me.rokevin.android.lib.sharesdk.businees.qq.QQLogin;
import me.rokevin.android.lib.sharesdk.businees.qq.QQShare;
import me.rokevin.android.lib.sharesdk.businees.qq.QQToken;
import me.rokevin.android.lib.sharesdk.businees.sina.QQTokenKeeper;
import me.rokevin.android.lib.sharesdk.businees.sina.SinaLogin;
import me.rokevin.android.lib.sharesdk.businees.sina.SinaShare;
import me.rokevin.android.lib.sharesdk.businees.wx.WXLogin;
import me.rokevin.android.lib.sharesdk.businees.wx.WXShare;
import me.rokevin.android.lib.sharesdk.exception.AppKeyException;

/**
 * Created by luokaiwen on 16/10/31.
 * <p/>
 * 分享帮助类
 */
public class ShareUtil {

    private static final String TAG = ShareUtil.class.getSimpleName();

    // QQ OPEN_ID
    private static String QQ_OPEN_ID;

    // 新浪 APP_KEY
    public static String SINA_APP_KEY;

    // 微信APP_ID
    public static String WX_APP_ID;

    private static IWXAPI mApi;
    private static Tencent mTencent;
    private static IWeiboShareAPI mWeiboShareAPI;

    private static Context mContext;

    private static WXShare mWXShare;
    private static WXLogin mWXLogin;

    private static QQShare mQQShare;
    private static QQLogin mQQLogin;

    private static SinaShare mSinaShare;
    private static SinaLogin mSinaLogin;

    /**
     * 注册WX APP_ID
     *
     * @param context
     * @return
     */
    public static void registeWX(Context context, String appId) {

        if (context == null) {
            LogUtil.e(TAG, "context is null");
            return;
        }

        if (TextUtils.isEmpty(appId)) {
            try {
                throw new AppKeyException(context.getString(R.string.exception_appkey_null));
            } catch (AppKeyException e) {
                LogUtil.e(TAG, "wx not set AppId");
            }
        }

        mContext = context;

        WX_APP_ID = appId;

        mApi = WXAPIFactory.createWXAPI(context, appId, true);

        mApi.registerApp(appId);

        mWXShare = new WXShare(mContext, mApi);
        mWXLogin = new WXLogin(mContext, mApi);
    }

    /**
     * 注册QQ OPEN_ID
     *
     * @param context
     * @return
     */
    public static void registeQQ(Context context, String openId) {

        if (context == null) {
            LogUtil.e(TAG, "context is null");
            return;
        }

        mContext = context;

        QQ_OPEN_ID = openId;

        if (TextUtils.isEmpty(QQ_OPEN_ID)) {
            try {
                throw new AppKeyException(context.getString(R.string.exception_appkey_null));
            } catch (AppKeyException e) {
                LogUtil.e(TAG, "qq not set OpenId");
            }
        }

        mTencent = Tencent.createInstance(QQ_OPEN_ID, context);

        QQToken qqToken = QQTokenKeeper.readAccessToken(mContext);

        if (qqToken != null) {

            mTencent.setOpenId(qqToken.getOpenid());
            mTencent.setAccessToken(qqToken.getAccess_token(), String.valueOf(qqToken.getExpires_in()));
        }

        mQQShare = new QQShare(mContext, mTencent);
        mQQLogin = new QQLogin(mContext, mTencent);
    }

    /**
     * 注册新浪微博 APP_KEY
     *
     * @param context
     */
    public static void registeSina(Context context, String appKey) {

        if (context == null) {
            LogUtil.e(TAG, "context is null");
            return;
        }

        mContext = context;

        SINA_APP_KEY = appKey;

        if (TextUtils.isEmpty(SINA_APP_KEY)) {
            try {
                throw new AppKeyException(context.getString(R.string.exception_appkey_null));
            } catch (AppKeyException e) {
                LogUtil.e(TAG, "sina not set AppKey");
            }
        }

        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, SINA_APP_KEY);

        mWeiboShareAPI.registerApp(); // 将应用注册到微博客户端
    }

    //===============================微信===============================//

    /**
     * 分享到好友
     *
     * @param title
     * @param description
     * @param musicUrl
     * @param imageId
     */
    public static void shareToWX(String title, String description, String musicUrl, int imageId) {

        mWXShare.shareToWX(title, description, musicUrl, imageId);
    }

    /**
     * 分享到朋友圈
     *
     * @param title
     * @param description
     * @param musicUrl
     * @param imageId
     */
    public static void shareToCircle(String title, String description, String musicUrl, int imageId) {

        mWXShare.shareToCircle(title, description, musicUrl, imageId);
    }

    //===============================微信===============================//

    //================================QQ===============================//

    /**
     * 分享到QQ
     *
     * @param activity
     * @param title
     * @param summary
     * @param audioUrl
     * @param imageUrl
     * @param targetUrl
     * @param appName
     * @param appSource
     */
    public static void shareToQQ(Activity activity, String title, String summary, String audioUrl, String imageUrl, String targetUrl, String appName, String appSource) {
        mQQShare.shareToQQ(activity, title, summary, audioUrl, imageUrl, targetUrl, appName, appSource);
    }

    /**
     * QQ登录
     *
     * @param activity
     * @param scope
     * @param iUiListener
     */
    public static void loginQQ(Activity activity, String scope, IUiListener iUiListener) {

        mQQLogin.login(activity, scope, iUiListener);
    }

    /**
     * QQ退出登录
     */
    public static void logoutQQ() {

        mQQLogin.logout();
        QQTokenKeeper.clear(mContext);
    }

    /**
     * 判断QQ是否登录
     *
     * @return
     */
    public static boolean isLoginQQ() {

        boolean isLogin = false;

        if (mQQLogin.isLogin()) {
            isLogin = true;
        }

        return isLogin;
    }

    /**
     * 获取用户信息
     */
    public static void getUserInfoQQ(QQLogin.UserInfoQQListener userInfoQQListener) {

        mQQLogin.getUserInfo(userInfoQQListener);
    }

    /**
     * 监听回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {

        Tencent.onActivityResultData(requestCode, resultCode, data, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    //================================QQ===============================//

    //================================Sina=============================//


    //================================Sina=============================//
}
