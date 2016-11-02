package me.rokevin.android.lib.sharesdk.businees.sina;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class SinaShare {

    private static final String TAG = SinaShare.class.getSimpleName();
    private Context mContext;
    private IWeiboShareAPI mIWeiboShareAPI;

    public SinaShare(Context context, IWeiboShareAPI iWeiboShareAPI) {
        mContext = context;
        mIWeiboShareAPI = iWeiboShareAPI;
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
    public void sendSingleMessage(Activity activity, String title, String description, String musicUrl, int duration, String webUrl, String defaultText, int imageId) {

        if (mIWeiboShareAPI == null || mContext == null) {
            return;
        }
        if (!mIWeiboShareAPI.isWeiboAppSupportAPI()) {

            Toast.makeText(mContext, "您的手机不支持微博分享", Toast.LENGTH_SHORT).show();
            return;
        }

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
//        if (hasVoice) {
//            weiboMessage.mediaObject = getVoiceObj();
//        }

        weiboMessage.mediaObject = getMusicObj(title, description, musicUrl, duration, webUrl, defaultText, imageId);
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mIWeiboShareAPI.sendRequest(activity, request);
    }

    /**
     * 创建文本消息对象。
     *
     * @param text
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @param imageId
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(int imageId) {
        ImageObject imageObject = new ImageObject();
        //BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageId);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @param title
     * @param description
     * @param actionUrl
     * @param appSource
     * @param imageId
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(String title, String description, String actionUrl, String appSource, int imageId) {

        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = description;

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageId);
        // 设置 Bitmap 类型的图片到视频对象里 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = actionUrl;
        // Webpage 默认文案
        mediaObject.defaultText = appSource;
        return mediaObject;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @param title
     * @param description
     * @param musicUrl
     * @param duration
     * @param webUrl
     * @param defaultText
     * @param imageId
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj(String title, String description, String musicUrl, int duration, String webUrl, String defaultText, int imageId) {

        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = title;
        musicObject.description = description;

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageId);

        // 设置 Bitmap 类型的图片到视频对象里        设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        musicObject.setThumbImage(bitmap);
        // musicObject.actionUrl = "http://112.74.78.105:8080/HelloWorld/download/voice2.amr";
        // musicObject.actionUrl = "http://music.sina.com.cn/yueku/i/2850305.html";
        // http://music.163.com/#/outchain/2/437289014/m/use/html
        musicObject.actionUrl = musicUrl;
        musicObject.duration = duration;
        musicObject.dataUrl = webUrl;
        musicObject.dataHdUrl = webUrl;
        // Music 默认文案
        musicObject.defaultText = defaultText;
        return musicObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @param title
     * @param description
     * @param audioUrl
     * @param duration
     * @param webUrl
     * @param defaultText
     * @param imageId
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj(String title, String description, String audioUrl, int duration, String webUrl, String defaultText, int imageId) {

        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = title;
        videoObject.description = description;

        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageId);
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            System.out.println("kkkkkkk size" + os.toByteArray().length);
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
        videoObject.actionUrl = audioUrl;
        videoObject.duration = duration;
        videoObject.dataUrl = webUrl;
        videoObject.dataHdUrl = webUrl;
        // Vedio 默认文案
        videoObject.defaultText = defaultText;
        return videoObject;
    }

    /**
     * 创建多媒体（音频）消息对象。
     *
     * @param title
     * @param description
     * @param voiceUrl
     * @param duration
     * @param webUrl
     * @param defaultText
     * @param imageId
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj(String title, String description, String voiceUrl, int duration, String webUrl, String defaultText, int imageId) {

        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Utility.generateGUID();
        voiceObject.title = title;
        voiceObject.description = description;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageId);
        // 设置 Bitmap 类型的图片到视频对象里      设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        voiceObject.setThumbImage(bitmap);
        voiceObject.actionUrl = voiceUrl;
        voiceObject.duration = duration;
        voiceObject.dataUrl = webUrl;
        voiceObject.dataHdUrl = webUrl;
        voiceObject.defaultText = defaultText;
        return voiceObject;
    }
}
