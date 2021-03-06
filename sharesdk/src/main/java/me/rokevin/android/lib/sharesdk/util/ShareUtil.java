package me.rokevin.android.lib.sharesdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

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
import me.rokevin.android.lib.sharesdk.businees.qq.QQAuth;
import me.rokevin.android.lib.sharesdk.businees.qq.QQShare;
import me.rokevin.android.lib.sharesdk.businees.qq.QQTokenKeeper;
import me.rokevin.android.lib.sharesdk.businees.qq.model.QQAccessToken;
import me.rokevin.android.lib.sharesdk.businees.sina.SinaLogin;
import me.rokevin.android.lib.sharesdk.businees.sina.SinaShare;
import me.rokevin.android.lib.sharesdk.businees.wx.WXLogin;
import me.rokevin.android.lib.sharesdk.businees.wx.WXShare;
import me.rokevin.android.lib.sharesdk.businees.wx.WXTokenKeeper;
import me.rokevin.android.lib.sharesdk.exception.AppKeyException;
import me.rokevin.android.lib.sharesdk.listener.IShareQQ;

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
    private static QQAuth mQQAuth;

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

        QQAccessToken qqAccessToken = QQTokenKeeper.readAccessToken(mContext);

        if (qqAccessToken != null) {

            mTencent.setOpenId(qqAccessToken.getOpenid());
            mTencent.setAccessToken(qqAccessToken.getAccess_token(), String.valueOf(qqAccessToken.getExpires_in()));
        }

        mQQShare = new QQShare(mContext, mTencent);
        mQQAuth = new QQAuth(mContext, mTencent);
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
    public static void shareToWXWebPage(String title, String description, String musicUrl, int imageId) {

        mWXShare.shareToWXWebPage(title, description, musicUrl, imageId);
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

    /**
     * 判断微信是否登录
     *
     * @return
     */
    public static boolean isLoginWX() {

        return mWXLogin.isLogin();
    }

    /**
     * 判断微信是否安装
     *
     * @return
     */
    public static boolean isWXInstall() {

        return mWXLogin.isInstall();
    }

    /**
     * 微信登录
     */
    public static void getWXCode(String state) {

        mWXLogin.getCode(state);
    }

    /**
     * 获取Token
     *
     * @param code 获取token的code
     */
    public static void getWXToken(String appId, String secret, String code, WXLogin.WXGetTokenListener listener) {

        mWXLogin.getAccessToken(appId, secret, code, listener);
    }

    /**
     * 刷新Token
     *
     * @param appId
     * @param refreshToken
     * @param listener
     */
    public static void refreshWXToken(String appId, String refreshToken, WXLogin.WXGetTokenListener listener) {

        mWXLogin.refreshToken(appId, refreshToken, listener);
    }

    /**
     * 微信退出登录
     */
    public static void logoutWX() {

        WXTokenKeeper.clear(mContext);
    }

    /**
     * 获取用户信息
     */
    public static void getWXUserInfo(String token, String openId, WXLogin.WXGetUserInfoListener listener) {

        mWXLogin.getUserInfo(token, openId, listener);
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
    public static void shareToQQ(Activity activity, String title, String summary, String audioUrl, String imageUrl, String targetUrl, String appName, String appSource, IShareQQ iShareQQ) {

        mQQShare.shareToQQ(activity, title, summary, audioUrl, imageUrl, targetUrl, appName, appSource, iShareQQ);
    }

    /**
     * 分享到QQ空间 http://wiki.open.qq.com/index.php?title=Android_API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E&=45038#1.14_.E5.88.86.E4.BA.AB.E5.88.B0QQ.E7.A9.BA.E9.97.B4.EF.BC.88.E6.97.A0.E9.9C.80QQ.E7.99.BB.E5.BD.95.EF.BC.89
     *
     * @param activity
     * @param title
     * @param url
     */
    public static void shareToQQZoneTextImage(Activity activity, String title, String summary, String url, IShareQQ iShareQQ) {

        mQQShare.shareToQQZone(activity, title, summary, url, iShareQQ);
    }

    /**
     * 判断QQ是否登录
     *
     * @return
     */
    public static boolean isLoginQQ() {

        return mQQAuth.isLogin();
    }

    /**
     * QQ登录
     *
     * @param activity
     * @param scope
     * @param listener
     */
    public static void loginQQ(Activity activity, String scope, QQAuth.QQAuthListener listener) {

        mQQAuth.auth(activity, scope, listener);
    }

    /**
     * QQ退出登录
     */
    public static void logoutQQ() {

        mQQAuth.logout();
        QQTokenKeeper.clear(mContext);
    }

    /**
     * 获取用户信息
     */
    public static void getUserInfoQQ(QQAuth.QQUserInfoListener listener) {

        mQQAuth.getUserInfo(listener);
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

                Toast.makeText(mContext, R.string.share_qqzone_succ, Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(mContext, uiError.errorMessage + ":" + uiError.errorDetail, Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT);
            }
        });

        mSinaLogin.onActivityResultData(requestCode, resultCode, data);
    }

    //================================QQ===============================//

    //================================Sina=============================//

    public static void shareToSina(Activity activity, String title, String description, String musicUrl, int duration, String webUrl, String defaultText, int imageId) {

        mSinaShare.sendSingleMessage(activity, title, description, musicUrl, duration, webUrl, defaultText, imageId);
    }

    public static void loginSina(Activity activity, SinaLogin.SinaAuthListener listener) {

        mSinaLogin.login(activity, listener);
    }

    public static void logoutSina() {

        mSinaLogin.logout();
    }

    public static void getUserInfoSina(SinaLogin.SinaUserInfoListener listener) {

        mSinaLogin.getUserInfo(listener);
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
