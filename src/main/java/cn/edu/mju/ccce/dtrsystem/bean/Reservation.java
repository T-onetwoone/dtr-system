package cn.edu.mju.ccce.dtrsystem.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;


/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bean.Reservation<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>预约课程实体类<br>
 * <b>创建时间：</b>2020-03-04 14:20<br>
 */
public class Reservation implements Serializable {

    private static final long serialVersionUID = -8645724840745642893L;

    private long RESERVATION_ID;// bigint(17) NOT NULL COMMENT '预约ID',
    private long COURSE_ID;//bigint(17) DEFAULT NULL COMMENT '预约的课程ID',
    private Date COURSE_TIME;// datetime DEFAULT NULL COMMENT '预约课程时间',
    private String COURSE_TYPE_NAME;// varchar(20) DEFAULT NULL '预约课程类型名',
    private String  COURSE_NAME;// varchar(32) DEFAULT NULL '预约课程名',
    private long COURSE_TEACHER_NBR;// bigint(32) DEFAULT NULL COMMENT '预约课程的任课老师NBR',
    private String COURSE_TEACHER_NAME;//varchar(50) DEFAULT NULL COMMENT '预约课程的任课老师',
    private long USER_NBR;// bigint(17) DEFAULT NULL COMMENT '预约的用户NBR',
    private String USER_NAME;// varchar(64) DEFAULT NULL COMMENT '预约的用户名称',
    private int RESERVATION_STATUS = 0;// int(1) DEFAULT NULL COMMENT '预约状态 0=正常  1=过期 2=异常',
    private Date CREAT_TIME;// datetime DEFAULT NULL COMMENT '创建时间',
    private Date UPDATE_TIME = new Date();// datetime DEFAULT NULL COMMENT '更新时间',

    public long getRESERVATION_ID() {
        return RESERVATION_ID;
    }

    public void setRESERVATION_ID(long RESERVATION_ID) {
        this.RESERVATION_ID = RESERVATION_ID;
    }

    public long getCOURSE_ID() {
        return COURSE_ID;
    }

    public void setCOURSE_ID(long COURSE_ID) {
        this.COURSE_ID = COURSE_ID;
    }

    public Date getCOURSE_TIME() {
        return COURSE_TIME;
    }

    public void setCOURSE_TIME(Date COURSE_TIME) {
        this.COURSE_TIME = COURSE_TIME;
    }

    public String getCOURSE_TYPE_NAME() {
        return COURSE_TYPE_NAME;
    }

    public void setCOURSE_TYPE_NAME(String COURSE_TYPE_NAME) {
        this.COURSE_TYPE_NAME = COURSE_TYPE_NAME;
    }

    public String getCOURSE_NAME() {
        return COURSE_NAME;
    }

    public void setCOURSE_NAME(String COURSE_NAME) {
        this.COURSE_NAME = COURSE_NAME;
    }

    public long getCOURSE_TEACHER_NBR() {
        return COURSE_TEACHER_NBR;
    }

    public void setCOURSE_TEACHER_NBR(long COURSE_TEACHER_NBR) {
        this.COURSE_TEACHER_NBR = COURSE_TEACHER_NBR;
    }

    public String getCOURSE_TEACHER_NAME() {
        return COURSE_TEACHER_NAME;
    }

    public void setCOURSE_TEACHER_NAME(String COURSE_TEACHER_NAME) {
        this.COURSE_TEACHER_NAME = COURSE_TEACHER_NAME;
    }

    public long getUSER_NBR() {
        return USER_NBR;
    }

    public void setUSER_NBR(long USER_NBR) {
        this.USER_NBR = USER_NBR;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public int getRESERVATION_STATUS() {
        return RESERVATION_STATUS;
    }

    public void setRESERVATION_STATUS(int RESERVATION_STATUS) {
        this.RESERVATION_STATUS = RESERVATION_STATUS;
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

        Reservation that = (Reservation) o;

        if (RESERVATION_ID != that.RESERVATION_ID) return false;
        if (COURSE_ID != that.COURSE_ID) return false;
        if (COURSE_TEACHER_NBR != that.COURSE_TEACHER_NBR) return false;
        if (USER_NBR != that.USER_NBR) return false;
        if (RESERVATION_STATUS != that.RESERVATION_STATUS) return false;
        if (COURSE_TIME != null ? !COURSE_TIME.equals(that.COURSE_TIME) : that.COURSE_TIME != null) return false;
        if (COURSE_TYPE_NAME != null ? !COURSE_TYPE_NAME.equals(that.COURSE_TYPE_NAME) : that.COURSE_TYPE_NAME != null)
            return false;
        if (COURSE_NAME != null ? !COURSE_NAME.equals(that.COURSE_NAME) : that.COURSE_NAME != null) return false;
        if (COURSE_TEACHER_NAME != null ? !COURSE_TEACHER_NAME.equals(that.COURSE_TEACHER_NAME) : that.COURSE_TEACHER_NAME != null)
            return false;
        if (USER_NAME != null ? !USER_NAME.equals(that.USER_NAME) : that.USER_NAME != null) return false;
        if (CREAT_TIME != null ? !CREAT_TIME.equals(that.CREAT_TIME) : that.CREAT_TIME != null) return false;
        return UPDATE_TIME != null ? UPDATE_TIME.equals(that.UPDATE_TIME) : that.UPDATE_TIME == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (RESERVATION_ID ^ (RESERVATION_ID >>> 32));
        result = 31 * result + (int) (COURSE_ID ^ (COURSE_ID >>> 32));
        result = 31 * result + (COURSE_TIME != null ? COURSE_TIME.hashCode() : 0);
        result = 31 * result + (COURSE_TYPE_NAME != null ? COURSE_TYPE_NAME.hashCode() : 0);
        result = 31 * result + (COURSE_NAME != null ? COURSE_NAME.hashCode() : 0);
        result = 31 * result + (int) (COURSE_TEACHER_NBR ^ (COURSE_TEACHER_NBR >>> 32));
        result = 31 * result + (COURSE_TEACHER_NAME != null ? COURSE_TEACHER_NAME.hashCode() : 0);
        result = 31 * result + (int) (USER_NBR ^ (USER_NBR >>> 32));
        result = 31 * result + (USER_NAME != null ? USER_NAME.hashCode() : 0);
        result = 31 * result + RESERVATION_STATUS;
        result = 31 * result + (CREAT_TIME != null ? CREAT_TIME.hashCode() : 0);
        result = 31 * result + (UPDATE_TIME != null ? UPDATE_TIME.hashCode() : 0);
        return result;
    }
}
