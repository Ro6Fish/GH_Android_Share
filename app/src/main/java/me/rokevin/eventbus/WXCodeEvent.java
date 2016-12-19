package me.rokevin.eventbus;

/**
 * Created by luokaiwen on 16/12/19.
 */

public class WXCodeEvent {

    public String code;

    public WXCodeEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
