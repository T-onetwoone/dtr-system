package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.Course;

import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.CourseBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>课程Bmo<br>
 * <b>创建时间：</b>2020-02-11 19:49<br>
 */
public interface CourseBmo {

    /**
     * 添加课程
     *
     * @param course
     * @return
     */
    public  Map<String, Object> addCourse(Course course);

    /**
     * 获取课程类型id
     * 特殊,不用检测
     * @param courseTypeName
     * @return
     */
    public int getCourseIDbyName(String courseTypeName);

    /**
     * 根据课程ID获得课程全部信息
     * @param courseID
     * @return map key=courseDet
     */
    public Map<String,Object> getCourseDetByID(String courseID);

    /**
     * 根据老师Nbr获取可预约的课程列表
     * @param teacherNbr
     * @return map key=courseList
     */
    public Map<String,Object> getCourseListByTeacherNbr(String teacherNbr);

    /**
     * 根据老师Nbr获取已完成的课程列表
     * @param teacherNbr
     * @return map key=courseDoneList
     */
    public Map<String,Object> getCourseDoneListByTeacherNbr(String teacherNbr);

    /**
     * 取消课程
     * @param courseID
     * @return
     */
    public Map<String,Object> cancelCourse (String courseID);

    /**
     * 更新发布课程信息
     * @param course
     * @return
     */
    public Map<String,Object> upDateCourse (Course course);

    /**
     * 获取预约课程的学生列表
     * @param courseID
     *  @return map key=userList
     */
    public Map<String,Object> getCourseStuList(String courseID);

    /**
     * 获取已完成课程的学生列表
     * @param courseID
     * @return map key=userList
     */
    public Map<String,Object> getDoneCourseStuList(String courseID);

    /**
     * 特殊，定时检测job专用
     * 控制课程过期
     * @param courseID
     * @return
     */
    public Map<String,Object> passDueCourse (String courseID);

    /**
     * 获取发布历史记录
     * @param teacherNbr
     * @return map <p>key=courseList<p/><p>key=underwayList<p/><p>key=doneList<p/><p>key=cancelList<p/>
     */
    public Map<String,Object> getAllHistory(String teacherNbr);

    /**
     * 获取所有的课程类别
     * @return map key=typeList
     */
    public Map<String,Object> getAllCourseType();

    /**
     * 获取今天的课程
     * @return map key=courseList
     */
    public Map<String,Object> getTodayCourse();

    /**
     * 获取发布历史
     * @return map key=pastWeek key=pastMonth key=courseList
     */
    public Map<String,Object> getHistory();

    /**
     * 获取发布
     * @return map key=oneMonth
     */
    public Map<String,Object> getIssueList();

    /**
     * 新增课程类别
     * @param name
     * @return
     */
    Map<String,Object> addCourseType(String name);

    /**
     * 删除课程类别
     * @param name
     * @return
     */
    Map<String,Object> delCourseType(String name);

}
