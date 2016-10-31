package me.rokevin.android.lib.sharesdk;

/**
 * Created by luokaiwen on 16/10/31.
 * <p/>
 * 登录相关接口
 */
public interface ILogin {

    void login();

    void logout();

    void getUserInfo();

    void refreshToke();

    void getAccessToken();
}
