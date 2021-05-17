package cn.edu.mju.ccce.dtrsystem.dao;

import cn.edu.mju.ccce.dtrsystem.bean.EvaluateStu;
import cn.edu.mju.ccce.dtrsystem.bean.EvaluateTea;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.EvaluateTeaDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-03-19 14:38<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.EvaluateTeaDao")
public interface EvaluateTeaDao {

    /**
     * 新建一条评论记录
     * @param evaluate
     * @return
     */
    public int insert (EvaluateTea evaluate);

    /**
     * 获取学生对课程的评价
     * @param courseID
     * @param userNbr
     * @return
     */
    public EvaluateTea selectEvaluate (String courseID, String userNbr);

    /**
     * 获取最近10条对该学生的评价记录
     * @param stuNbr
     * @return
     */
    public List<Map<String,Object>> selectStuEvaluate (String stuNbr);
}
