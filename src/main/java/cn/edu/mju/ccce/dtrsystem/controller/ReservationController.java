package cn.edu.mju.ccce.dtrsystem.controller;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import cn.edu.mju.ccce.dtrsystem.bean.Reservation;
import cn.edu.mju.ccce.dtrsystem.bmo.CourseBmo;
import cn.edu.mju.ccce.dtrsystem.bmo.ReservationBmo;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.MapTool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.controller.ReservationController<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>课程预约控制<br>
 * <b>创建时间：</b>2020-02-25 22:15<br>
 */
@Controller
@RequestMapping("/dtr/rese")
public class ReservationController {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.CourseBmoImpl")
    private CourseBmo courseBmo;

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.ReservationBmoImpl")
    private ReservationBmo reservationBmo;

    /**
     * 获取可预约课程列表
     *
     * @return
     */
    @RequestMapping("/getList")
    @ResponseBody
    public Map<String, Object> getCourseList() {
        try {
            Map<String, Object> returnMap = new HashMap<>();
            Map<String, Object> courseListMap = reservationBmo.getCanReservationCourseList();
            boolean listMapBoolean = G.bmo.returnMapBool(courseListMap);
            if (!listMapBoolean) {
                String msg = G.bmo.returnMapMsg(courseListMap);
                returnMap = G.page.returnMap(false, msg);
                return returnMap;
            }
            List<Course> courseList = (List<Course>) MapTool.getObject(courseListMap, "courseList");
            //不做查询是否为空检查  bmo已经做了检查返回
            // 阿里fastjson返回首字母总是小写,暂时没有更好的解决办法  暂时这样
            JSONArray courseListFormat = JSONArray.parseArray(toJSONString(courseList, new PascalNameFilter()));
            returnMap = G.page.returnMap(true, "ok");
            returnMap.put("courseList", courseListFormat);
            return returnMap;
        } catch (Exception e) {
            log.error("获取可预约课程列表异常：", e);
            return G.page.returnMap(false, "获取可预约课程列表异常");
        }
    }

    /**
     * 预约课程
     *
     * @param inMap
     * @param httpSession
     * @return
     */
    @RequestMapping("/reseCourse")
    @ResponseBody
    public Map<String, Object> reserCourse(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        String sessionId = "";
        try {
            sessionId = httpSession.getId();
        } catch (Exception e) {
            return G.page.returnMap(false, "请先登录");
        }
        String courseID = MapTool.getString(inMap, "courseID");
        try {
            courseID.substring(1);// 探测非空
        } catch (Exception e) {
            log.error("预约条件异常", e);
            return G.page.returnMap(false, "预约条件异常");
        }
        try {
            Map<String, Object> uMsg = (Map<String, Object>) httpSession.getAttribute(sessionId);
            String type_name = MapTool.getString(uMsg, "TYPE_NAME");
            if (!"学生".equals(type_name)) {
                return G.page.returnMap(false, "非学生用户不能预约");
            }
            String uNbr = MapTool.getString(uMsg, "USER_NBR");
            Map<String, Object> reservationMap = reservationBmo.getAllReservationCourseByUserNbr(uNbr);
            boolean reservationMapBoolean = G.bmo.returnMapBool(reservationMap);
            if (!reservationMapBoolean) {
                String msg = G.bmo.returnMapMsg(reservationMap);
                return G.page.returnMap(false, msg);
            }
            List<Reservation> reservationList = (List<Reservation>) MapTool.getObject(reservationMap, "reservationList");
            if (!reservationList.isEmpty()) {
                for (Reservation reservation : reservationList) {
                    String reservationCourseID = String.valueOf(reservation.getCOURSE_ID());
                    if (courseID.equals(reservationCourseID)) {
                        return G.page.returnMap(false, "不能重复预约");
                    }
                }
            }
            String user_name = MapTool.getString(uMsg, "USER_NAME");
            Map<String, Object> reseMap = new HashMap<>();
            reseMap.put("userNbr", uNbr);
            reseMap.put("userName", user_name);
            reseMap.put("courseID", courseID);
            Map<String, Object> relMap = reservationBmo.reservationCourse(reseMap);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.bmo.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            return G.page.returnMap(true, "预约成功");
        } catch (Exception e) {
            log.error("预约异常", e);
            return G.page.returnMap(false, "预约异常");
        }
    }

    /**
     * 取消预约课程
     *
     * @param inMap
     * @param httpSession
     * @return
     */
    @RequestMapping("/unReseCourse")
    @ResponseBody
    public Map<String, Object> unReseCourse(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        String sessionID = "";
        try {
            sessionID = httpSession.getId();
        } catch (Exception e) {
            return G.page.returnMap(false, "请先登录");
        }
        String courseID = MapTool.getString(inMap, "courseID");
        try {
            courseID.substring(1);// 探测非空
        } catch (Exception e) {
            log.error("预约条件异常", e);
            return G.page.returnMap(false, "预约条件异常");
        }
        try {
            Map<String, Object> uMsg = (Map<String, Object>) httpSession.getAttribute(sessionID);
            String type_name = MapTool.getString(uMsg, "TYPE_NAME");
            if (!"学生".equals(type_name)) {
                return G.page.returnMap(false, "非学生用户不能操作");
            }
            String uNbr = MapTool.getString(uMsg, "USER_NBR");
            Map<String, Object> reservationMap = reservationBmo.getAllReservationCourseByUserNbr(uNbr);
            boolean reservationMapBoolean = G.bmo.returnMapBool(reservationMap);
            if (!reservationMapBoolean) {
                String msg = G.bmo.returnMapMsg(reservationMap);
                return G.page.returnMap(false, msg);
            }
            List<Reservation> reservationList = (List<Reservation>) MapTool.getObject(reservationMap, "reservationList");
            if (!reservationList.isEmpty()) {
                for (Reservation reservation : reservationList) {
                    String reservationCourseID = String.valueOf(reservation.getCOURSE_ID());
                    if (courseID.equals(reservationCourseID)) {
                        Map<String, Object> relMap = reservationBmo.updateReservationCourseStatus(courseID, uNbr, "2");
                        boolean relMapBoolean = G.bmo.returnMapBool(relMap);
                        if (!relMapBoolean) {
                            String msg = G.bmo.returnMapMsg(relMap);
                            return G.page.returnMap(false, msg);
                        }

                        return G.page.returnMap(true, "ok");
                    }
                }
            }
            return G.page.returnMap(false, "预约历史为空，请刷新页面后重试");
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 查询课程详细
     *
     * @param inMap
     * @return
     */
    @RequestMapping("/getCourseDet")
    @ResponseBody
    public Map<String, Object> getCourseDet(@RequestBody Map<String, Object> inMap) {
        String courseID = MapTool.getString(inMap, "courseID");
        try {
            courseID.substring(1);// 探测非空
        } catch (Exception e) {
            log.error("查询条件异常", e);
            return G.page.returnMap(false, "查询条件异常");
        }
        try {
            Map<String, Object> relMap = courseBmo.getCourseDetByID(courseID);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.bmo.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            Course courseDet = (Course) MapTool.getObject(relMap, "courseDet");
            //不做查询是否为空检查  bmo已经做了检查返回
            // 阿里fastjson返回首字母总是小写,暂时没有更好的解决办法  暂时这样
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            returnMap.put("courseDet", courseDet);
            return returnMap;
        } catch (Exception e) {
            log.error("查询课程详细异常：", e);
            return G.page.returnMap(false, "查询课程详细异常");
        }
    }

    /**
     * 获取自己所有的预约记录
     *
     * @param inMap
     * @param httpSession
     * @return
     */
    @RequestMapping("/getSelfHistory")
    @ResponseBody
    public Map<String, Object> getSelfReservationHistory(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        String sessionID = "";
        try {
            sessionID = httpSession.getId();
        } catch (Exception e) {
            return G.page.returnMap(false, "请先登录");
        }
        try {
            Map<String, Object> uMsg = (Map<String, Object>) httpSession.getAttribute(sessionID);
            String uNbr = MapTool.getString(uMsg, "USER_NBR");
            Map<String, Object> reservationMap = reservationBmo.getAllReservationCourseByUserNbr(uNbr);
            boolean reservationMapBoolean = G.bmo.returnMapBool(reservationMap);
            if (!reservationMapBoolean) {
                String msg = G.bmo.returnMapMsg(reservationMap);
                return G.page.returnMap(false, msg);
            }
            Map<String, Object> relMap = new HashMap<>();
            // 做提前声明，不然前端数据异常也不会报错自己还偷偷吃掉这个BUG
            relMap.put("noReservationList", "");
            relMap.put("noReservationDoneList", "");
            List<Reservation> reservationList = (List<Reservation>) MapTool.getObject(reservationMap, "reservationList");
            if (reservationList.isEmpty()) {
                relMap.put("noReservationList", "暂无预约记录");
            }
            Map<String, Object> reservationDoneMap = reservationBmo.getAllReservationCourseDoneByUserNbr(uNbr);
            boolean reservationDoneMapBoolean = G.bmo.returnMapBool(reservationDoneMap);
            if (!reservationDoneMapBoolean) {
                String msg = G.bmo.returnMapMsg(reservationDoneMap);
                return G.page.returnMap(false, msg);
            }
            List<Reservation> reservationDoneList = (List<Reservation>) MapTool.getObject(reservationDoneMap, "reservationDoneList");
            if (reservationDoneList.isEmpty()) {
                relMap.put("noReservationDoneList", "暂无预约记录");
            }
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            returnMap.putAll(relMap);
            returnMap.put("reservationList", reservationList);
            returnMap.put("reservationDoneList", reservationDoneList);
            return returnMap;
        } catch (Exception e) {
            log.error("查询预约历史信息异常", e);
            return G.page.returnMap(false, "查询预约历史信息异常");
        }
    }

}
