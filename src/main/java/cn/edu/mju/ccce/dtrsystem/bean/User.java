package cn.edu.mju.ccce.dtrsystem.bean;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bean.User<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>用户实体类<br>
 * <b>创建时间：</b>2020-02-08 20:35<br>
 */
public class User implements Serializable {

    private static final long serialVersionUID = -1320367561013449636L;
    private BigInteger USER_ID;
    private String USER_NAME;
    private BigInteger USER_NBR;
    private String USER_SEX;
    private int TYPE_ID;
    private String TYPE_NAME;
    private BigInteger USER_PHONE;
    private String USER_PASS;
    private BigInteger EVALUATE_NBR;
    private int USER_STATUS = 0;
    private Date CREAT_TIME;
    private Date UPDATE_TIME = new Date();

    public BigInteger getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(BigInteger USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public BigInteger getUSER_NBR() {
        return USER_NBR;
    }

    public void setUSER_NBR(BigInteger USER_NBR) {
        this.USER_NBR = USER_NBR;
    }

    public String getUSER_SEX() {
        return USER_SEX;
    }

    public void setUSER_SEX(String USER_SEX) {
        this.USER_SEX = USER_SEX;
    }

    public int getTYPE_ID() {
        return TYPE_ID;
    }

    public void setTYPE_ID(int TYPE_ID) {
        this.TYPE_ID = TYPE_ID;
    }

    public String getTYPE_NAME() {
        return TYPE_NAME;
    }

    public void setTYPE_NAME(String TYPE_NAME) {
        this.TYPE_NAME = TYPE_NAME;
    }

    public BigInteger getUSER_PHONE() {
        return USER_PHONE;
    }

    public void setUSER_PHONE(BigInteger USER_PHONE) {
        this.USER_PHONE = USER_PHONE;
    }

    public String getUSER_PASS() {
        return USER_PASS;
    }

    public void setUSER_PASS(String USER_PASS) {
        this.USER_PASS = USER_PASS;
    }

    public BigInteger getEVALUATE_NBR() {
        return EVALUATE_NBR;
    }

    public void setEVALUATE_NBR(BigInteger EVALUATE_NBR) {
        this.EVALUATE_NBR = EVALUATE_NBR;
    }

    public int getUSER_STATUS() {
        return USER_STATUS;
    }

    public void setUSER_STATUS(int USER_STATUS) {
        this.USER_STATUS = USER_STATUS;
    }

    public Date getCREAT_TIME() {
        return CREAT_TIME;
    }

    public void setCREAT_TIME(Date CREAT_TIME) {
        this.CREAT_TIME = CREAT_TIME;
    }

    public Date getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(Date UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (TYPE_ID != user.TYPE_ID) return false;
        if (USER_STATUS != user.USER_STATUS) return false;
        if (USER_ID != null ? !USER_ID.equals(user.USER_ID) : user.USER_ID != null) return false;
        if (USER_NAME != null ? !USER_NAME.equals(user.USER_NAME) : user.USER_NAME != null) return false;
        if (USER_NBR != null ? !USER_NBR.equals(user.USER_NBR) : user.USER_NBR != null) return false;
        if (USER_SEX != null ? !USER_SEX.equals(user.USER_SEX) : user.USER_SEX != null) return false;
        if (TYPE_NAME != null ? !TYPE_NAME.equals(user.TYPE_NAME) : user.TYPE_NAME != null) return false;
        if (USER_PHONE != null ? !USER_PHONE.equals(user.USER_PHONE) : user.USER_PHONE != null) return false;
        if (USER_PASS != null ? !USER_PASS.equals(user.USER_PASS) : user.USER_PASS != null) return false;
        if (EVALUATE_NBR != null ? !EVALUATE_NBR.equals(user.EVALUATE_NBR) : user.EVALUATE_NBR != null) return false;
        if (CREAT_TIME != null ? !CREAT_TIME.equals(user.CREAT_TIME) : user.CREAT_TIME != null) return false;
        return UPDATE_TIME != null ? UPDATE_TIME.equals(user.UPDATE_TIME) : user.UPDATE_TIME == null;
    }

    @Override
    public int hashCode() {
        int result = USER_ID != null ? USER_ID.hashCode() : 0;
        result = 31 * result + (USER_NAME != null ? USER_NAME.hashCode() : 0);
        result = 31 * result + (USER_NBR != null ? USER_NBR.hashCode() : 0);
        result = 31 * result + (USER_SEX != null ? USER_SEX.hashCode() : 0);
        result = 31 * result + TYPE_ID;
        result = 31 * result + (TYPE_NAME != null ? TYPE_NAME.hashCode() : 0);
        result = 31 * result + (USER_PHONE != null ? USER_PHONE.hashCode() : 0);
        result = 31 * result + (USER_PASS != null ? USER_PASS.hashCode() : 0);
        result = 31 * result + (EVALUATE_NBR != null ? EVALUATE_NBR.hashCode() : 0);
        result = 31 * result + USER_STATUS;
        result = 31 * result + (CREAT_TIME != null ? CREAT_TIME.hashCode() : 0);
        result = 31 * result + (UPDATE_TIME != null ? UPDATE_TIME.hashCode() : 0);
        return result;
    }

    /**
     * 获取在线用户信息
     *
     * @param httpSession
     * @return map  <p>key=USER_NBR</p><p>key=USER_NAME</p><p>key=USER_SEX </p><p>key=TYPE_NAME</p>
     */
    public static Map<String, Object> getUserMap(HttpSession httpSession) {
        try {
            String sessionID = httpSession.getId();
            //session里面最好改成用户类,不过比较懒  先放着
            Map<String, Object> uMgs = (Map<String, Object>) httpSession.getAttribute(sessionID);
            if (uMgs.isEmpty()) {
                return new HashMap<>();
            }
            return uMgs;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
