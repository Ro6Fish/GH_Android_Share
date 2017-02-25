package me.rokevin.android.lib.sharesdk.mode;

import java.io.Serializable;

/**
 * Created by luokaiwen on 17/2/24.
 * <p>
 * 分享的信息
 */

public class ShareInfo implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String summary;

    /**
     * 音乐地址
     */
    private String musicUrl;

    /**
     * 访问网页
     */
    private String targetUrl;

    /**
     * 图片资源ID
     */
    private int imageId;

    /**
     * QQ 图片地址
     */
    private String imageUrl;

    /**
     * QQ APP 名称
     */
    private String appName;

    /**
     * QQ OPEN_ID
     */
    private String appSource;

    /**
     * 新浪 默认播放时长
     */
    private int duration;

    /**
     * 新浪 默认音乐文案
     */
    private String defaultText;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppSource() {
        return appSource;
    }

    public void setAppSource(String appSource) {
        this.appSource = appSource;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "appName='" + appName + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", imageId=" + imageId +
                ", imageUrl='" + imageUrl + '\'' +
                ", appSource='" + appSource + '\'' +
                ", duration=" + duration +
                ", defaultText='" + defaultText + '\'' +
                '}';
    }
}
