package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import cn.edu.mju.ccce.dtrsystem.bean.EvaluateStu;
import cn.edu.mju.ccce.dtrsystem.bean.EvaluateTea;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.MapTool;
import cn.edu.mju.ccce.dtrsystem.dao.EvaluateStuDao;
import cn.edu.mju.ccce.dtrsystem.dao.EvaluateTeaDao;
import cn.edu.mju.ccce.dtrsystem.dao.LoginDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.EvaluateBmoImpl<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-03-16 10:15<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.bmo.EvaluateBmoImpl")
public class EvaluateBmoImpl implements EvaluateBmo {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.EvaluateStuDao")
    protected EvaluateStuDao evaluateStuDao;

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.EvaluateTeaDao")
    protected EvaluateTeaDao evaluateTeaDao;

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.LoginDao")
    protected LoginDao loginDao;

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.CourseBmoImpl")
    private CourseBmo courseBmo;

    /**
     * 创建一条新的评价记录
     *
     * @param e
     * @return
     */
    @Override
    public Map<String, Object> createStudentEvaluate(EvaluateStu e) {
        try {
            String courseID = String.valueOf(e.getCOURSE_ID());
            String userNbr = String.valueOf(e.getUSER_NBR());
            Map<String, Object> eMap = getStudentEvaluate(courseID, userNbr);
            boolean eMapBoolean = G.bmo.returnMapBool(eMap);
            if (eMapBoolean) {
                String msg = G.bmo.returnMapMsg(eMap);
                return G.bmo.returnMap(false, msg);
            }
            EvaluateStu eS = (EvaluateStu) MapTool.getObject(eMap, "EvaluateStu");
            if (eS != null) {
                return G.bmo.returnMap(false, "课程已评价");
            }
            Map<String, Object> courseMap = courseBmo.getCourseDetByID(String.valueOf(e.getCOURSE_ID()));
            boolean relMapBoolean = G.bmo.returnMapBool(courseMap);
            if (!relMapBoolean) {
                String msg = G.bmo.returnMapMsg(courseMap);
                return G.page.returnMap(false, msg);
            }
            Course c = (Course) MapTool.getObject(courseMap, "courseDet");
            String course_name = c.getCOURSE_NAME();
            BigInteger evaluateNbr = c.getEVALUATE_NBR();
            e.setCREAT_TIME(new Date());
            e.setCOURSE_NAME(course_name);
            e.setEVALUATE_NBR(evaluateNbr);
            e.setEVALUATE_STATUS(0);
            int insert = evaluateStuDao.insert(e);
            if (insert > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "创建失败");
        } catch (Exception ex) {
            log.error("创建评价记录异常：", ex);
            return G.bmo.returnMap(false, "创建评价记录异常");
        }
    }

    /**
     * 查找学生评价记录
     *
     * @param courseID
     * @param userNbr
     * @return map key=EvaluateStu
     */
    @Override
    public Map<String, Object> getStudentEvaluate(String courseID, String userNbr) {
        try {
            EvaluateStu e = new EvaluateStu();
            try {
                e = evaluateStuDao.selectEvaluate(courseID, userNbr);
                e.getCOURSE_NAME();
            } catch (NullPointerException ex) {
                return G.bmo.returnMap(false, "查询为空！");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("EvaluateStu", e);
            return returnMap;
        } catch (Exception e) {
            log.error("查询评价记录异常：", e);
            return G.bmo.returnMap(false, "查询评价记录异常");
        }
    }

    @Override
    public Map<String, Object> createTeacherEvaluate(EvaluateTea e) {
        try {
            String courseID = String.valueOf(e.getCOURSE_ID());
            String studentNbr = String.valueOf(e.getUSER_NBR());
            Map<String, Object> eMap = getTeacherEvaluate(courseID, studentNbr);
            boolean eMapBoolean = G.bmo.returnMapBool(eMap);
            if (!eMapBoolean) {
                String msg = G.bmo.returnMapMsg(eMap);
                return G.bmo.returnMap(false, msg);
            }
            EvaluateStu eS = (EvaluateStu) MapTool.getObject(eMap, "EvaluateStu");
            if (eS != null) {
                return G.bmo.returnMap(false, "课程已评价");
            }
            Map<String, Object> userMap = loginDao.selectUserAllMsgByUserNbr(studentNbr);
            String userName = MapTool.getString(userMap, "USER_NAME");
            Map<String, Object> courseMap = courseBmo.getCourseDetByID(String.valueOf(e.getCOURSE_ID()));
            boolean relMapBoolean = G.bmo.returnMapBool(courseMap);
            if (!relMapBoolean) {
                String msg = G.bmo.returnMapMsg(courseMap);
                return G.page.returnMap(false, msg);
            }
            Course c = (Course) MapTool.getObject(courseMap, "courseDet");
            String course_name = c.getCOURSE_NAME();
            e.setCREAT_TIME(new Date());
            e.setCOURSE_NAME(course_name);
            e.setEVALUATE_STATUS(0);
            e.setUSER_NAME(userName);
            int insert = evaluateTeaDao.insert(e);
            if (insert > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "创建失败");
        } catch (Exception ex) {
            log.error("创建评价记录异常：", ex);
            return G.bmo.returnMap(false, "创建评价记录异常");
        }
    }

    /**
     * 查找老师评价记录
     *
     * @param courseID
     * @param studentNbr
     * @return map key=EvaluateTea
     */
    @Override
    public Map<String, Object> getTeacherEvaluate(String courseID, String studentNbr) {
        try {
            EvaluateTea e = new EvaluateTea();
            try {
                e = evaluateTeaDao.selectEvaluate(courseID, studentNbr);
                e.getCOURSE_NAME();
            } catch (NullPointerException ex) {
                return G.bmo.returnMap(true, "查询为空！");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("EvaluateTea", e);
            return returnMap;
        } catch (Exception e) {
            log.error("查询评价记录异常：", e);
            return G.bmo.returnMap(false, "查询评价记录异常");
        }
    }

    /**
     * 获取最近10条对该学生的评价记录
     * @param stuNbr
     * @return map key=stuHistory
     */
    @Override
    public Map<String, Object> getStuHistoryEvaluate(String stuNbr) {
        try {
            List<Map<String,Object>> e = new ArrayList<>();
            try {
                e = evaluateTeaDao.selectStuEvaluate(stuNbr);
            } catch (NullPointerException ex) {
                return G.bmo.returnMap(true, "查询为空！");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("stuHistory", e);
            return returnMap;
        } catch (Exception e) {
            log.error("查询评价记录异常：", e);
            return G.bmo.returnMap(false, "查询评价记录异常");
        }
    }
}
