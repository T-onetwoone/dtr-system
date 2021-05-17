package cn.edu.mju.ccce.dtrsystem.controller;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import cn.edu.mju.ccce.dtrsystem.bean.Reservation;
import cn.edu.mju.ccce.dtrsystem.bean.User;
import cn.edu.mju.ccce.dtrsystem.bmo.CourseBmo;
import cn.edu.mju.ccce.dtrsystem.bmo.ReservationBmo;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.MapTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.controller.HistoryController<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>历史记录<br>
 * <b>创建时间：</b>2020-03-26 12:41<br>
 */
@Controller
@RequestMapping("/dtr/his")
public class HistoryController {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.CourseBmoImpl")
    private CourseBmo courseBmo;

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.ReservationBmoImpl")
    private ReservationBmo reservationBmo;

    @RequestMapping("/getHistory")
    @ResponseBody
    Map<String,Object> getHistory(@RequestBody Map<String, Object> inMap, HttpSession httpSession){
        try{
            Map<String,Object> userMsg = User.getUserMap(httpSession);
            String type = MapTool.getString(userMsg,"TYPE_NAME");
            String userNbr = MapTool.getString(userMsg,"USER_NBR");
            if ("学生".equals(type)){
                Map<String,Object> relMap = reservationBmo.getAllHistory(userNbr);
                boolean relMapBoolean = G.bmo.returnMapBool(relMap);
                if (!relMapBoolean) {
                    String msg = G.bmo.returnMapMsg(relMap);
                    Map<String,Object> returnMap = G.page.returnMap(false, msg);
                    return returnMap;
                }
                List<Reservation> reservationList = (List<Reservation>) MapTool.getObject(relMap,"reservationList");
                List<Reservation> underwayList = (List<Reservation>) MapTool.getObject(relMap,"underwayList");
                List<Reservation> doneList = (List<Reservation>) MapTool.getObject(relMap,"doneList");
                List<Reservation> cancelList = (List<Reservation>) MapTool.getObject(relMap,"cancelList");
                Map<String,Object> returnMap = G.page.returnMap(true, "ok");
                returnMap.put("reservationList",reservationList);
                returnMap.put("underwayList",underwayList);
                returnMap.put("doneList",doneList);
                returnMap.put("cancelList",cancelList);
                return returnMap;
            }else if("教师".equals(type)){
                Map<String,Object> relMap = courseBmo.getAllHistory(userNbr);
                boolean relMapBoolean = G.bmo.returnMapBool(relMap);
                if (!relMapBoolean) {
                    String msg = G.bmo.returnMapMsg(relMap);
                    Map<String,Object> returnMap = G.page.returnMap(false, msg);
                    return returnMap;
                }
                List<Course> courseList = (List<Course>) MapTool.getObject(relMap,"courseList");
                Map<String,Object> returnMap = G.page.returnMap(true, "ok");
                returnMap.put("courseList",courseList);
                return returnMap;
            }else {
                return G.page.returnMap(false,"权限错误");
            }
        }catch (Exception e){
            log.error("查询历史记录异常:",e);
            return G.page.returnMap(false,"查询历史记录异常");
        }
    }
}
