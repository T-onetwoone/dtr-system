package cn.edu.mju.ccce.dtrsystem.dao;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.CourseTypeDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>课程类别<br>
 * <b>创建时间：</b>2020-02-18 20:42<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.CourseTypeDao")
public interface CourseTypeDao {

    /**
     * 通过类别ID获取类别名
     * @param courseTypeName
     * @return
     */
    String selectCourseTypeId(String courseTypeName);

    /**
     * 获取所有的课程类别
     * @return
     */
    public List<Map<String,Object>> selectCourseType();

    /**
     * 删除课程类别
     * @param CourseTId
     * @return
     */
    public int deleteCourseType (String CourseTId);

    /**
     * 新增课程类别
     * @param map
     * @return
     */
    public int creaseCourseType (Map<String,Object> map);

}
