package me.rokevin.share.sina;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import me.rokevin.android.lib.sharesdk.businees.sina.AccessTokenKeeper;
import me.rokevin.android.lib.sharesdk.businees.sina.Constants;
import me.rokevin.android.lib.sharesdk.businees.sina.api.LogoutAPI;
import me.rokevin.android.lib.sharesdk.businees.sina.api.UsersAPI;
import me.rokevin.android.lib.sharesdk.businees.sina.models.ErrorInfo;
import me.rokevin.android.lib.sharesdk.businees.sina.models.User;
import me.rokevin.share.BaseActivity;
import me.rokevin.share.R;
import me.rokevin.share.ShareConfig;

public class SinaActivity extends BaseActivity implements IWeiboHandler.Response {

    private String TAG = SinaActivity.class.getSimpleName();

    private TextView tvToken;
    private TextView tvInfo;

    private IWeiboShareAPI mWeiboShareAPI;

    // 登录
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

    // 登录 、 获取用户信息
    private Oauth2AccessToken mAccessToken;

    /**
     * 用户信息接口
     */
    private UsersAPI mUsersAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sina);

        tvToken = (TextView) findViewById(R.id.tv_token);
        tvInfo = (TextView) findViewById(R.id.tv_info);

        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ShareConfig.SINA_APP_KEY);
        mWeiboShareAPI.registerApp(); // 将应用注册到微博客户端

        // 创建授权认证信息
        mAuthInfo = new AuthInfo(this, ShareConfig.SINA_APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        // 获取本地存储的AccessToken如果没有则要登录下
        mAccessToken = AccessTokenKeeper.readAccessToken(SinaActivity.this);

        // 获取用户信息接口
        mUsersAPI = new UsersAPI(this, ShareConfig.SINA_APP_KEY, mAccessToken);

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                    sendSingleMessage();
                } else {
                    Toast.makeText(mContext, "您的手机不支持微博分享", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null == mSsoHandler && mAuthInfo != null) {
                    mSsoHandler = new SsoHandler((Activity) mContext, mAuthInfo);
                }

                if (mSsoHandler != null) {
                    mSsoHandler.authorize(mAuthListener);
                } else {
                    LogUtil.e(TAG, "Please setWeiboAuthInfo(...) for first");
                }
            }
        });

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * 注销按钮：该按钮未做任何封装，直接调用对应 API 接口
                 */
                new LogoutAPI(SinaActivity.this, ShareConfig.SINA_APP_KEY, AccessTokenKeeper.readAccessToken(SinaActivity.this)).logout(mLogoutListener);
            }
        });

        /**
         * 获取用户信息
         */
        findViewById(R.id.btn_get_user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long uid = Long.parseLong(mAccessToken.getUid());
                mUsersAPI.show(uid, mListener);
            }
        });
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
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
                    Toast.makeText(this,
                            getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResp.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public void share() {


    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * <p/>
     * //     * @param hasText    分享的内容是否有文本
     * //     * @param hasImage   分享的内容是否有图片
     * //     * @param hasWebpage 分享的内容是否有网页
     * //     * @param hasMusic   分享的内容是否有音乐
     * //     * @param hasVideo   分享的内容是否有视频
     */
    //private void sendSingleMessage(boolean hasText, boolean hasImage, boolean hasWebpage, boolean hasMusic, boolean hasVideo) {
    private void sendSingleMessage() {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
//        if (hasText) {
//            weiboMessage.mediaObject = getTextObj();
//        }
//        if (hasImage) {
//            weiboMessage.mediaObject = getImageObj();
//        }
//        if (hasWebpage) {
//            weiboMessage.mediaObject = getWebpageObj();
//        }
//        if (hasMusic) {
//            weiboMessage.mediaObject = getMusicObj();
//        }
//        if (hasVideo) {
//            weiboMessage.mediaObject = getVideoObj();
//        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/
        weiboMessage.mediaObject = getMusicObj();
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(SinaActivity.this, request);
    }

    /**
     * 获取分享的文本模板。
     *
     * @return 分享的文本模板
     */
    private String getSharedText() {
        return "我正在听 XXX 音乐（分享自 joke）";
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        //BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "来个笑话";
        mediaObject.description = "上山打老虎";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj() {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = "给你来个笑话";
        musicObject.description = "上山打老虎";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


        // 设置 Bitmap 类型的图片到视频对象里        设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        musicObject.setThumbImage(bitmap);
        // musicObject.actionUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
        // musicObject.actionUrl = "http://music.sina.com.cn/yueku/i/2850305.html";
        musicObject.actionUrl = "http://music.163.com/#/outchain/2/437289014/m/use/html";
        musicObject.dataUrl = "www.weibo.com";
        musicObject.dataHdUrl = "www.weibo.com";
        musicObject.duration = 10;
        musicObject.defaultText = "Music 默认文案";
        return musicObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj() {
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = "来一个笑话";
        videoObject.description = "上山打老虎";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。


        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            System.out.println("kkkkkkk    size  " + os.toByteArray().length);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Weibo.BaseMediaObject", "put thumb failed");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        videoObject.setThumbImage(bitmap);
        videoObject.actionUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
        videoObject.dataUrl = "www.weibo.com";
        videoObject.dataHdUrl = "www.weibo.com";
        videoObject.duration = 10;
        videoObject.defaultText = "Vedio 默认文案";
        return videoObject;
    }

    /**
     * 创建多媒体（音频）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj() {
        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Utility.generateGUID();
        voiceObject.title = "来一个笑话";
        voiceObject.description = "上山打老虎";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里      设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        voiceObject.setThumbImage(bitmap);
        voiceObject.actionUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
        voiceObject.dataUrl = "www.weibo.com";
        voiceObject.dataHdUrl = "www.weibo.com";
        voiceObject.duration = 10;
        voiceObject.defaultText = "Voice 默认文案";
        return voiceObject;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
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
                tvToken.setText(mAccessToken.toString());
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), mAccessToken);

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
                        AccessTokenKeeper.clear(mContext);
                    }

                    tvToken.setText("token:");
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
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i(TAG, response);
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    Toast.makeText(mContext, "获取User信息成功，用户昵称：" + user.screen_name, Toast.LENGTH_LONG).show();
                    tvInfo.setText("用户信息:" + user.toString());
                } else {
                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                }
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
     */
    private void refreshTokenRequest() {
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(mContext);
        RefreshTokenApi.create(getApplicationContext()).refreshToken(
                ShareConfig.SINA_APP_KEY, token.getRefreshToken(), new RequestListener() {

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        Toast.makeText(mContext, "RefreshToken Result : " + arg0.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete(String arg0) {
                        Toast.makeText(mContext, "RefreshToken Result : " + arg0, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
