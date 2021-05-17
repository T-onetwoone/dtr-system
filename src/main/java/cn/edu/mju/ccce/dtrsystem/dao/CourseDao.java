package cn.edu.mju.ccce.dtrsystem.dao;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.CourseDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-02-18 20:42<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.CourseDao")
public interface CourseDao {

    /**
     * 插入一条新的课程信息
     *
     * @param course
     * @return
     */
    public int insertNewCourse(Course course);

    /**
     * 获取可预约课程列表
     *
     * @return
     */
    public List<Course> selectCourseList();

    /**
     * 通过课程ID查找课程信息
     *
     * @param courseID
     * @return
     */
    public Course selectCourseByID(String courseID);

    /**
     * 更新指定课程已预约人数
     *
     * @param courseDoneStuNbr
     * @return
     */
    public int upDateCourseDoneStuNbr(String courseDoneStuNbr, String courseID, Date upTime);

    /**
     * 获取老师的课程列表
     * @param courseTeacherNbr
     * @param courseStatus    =0可预约  =1 已过期
     * @return
     */
    public List<Course> selectCourseListByTeacherNbr(String courseTeacherNbr,String courseStatus);


    /**
     * 更新课程信息
     * @param course
     * @return
     */
    public int updateCourse(Course course);

    /**
     * 获取历史记录
     * @param teacherNbr
     * @return
     */
    public List<Course> selectHistory(String teacherNbr);

    /**
     * 查找今天的课程
     * @return
     */
    public List<Course> selectCourseToDay();

    /**
     * 查找一周前的数据
     * @return
     */
    public List<Course> selectCourseBeforePastWeek();

    /**
     * 查找30天前的数据
     * @return
     */
    public List<Map<String,Object>> selectCourseBefore30Day();

    /**
     *  查找30天前的课程列表
     * @return
     */
    public List<Course> selectCourseListMsgBefore30Day();

    /**
     *  查找未来30天的课程列表
     * @return
     */
    public List<Course> selectCourseListMsg30Day();

    /**
     * 查找指定天数内的数据
     * @return
     */
    public List<Course> selectCourseByDay(int day);

}
