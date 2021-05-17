package cn.edu.mju.ccce.dtrsystem.dao;

import cn.edu.mju.ccce.dtrsystem.bean.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.ReservationDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-03-04 14:15<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.ReservationDao")
public interface ReservationDao {

    /**
     * 增加一条新的预约课程信息
     * @param reservation
     */
    public int insertReservationRecord(Reservation reservation);

    /**
     * 根据学号查询学号预约记录
     * @param StuNbr
     * @return
     */
    public List<Reservation> selectAllReservationRecordByStuNbr(String StuNbr);

    /**
     * 根据学号查询学号完成上课的预约记录
     * @param StuNbr
     * @return
     */
    public List<Reservation> selectAllReservationDoneRecordByStuNbr(String StuNbr);

    /**
     * 根据课程ID查询所有预约的记录
     * @param courseID
     * @return
     */
    public List<Reservation> selectAllReservationRecordByCourseID(String courseID);

    /**
     * 根据课程ID查询所有已完成的预约记录
     * @param courseID
     * @return
     */
    public List<Reservation> selectAllDoneReservationRecordByCourseID(String courseID);

    /**
     * 根据courseID改变预约课程预约状态
     * @param R
     */
    public int updateReservationCourseStatusByCourseID(Reservation R);

    /**
     * 获取预约历史记录（所有状态的）
     * @return
     */
    public List<Reservation> selectReservationHistory(String userNbr);
}
