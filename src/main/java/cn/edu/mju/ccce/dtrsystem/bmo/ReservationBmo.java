package cn.edu.mju.ccce.dtrsystem.bmo;

import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.ReservationBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>课程预约Bmo<br>
 * <b>创建时间：</b>2020-03-05 10:18<br>
 */
public interface ReservationBmo {
    /**
     *预约课程
     * @param inMap
     * @return
     */
    public Map<String,Object> reservationCourse (Map<String,Object> inMap);

    /**
     * 获取可预约课程列表
     * @param
     * @return map key=courseList
     */
    public Map<String,Object> getCanReservationCourseList();

    /**
     * 获取用户所有预约未上课的历史记录
     * @param userNbr
     * @return map key=reservationList
     */
    public Map<String,Object> getAllReservationCourseByUserNbr (String userNbr);

    /**
     * 获取用户所有完成的记录
     * @param userNbr
     * @return map key=reservationDoneList
     */
    public Map<String,Object> getAllReservationCourseDoneByUserNbr (String userNbr);

    /**
     * 取消预约课程
     * @param courseID
     * @param status
     * @return
     */
    public Map<String,Object> updateReservationCourseStatus (String courseID,String userNbr,String status);

    /**
     * 获取预约历史记录
     * @param userNbr
     * @return map <p>key=reservationList<p/><p>key=underwayList<p/><p>key=doneList<p/><p>key=cancelList<p/>
     */
    public Map<String,Object> getAllHistory(String userNbr);
}
