package me.rokevin.android.lib.sharesdk.businees.wx;

import android.content.Context;
import android.os.AsyncTask;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.rokevin.android.lib.sharesdk.util.LogUtil;

/**
 * Created by luokaiwen on 16/10/31.
 */
public class WXLogin {

    private static final String TAG = WXLogin.class.getSimpleName();
    private Context mContext;
    private IWXAPI mIwxapi;

    public WXLogin(Context context, IWXAPI iwxapi) {
        mContext = context;
        mIwxapi = iwxapi;
    }

    /**
     * 获取授权code
     *
     * @param state 随机码标识是微信回复的消息
     */
    public void getCode(String state) {

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state;
        mIwxapi.sendReq(req);
    }

    /**
     * 获取用户Token
     *
     * @param appId
     * @param secret
     * @param code
     */
    public void getAccessToken(String appId, String secret, String code) {

        getAccessTokenTask.execute(appId, secret, code);
    }

    public void refreshToken() {


    }

    /**
     * 获取用户信息
     *
     * @param token
     * @param openId
     */
    public void getUserInfo(String token, String openId) {

        getUserInfoTask.execute(token, openId);
    }

    /**
     * 请求token任务
     */
    private AsyncTask getAccessTokenTask = new AsyncTask() {

        @Override
        protected String doInBackground(Object[] params) {

            String appId = (String) params[0];
            String secret = (String) params[1];
            String code = (String) params[2];

            String urlstr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";

            HttpURLConnection connection = null;

            String line = "";
            String totalLine = "";

            try {
                URL url = new URL(urlstr);
                connection = ((HttpURLConnection) url.openConnection());
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {

                    totalLine += line;
                }

                LogUtil.e(TAG, "totalLine:" + totalLine);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return totalLine;
        }
    };

    /**
     * 请求刷新token任务
     */
    private AsyncTask refreshTokenTask = new AsyncTask() {

        @Override
        protected String doInBackground(Object[] params) {

            String appId = (String) params[0];
            String refreshToken = (String) params[1];

            String urlstr = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appId + "&grant_type=refresh_token&refresh_token=" + refreshToken;

            HttpURLConnection connection = null;

            String line = "";
            String totalLine = "";

            try {
                URL url = new URL(urlstr);
                connection = ((HttpURLConnection) url.openConnection());
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {

                    totalLine += line;
                }

                LogUtil.e(TAG, "totalLine:" + totalLine);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return totalLine;
        }
    };

    /**
     * 请求用户信息任务
     */
    private AsyncTask getUserInfoTask = new AsyncTask() {

        @Override
        protected String doInBackground(Object[] params) {

            String token = (String) params[0];
            String openId = (String) params[1];

            String urlstr = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openId;

            HttpURLConnection connection = null;

            String line = "";
            String totalLine = "";

            try {
                URL url = new URL(urlstr);
                connection = ((HttpURLConnection) url.openConnection());
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = br.readLine()) != null) {

                    totalLine += line;
                }

                LogUtil.e(TAG, "userInfo:" + totalLine);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return totalLine;
        }
    };
}
