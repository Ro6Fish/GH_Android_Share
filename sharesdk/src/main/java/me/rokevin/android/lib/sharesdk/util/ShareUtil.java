package me.rokevin.android.lib.sharesdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.share.IWeiboHandler;
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
import me.rokevin.android.lib.sharesdk.businees.qq.QQTokenKeeper;
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

        mSinaShare = new SinaShare(mContext, mWeiboShareAPI);
        mSinaLogin = new SinaLogin(mContext, mWeiboShareAPI, SINA_APP_KEY);
    }

    //===============================微信===============================//

    /**
     * 分享音乐到好友
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
     * 分享网页到好友
     *
     * @param title
     * @param description
     * @param musicUrl
     */
    public static void shareToWXWebPage(String title, String description, String musicUrl) {

        mWXShare.shareToWXWebPage(title, description, musicUrl);
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

    /**
     * 分享网页到好友
     *
     * @param title
     * @param description
     * @param musicUrl
     */
    public static void shareToCircleWebPage(String title, String description, String musicUrl, int resId) {

        mWXShare.shareToCircleWebPage(title, description, musicUrl, resId);
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

        mSinaLogin.onActivityResultData(requestCode, resultCode, data);
    }

    //================================QQ===============================//

    //================================Sina=============================//

    public static void shareToSina(Activity activity, String title, String description, String musicUrl, int duration, String webUrl, String defaultText, int imageId) {

        mSinaShare.sendSingleMessage(activity, title, description, musicUrl, duration, webUrl, defaultText, imageId);
    }

    public static void loginSina(Activity activity) {

        mSinaLogin.login(activity);
    }

    public static void logoutSina() {

        mSinaLogin.logout();
    }

    public static void getUserInfoSina() {

        mSinaLogin.getUserInfo();
    }

    public static void onNewIntent(Intent intent, IWeiboHandler.Response response) {

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, response);
    }

    public static void refreshToken() {

        mSinaLogin.refreshTokenRequest(SINA_APP_KEY);
    }

    //================================Sina=============================//

    /**
     * 发送邮件
     *
     * @param context
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param emailUrl 邮件地址
     */
    public static void shareToMail(Activity context, String subject, String content, String emailUrl) {

        Intent email = new Intent(android.content.Intent.ACTION_SEND);

        email.setType("plain/text");

        String emailBody = content + emailUrl;

        //邮件主题
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        //邮件内容
        email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);

        context.startActivityForResult(Intent.createChooser(email, "请选择邮件发送内容"), 1001);
    }

    /**
     * 发短信
     *
     * @param context
     * @param content 分享内容
     * @param webUrl  分享地址
     */
    public static void shareToSMS(Activity context, String content, String webUrl) {
        String smsBody = content + webUrl;
//        Uri smsToUri = Uri.parse("smsto:");
//        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
//        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
//        //短信内容
//        sendIntent.putExtra("sms_body", smsBody);
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        context.startActivityForResult(sendIntent, 1002);

        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        context.startActivityForResult(intent, 1002);
    }
}
