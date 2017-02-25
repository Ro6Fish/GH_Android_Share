package me.rokevin.android.lib.sharesdk.businees.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QzoneShare;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

import me.rokevin.android.lib.sharesdk.listener.IShareQQ;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class QQShare {

    private Context mContext;
    private Tencent mTencent;

    public QQShare(Context context, Tencent tencent) {

        mContext = context;
        mTencent = tencent;
    }


    public void shareToQQ(Activity activity, String title, String summary, String audioUrl, String imageUrl, String targetUrl, String appName, String appSource, IShareQQ iShareQQ) {

        if (mTencent == null || activity == null) {
            return;
        }

        Bundle bundle = new Bundle();

        //这条分享消息被好友点击后的跳转URL。
        bundle.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_AUDIO);

        bundle.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);

        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最你少必须有一个是有值的。
        bundle.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, title);

        bundle.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);

        //分享的图片URL
        bundle.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);

        //分享的消息摘要，最长50个字
        bundle.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, summary);

        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_APP_NAME, appName);

        //标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(SocialConstants.PARAM_APP_SOURCE, appSource);

        mTencent.shareToQQ(activity, bundle, iShareQQ);
    }

    /**
     * 分享到QQ空间
     *
     * @param activity 被分享的页面
     * @param title    标题
     * @param summary  内容
     * @param url      链接
     */
    public void shareToQQZone(Activity activity, String title, String summary, String url, IShareQQ iShareQQ) {

        if (mTencent == null || activity == null) {
            return;
        }

        ArrayList<String> imageUrls = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title); // 必填
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, title); // 选填
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);// 必填
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls); // 选填
        mTencent.shareToQzone(activity, bundle, iShareQQ);
    }

    /**
     * 分享到QQ空间
     *
     * @param activity 被分享的页面
     * @param title    标题
     * @param url      链接
     */
    public void shareToQQZone(Activity activity, String title, String url, IShareQQ iShareQQ) {

        shareToQQZone(activity, title, "", url, iShareQQ);
    }
}
