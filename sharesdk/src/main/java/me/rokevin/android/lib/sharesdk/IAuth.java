package me.rokevin.android.lib.sharesdk;

/**
 * Created by luokaiwen on 16/10/31.
 * <p/>
 * 授权相关
 */
public interface IAuth {

    void auth();

    void refreshToke();

    void getUserInfo();

    void logout();
}
