package me.rokevin.android.lib.sharesdk.businees.wx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * Created by luokaiwen on 16/10/31.
 * <p/>
 * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=22ce97a5e39f843c314486e106067b4f26a44ca9&lang=zh_CN
 */
public class WXShare {

    private Context mContext;
    private IWXAPI mIwxapi;

    public WXShare(Context context, IWXAPI iwxapi) {
        mContext = context;
        mIwxapi = iwxapi;
    }

    public void shareToWX(String title, String description, String musicUrl, int imageId) {

        if (mIwxapi == null || mContext == null) {
            return;
        }

        WXMusicObject music = new WXMusicObject();
        music.musicUrl = musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;

        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), imageId);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mIwxapi.sendReq(req);
    }

    public void shareToWXWebPage(String title, String description, String url) {

        if (mIwxapi == null || mContext == null) {
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mIwxapi.sendReq(req);
    }

    public void shareToCircle(String title, String description, String musicUrl, int imageId) {

        if (mIwxapi == null || mContext == null) {
            return;
        }

        WXMusicObject music = new WXMusicObject();
        music.musicUrl = musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;

        // 不添加此项不能分享
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), imageId);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mIwxapi.sendReq(req);
    }

    public void shareToCircleWebPage(String title, String description, String url, int resId) {

        if (mIwxapi == null || mContext == null) {
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), resId);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mIwxapi.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
