package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import cn.edu.mju.ccce.dtrsystem.bean.Reservation;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.IdGenerator;
import cn.edu.mju.ccce.dtrsystem.dao.CourseDao;
import cn.edu.mju.ccce.dtrsystem.dao.CourseTypeDao;
import cn.edu.mju.ccce.dtrsystem.dao.LoginDao;
import cn.edu.mju.ccce.dtrsystem.dao.ReservationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.CourseBmoImpl<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-02-11 19:49<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.bmo.CourseBmoImpl")
public class CourseBmoImpl implements CourseBmo {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.CourseDao")
    protected CourseDao courseDao;

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.CourseTypeDao")
    protected CourseTypeDao courseTypeDao;

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.LoginDao")
    protected LoginDao loginDao;

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.ReservationDao")
    protected ReservationDao reservationDao;

    /**
     * 添加课程
     *
     * @param course
     * @return
     */
    @Override
    public Map<String, Object> addCourse(Course course) {
        try {
            course.setCOURSE_ID(BigInteger.valueOf(IdGenerator.genLongId()));
            course.setEVALUATE_NBR(BigInteger.valueOf(IdGenerator.genLongId()));
            course.setCREAT_TIME(new Date());
            int insert = courseDao.insertNewCourse(course);
            if (insert > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "创建失败");
        } catch (Exception e) {
            log.error("新建课程异常：", e);
            return G.bmo.returnMap(false, "创建异常");
        }
    }

    /**
     * 获取课程类型id
     * 特殊,不用检测
     *
     * @param courseTypeName
     * @return
     */
    @Override
    public int getCourseIDbyName(String courseTypeName) {
        return Integer.parseInt(courseTypeDao.selectCourseTypeId(courseTypeName));
    }

    /**
     * 根据课程ID获得课程全部信息
     *
     * @param courseID
     * @return map key=courseDet
     */
    @Override
    public Map<String, Object> getCourseDetByID(String courseID) {
        try {
            Course course = new Course();
            try {
                course = courseDao.selectCourseByID(courseID);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("courseDet", course);
            return returnMap;
        } catch (Exception e) {
            log.error("查询课程详细异常:", e);
            return G.bmo.returnMap(false, "查询异常");
        }

    }

    /**
     * 根据老师Nbr获取可预约的课程列表
     *
     * @param teacherNbr
     * @return map key=courseList
     */
    @Override
    public Map<String, Object> getCourseListByTeacherNbr(String teacherNbr) {
        try {
            List<Course> courseList = new ArrayList<>();
            try {
                courseList = courseDao.selectCourseListByTeacherNbr(teacherNbr, "0");
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("courseList", courseList);
            return returnMap;
        } catch (Exception e) {
            log.error("查询发布课程异常:", e);
            return G.bmo.returnMap(false, "查询异常");
        }
    }

    /**
     * 根据老师Nbr获取已完成的课程列表
     *
     * @param teacherNbr
     * @return map key=courseDoneList
     */
    @Override
    public Map<String, Object> getCourseDoneListByTeacherNbr(String teacherNbr) {
        try {
            List<Course> courseList = new ArrayList<>();
            try {
                courseList = courseDao.selectCourseListByTeacherNbr(teacherNbr, "1");
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("courseDoneList", courseList);
            return returnMap;
        } catch (Exception e) {
            log.error("查询发布课程（完成）异常:", e);
            return G.bmo.returnMap(false, "查询异常");
        }
    }

    /**
     * 取消课程
     *
     * @param courseID
     * @return
     */
    @Override
    public Map<String, Object> cancelCourse(String courseID) {
        try {
            Course course = courseDao.selectCourseByID(courseID);
            List<Reservation> reservationList = new ArrayList<>();
            try {
                reservationList = reservationDao.selectAllReservationRecordByCourseID(courseID);
            } catch (NullPointerException e) {
            }
            if (!reservationList.isEmpty()) {
                for (Reservation r : reservationList) {
                    r.setRESERVATION_STATUS(2);
                    reservationDao.updateReservationCourseStatusByCourseID(r);
                }
            }
            int i = courseDao.upDateCourseDoneStuNbr("0", courseID, new Date());
            if (i <= 0) {
                return G.bmo.returnMap(false, "取消失败");
            }
            course.setCOURSE_STATUS(2);
            course.setUPDATE_TIME(new Date());
            int up = courseDao.updateCourse(course);
            if (up > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "取消失败");
        } catch (Exception e) {
            log.error("课程取消异常：", e);
            return G.bmo.returnMap(false, "课程取消异常");
        }
    }

    /**
     * 更新发布课程信息
     *
     * @param course
     * @return
     */
    @Override
    public Map<String, Object> upDateCourse(Course course) {
        try {
            course.setUPDATE_TIME(new Date());
            int result = courseDao.updateCourse(course);
            if (result > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "更新失败");
        } catch (Exception e) {
            log.error("更新课程信息异常：", e);
            return G.bmo.returnMap(false, "更新课程信息异常");
        }
    }

    /**
     * 获取预约课程的学生列表
     *
     * @param courseID
     * @return map key=userList
     */
    @Override
    public Map<String, Object> getCourseStuList(String courseID) {
        try {
            List<Reservation> reservationList = new ArrayList<>();
            try {
                reservationList = reservationDao.selectAllReservationRecordByCourseID(courseID);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空");
            }
            List<Map<String, Object>> userList = new ArrayList<>();
            for (Reservation r : reservationList) {
                long stuNbr = r.getUSER_NBR();
                Map<String, Object> user = loginDao.selectUserAllMsgByUserNbr(String.valueOf(stuNbr));
                userList.add(user);
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("userList", userList);
            return returnMap;
        } catch (Exception e) {
            log.error("获取学生预约信息异常：", e);
            return G.bmo.returnMap(false, "获取学生预约信息异常");
        }
    }

    /**
     * 获取已完成课程的学生列表
     *
     * @param courseID
     * @return map key=userList
     */
    @Override
    public Map<String, Object> getDoneCourseStuList(String courseID) {
        try {
            List<Reservation> reservationList = new ArrayList<>();
            try {
                reservationList = reservationDao.selectAllDoneReservationRecordByCourseID(courseID);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空");
            }
            List<Map<String, Object>> userList = new ArrayList<>();
            for (Reservation r : reservationList) {
                long stuNbr = r.getUSER_NBR();
                Map<String, Object> user = loginDao.selectUserAllMsgByUserNbr(String.valueOf(stuNbr));
                userList.add(user);
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("userList", userList);
            return returnMap;
        } catch (Exception e) {
            log.error("获取学生预约信息异常：", e);
            return G.bmo.returnMap(false, "获取学生预约信息异常");
        }
    }

    /**
     * 特殊，定时检测job专用
     * 控制课程过期
     *
     * @param courseID
     * @return
     */
    @Override
    public Map<String, Object> passDueCourse(String courseID) {
        try {
            Course course = courseDao.selectCourseByID(courseID);
            List<Reservation> reservationList = new ArrayList<>();
            try {
                reservationList = reservationDao.selectAllReservationRecordByCourseID(courseID);
            } catch (NullPointerException e) {
            }
            if (!reservationList.isEmpty()) {
                for (Reservation r : reservationList) {
                    r.setRESERVATION_STATUS(1);
                    reservationDao.updateReservationCourseStatusByCourseID(r);
                }
            }
            int i = courseDao.upDateCourseDoneStuNbr("0", courseID, new Date());
            if (i <= 0) {
                return G.bmo.returnMap(false, "过期失败");
            }
            course.setCOURSE_STATUS(1);
            course.setUPDATE_TIME(new Date());
            int up = courseDao.updateCourse(course);
            if (up > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "过期失败");
        } catch (Exception e) {
            log.error("课程过期异常：", e);
            return G.bmo.returnMap(false, "课程过期异常");
        }
    }

    /**
     * 获取发布历史记录
     *
     * @param teacherNbr
     * @return map <p>key=courseList<p/><p>key=underwayList<p/><p>key=doneList<p/><p>key=cancelList<p/>
     */
    @Override
    public Map<String, Object> getAllHistory(String teacherNbr) {
        try {
            List<Course> courseList = new ArrayList<>();
            try {
                courseList = courseDao.selectHistory(teacherNbr);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(true, "空");
            }
            if (courseList.isEmpty()) {
                return G.bmo.returnMap(true, "空");
            }
            List<Course> underwayList = new ArrayList<>();
            List<Course> doneList = new ArrayList<>();
            List<Course> cancelList = new ArrayList<>();
            for (Course c : courseList) {
                int st = c.getCOURSE_STATUS();
                switch (st) {
                    case 0:
                        underwayList.add(c);
                        break;
                    case 1:
                        doneList.add(c);
                        break;
                    case 2:
                        cancelList.add(c);
                        break;
                    default:
                        break;
                }
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("courseList", courseList);
            returnMap.put("underwayList", underwayList);
            returnMap.put("doneList", doneList);
            returnMap.put("cancelList", cancelList);
            return returnMap;
        } catch (Exception e) {
            log.error("查询课程历史记录异常：", e);
            return G.bmo.returnMap(false, "查询课程历史记录异常");
        }
    }

    /**
     * 获取所有的课程类别
     *
     * @return map key=typeList
     */
    @Override
    public Map<String, Object> getAllCourseType() {
        try {
            List<Map<String, Object>> typeList = new ArrayList<>();
            try {
                typeList = courseTypeDao.selectCourseType();

            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "空");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("typeList", typeList);
            return returnMap;
        } catch (Exception e) {
            log.error("查询课程类别异常：", e);
            return G.bmo.returnMap(false, "查询课程类别异常");
        }
    }

    /**
     * 获取今天的课程
     *
     * @return map key=courseList
     */
    @Override
    public Map<String, Object> getTodayCourse() {
        try {
            List<Course> courseList = new ArrayList<>();
            try {
                courseList = courseDao.selectCourseToDay();
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询今天课程为空");
            }
            if (courseList.isEmpty()){
                return G.bmo.returnMap(false, "查询今天课程为空");
            }
            Map<String,Object> returnMap = G.bmo.returnMap(true,"ok");
            returnMap.put("courseList",courseList);
            return returnMap;
        } catch (Exception e) {
            log.error("查询今天课程异常：",e);
            return G.bmo.returnMap(false, "查询今天课程异常");
        }
    }

    /**
     * 获取发布历史
     * @return map key=pastWeek key=pastMonth key=courseList
     */
    @Override
    public Map<String, Object> getHistory() {
        try{
            List<Course> pastWeek = new ArrayList<>();
            List<Course> pastMonthCourseList = new ArrayList<>();
            List<Map<String, Object>> pastMonth  = new ArrayList<>();
            try {
                pastWeek = courseDao.selectCourseBeforePastWeek();
                pastMonth = courseDao.selectCourseBefore30Day();
                pastMonthCourseList = courseDao.selectCourseListMsgBefore30Day();
            } catch (NullPointerException e) {
                // ignore
            }
            Map<String,Object> returnMap = G.bmo.returnMap(true,"ok");
            returnMap.put("pastWeek",pastWeek);
            returnMap.put("pastMonth",pastMonth);
            returnMap.put("courseList",pastMonthCourseList);
            return returnMap;
        }catch (Exception e){
            log.error("查询历史课程异常：",e);
            return G.bmo.returnMap(false, "查询历史课程异常");
        }
    }

    /**
     * 获取发布历史
     * @return map key=oneMonth
     */
    @Override
    public Map<String, Object> getIssueList() {
        try{
            List<Course> oneMonth = new ArrayList<>();
            try {
                oneMonth = courseDao.selectCourseListMsg30Day();
            } catch (NullPointerException e) {
                // ignore
            }
            Map<String,Object> returnMap = G.bmo.returnMap(true,"ok");
            returnMap.put("oneMonth",oneMonth);
            return returnMap;
        }catch (Exception e){
            log.error("查询历史课程异常：",e);
            return G.bmo.returnMap(false, "查询历史课程异常");
        }
    }

    /**
     * 删除课程类别
     * @param name
     * @return
     */
    @Override
    public Map<String, Object> addCourseType(String name) {
        try{
            Map<String,Object> inMap = new HashMap<>();
            inMap.put("COURSE_TYPE_STATUS",0);
            inMap.put("COURSE_TYPE_NAME",name);
            int i = courseTypeDao.creaseCourseType(inMap);
            if (i>0){
                return G.bmo.returnMap(true,"ok");
            }
            return G.bmo.returnMap(false,"新增失败");
        }catch (Exception e){
            log.error("新增异常：",e);
            return G.bmo.returnMap(false,"新增异常");
        }
    }

    /**
     * 删除课程类别
     * @param name
     * @return
     */
    @Override
    public Map<String, Object> delCourseType(String name) {
        try{
            String id = "";
            try{
                id = courseTypeDao.selectCourseTypeId(name);
            }catch (Exception e){
                return G.bmo.returnMap(false,"删除失败");
            }
            if ("".equals(id)){
                return G.bmo.returnMap(false,"删除失败");
            }
            int i = courseTypeDao.deleteCourseType(id);
            if (i>0){
                return G.bmo.returnMap(true,"ok");
            }
            return G.bmo.returnMap(false,"删除失败");
        }catch (Exception e){
            log.error("删除异常：",e);
            return G.bmo.returnMap(false,"删除异常");
        }
    }

}
