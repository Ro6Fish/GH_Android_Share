package me.rokevin.android.lib.sharesdk.businees.qq.model;

/**
 * Created by luokaiwen on 16/11/1.
 * <p/>
 * {
 * "ret": 0,
 * "msg": "",
 * "is_lost": 0,
 * "nickname": "ˊ5⒋鱼!.o",
 * "gender": "男",
 * "province": "上海",
 * "city": "黄浦",
 * "figureurl": "http://qzapp.qlogo.cn/qzapp/1105547051/26E82E07D51EEEB6D172B5B66AE6F71D/30",
 * "figureurl_1": "http://qzapp.qlogo.cn/qzapp/1105547051/26E82E07D51EEEB6D172B5B66AE6F71D/50",
 * "figureurl_2": "http://qzapp.qlogo.cn/qzapp/1105547051/26E82E07D51EEEB6D172B5B66AE6F71D/100",
 * "figureurl_qq_1": "http://q.qlogo.cn/qqapp/1105547051/26E82E07D51EEEB6D172B5B66AE6F71D/40",
 * "figureurl_qq_2": "http://q.qlogo.cn/qqapp/1105547051/26E82E07D51EEEB6D172B5B66AE6F71D/100",
 * "is_yellow_vip": "0",
 * "vip": "0",
 * "yellow_vip_level": "0",
 * "level": "0",
 * "is_yellow_year_vip": "0"
 * }
 */
public class QQUserInfo {

    private String id;
    private String nickname;
    private String gender;
    private String province;
    private String city;
    private String figureurl_qq_1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    @Override
    public String toString() {
        return "QQUserInfo{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", figureurl_qq_1='" + figureurl_qq_1 + '\'' +
                '}';
    }
}
