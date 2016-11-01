package me.rokevin.android.lib.sharesdk.exception;

/**
 * Created by luokaiwen on 16/11/1.
 * <p/>
 * 没有设置AppKey异常
 */
public class AppKeyException extends Exception {

    public AppKeyException() {
        super();
    }

    public AppKeyException(String detailMessage) {
        super(detailMessage);
    }
}
