package me.rokevin.android.lib.sharesdk.businees.qq;

/**
 * Created by luokaiwen on 16/11/1.
 * <p/>
 * {
 * "ret":0,
 * "pay_token":"xxxxxxxxxxxxxxxx",
 * "pf":"openmobile_android",
 * "expires_in":"7776000",
 * "openid":"xxxxxxxxxxxxxxxxxxx",
 * "pfkey":"xxxxxxxxxxxxxxxxxxx",
 * "msg":"sucess",
 * "access_token":"xxxxxxxxxxxxxxxxxxxxx"
 * }
 */
public class QQToken {

    private String openid;
    private String access_token;
    private Long expires_in;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public String toString() {
        return "QQToken{" +
                "openid='" + openid + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                '}';
    }
}
