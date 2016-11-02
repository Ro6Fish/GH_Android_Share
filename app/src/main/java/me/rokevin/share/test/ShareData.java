package me.rokevin.share.test;

import me.rokevin.share.R;

/**
 * Created by luokaiwen on 16/11/2.
 */
public class ShareData {

    private String title;
    private String description;
    private String musicUrl;
    private int duration;
    private String webUrl;
    private String defaultText;
    private int imageId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "ShareData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                ", duration=" + duration +
                ", webUrl='" + webUrl + '\'' +
                ", defaultText='" + defaultText + '\'' +
                ", imageId=" + imageId +
                '}';
    }
}
