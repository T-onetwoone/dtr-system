package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.EvaluateStu;
import cn.edu.mju.ccce.dtrsystem.bean.EvaluateTea;

import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.EvaluateBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-03-16 10:14<br>
 */
public interface EvaluateBmo {

    /**
     * 学生创建一条新的评价记录
     * @param e
     * @return
     */
    public Map<String,Object> createStudentEvaluate(EvaluateStu e);

    /**
     * 查找学生评价记录
     * @param courseID
     * @param userNbr
     * @return map key=EvaluateStu class=EvaluateStu
     */
    public Map<String,Object> getStudentEvaluate(String courseID, String userNbr);

    /**
     * 老师创建一条评价记录
     * @param e
     * @return
     */
    public Map<String,Object> createTeacherEvaluate(EvaluateTea e);

    /**
     * 老师查找指定学生评价记录
     * @param courseID
     * @param studentNbr
     * @return map key=EvaluateTea
     */
    public Map<String,Object> getTeacherEvaluate(String courseID,String studentNbr);

    /**
     * 获取最近10条对该学生的评价记录
     * @param stuNbr
     * @return map key=stuHistory
     */
    public Map<String,Object> getStuHistoryEvaluate(String stuNbr);

}
