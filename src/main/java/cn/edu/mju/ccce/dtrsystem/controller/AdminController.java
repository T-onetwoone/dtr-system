package cn.edu.mju.ccce.dtrsystem.controller;

import cn.edu.mju.ccce.dtrsystem.bean.Course;
import cn.edu.mju.ccce.dtrsystem.bean.User;
import cn.edu.mju.ccce.dtrsystem.bmo.AdminBmo;
import cn.edu.mju.ccce.dtrsystem.bmo.CourseBmo;
import cn.edu.mju.ccce.dtrsystem.bmo.PassForgetRequestBmo;
import cn.edu.mju.ccce.dtrsystem.bmo.UserBmo;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.controller.AdminController<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-15 12:49<br>
 */
@Controller
@RequestMapping("dtr/admin")
public class AdminController {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static String ADMIN_PASS = "";

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.AdminBmoImpl")
    private AdminBmo adminBmo;

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.CourseBmoImpl")
    private CourseBmo courseBmo;

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.UserBmoImpl")
    private UserBmo userBmo;


    @Resource(name = "cn.edu.mju.ccce.dtrsystem.bmo.PassForgetRequestBmoImpl")
    private PassForgetRequestBmo requestBmo;

    /**
     * 管理员登录
     *
     * @param inMap
     * @param session
     * @return
     */
    @RequestMapping("admin-login")
    @ResponseBody
    public Map<String, Object> adminLogin(@RequestBody Map<String, Object> inMap, HttpSession session) {
        if (inMap.isEmpty()) {
            return G.page.returnMap(false, "非法输入！");
        }
        try {
            String adminId = MapTool.getString(inMap, "adminID");
            String adminPass = MapTool.getString(inMap, "adminPass");
            Map<String, Object> adminMap = new HashMap<>();
            adminMap.put("adminId", adminId);
            adminMap.put("adminPass", adminPass);
            Map<String, Object> relMap = adminBmo.checkAdminLogin(adminMap);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            Map<String, Object> userMsgMap = MapTool.getMap(relMap, "admin");
            if (!relMapBoolean) {
                return G.page.returnMap(false, "用户名或密码错误！");
            }
            session.setAttribute(session.getId(), userMsgMap);
            ADMIN_PASS = adminPass;
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            returnMap.putAll(userMsgMap);
            return returnMap;
        } catch (Exception e) {
            log.error("管理员登录异常：", e);
            return G.page.returnMap(false, "管理员登录异常");
        }
    }

    /**
     * 获取管理员信息
     *
     * @param session
     * @return
     */
    @RequestMapping("getAdmin")
    @ResponseBody
    public Map<String, Object> getAdmin(HttpSession session) {
        try {
            Map<String, Object> userMsgMap = new HashMap<>();
            String sessionID = session.getId();
            try {
                userMsgMap = (Map<String, Object>) session.getAttribute(sessionID);
                if (userMsgMap.isEmpty()) {
                    return G.page.returnMap(false, "请先登录！");
                }
            } catch (Exception e) {
                //igron
            }
            String id = MapTool.getString(userMsgMap, "id");
            String sex = MapTool.getString(userMsgMap, "sex");
            String name = MapTool.getString(userMsgMap, "name");
            String phone = MapTool.getString(userMsgMap, "phone");
            Map<String, Object> relMap = adminBmo.getAdmin(id);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.page.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            Map<String, Object> admin = MapTool.getMap(relMap, "admin");
            String rel_id = MapTool.getString(admin, "id");
            String rel_name = MapTool.getString(admin, "name");
            String rel_sex = MapTool.getString(admin, "sex");
            String rel_phone = MapTool.getString(admin, "phone");
            if (id.equals(rel_id) && name.equals(rel_name) && phone.equals(rel_phone) && sex.equals(rel_sex)) {
                admin.put("pass", ADMIN_PASS);
                Map<String, Object> returnMap = G.page.returnMap(true, "ok");
                returnMap.put("admin", admin);
                return returnMap;
            }
            session.removeAttribute(sessionID);
            return G.page.returnMap(false, "信息错误！");
        } catch (Exception e) {
            log.error("获取信息异常：", e);
            return G.page.returnMap(false, "获取信息异常");
        }
    }

    /**
     * 管理员退出
     *
     * @param inMap
     * @param session
     * @return
     */
    @RequestMapping("adminOut")
    @ResponseBody
    public Map<String, Object> adminOut(@RequestBody Map<String, Object> inMap, HttpSession session) {
        try {
            String loginOutUserNbr = MapTool.getString(inMap, "adminID");
            String sessionID = session.getId();
            Map<String, Object> userMsgMap = (Map<String, Object>) session.getAttribute(sessionID);
            if (userMsgMap.isEmpty()) {
                return G.page.returnMap(false, "已退出！");
            }
            String userNbr = MapTool.getString(userMsgMap, "id");
            if (!userNbr.equals(loginOutUserNbr)) {
                return G.page.returnMap(false, "退出异常");
            }
            session.removeAttribute(sessionID);
            ADMIN_PASS = "";
            return G.page.returnMap(true, "ok");
        } catch (Exception e) {
            return G.page.returnMap(false, "退出失败");
        }
    }

    /**
     * 获取课程发布历史
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("getHistory")
    @ResponseBody
    public Map<String, Object> getHistory(HttpSession httpSession) {
        try {
            if (!adminBmo.isAdmin(httpSession)) {
                return G.page.returnMap(false, "非管理员不可操作！");
            }
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            Map<String, Object> typeMap = courseBmo.getAllCourseType();
            boolean typeMapBoolean = G.bmo.returnMapBool(typeMap);
            if (!typeMapBoolean) {
                String msg = G.bmo.returnMapMsg(typeMap);
                returnMap.put("getAllCourseType失败", msg);
            }
            List<Map<String, Object>> typeList = (List<Map<String, Object>>) MapTool.getObject(typeMap, "typeList");
            Map<String, Object> relMap = courseBmo.getHistory();
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.page.returnMapMsg(relMap);
                returnMap.put("getHistory失败", msg);
            }
            List<Course> pastWeek = (List<Course>) MapTool.getObject(relMap, "pastWeek");
            List<Course> courseList = (List<Course>) MapTool.getObject(relMap, "courseList");
            List<Map<String, Object>> pastMonth = (List<Map<String, Object>>) MapTool.getObject(relMap, "pastMonth");
            List<Map<String, Object>> weekList = parseWeekCourseList(pastWeek);
            Map<String, Object> mapList = parseMonthCourseList(pastMonth);
//            if (weekList.isEmpty() && courseList.isEmpty()) {
//                return G.page.returnMap(false, "查找历史课程为空");
//            }
            Map<String, Object> map1 = requestBmo.getForgetRequestList();
            boolean map1Boolean = G.bmo.returnMapBool(relMap);
            if (!map1Boolean) {
                String msg = G.page.returnMapMsg(relMap);
                returnMap.put("getRequestList失败", msg);
            }
            List<Map<String, Object>> mapList1 = (List<Map<String, Object>>) MapTool.getObject(map1, "reList");
            returnMap.put("pastWeek", weekList);
            returnMap.put("pastMonth", mapList);
            returnMap.put("pastMonthCourseList", courseList);
            returnMap.put("typeList", getCourseTypeList());
            returnMap.put("allTypeList", typeList);
            returnMap.put("requestList", mapList1);
            return returnMap;
        } catch (Exception e) {
            log.error("查找历史课程异常：", e);
            return G.page.returnMap(false, "查找历史课程异常");
        }
    }

    /**
     * 获取可预约课程列表
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("getCouList")
    @ResponseBody
    public Map<String, Object> getCouList(HttpSession httpSession) {
        try {
            if (!adminBmo.isAdmin(httpSession)) {
                return G.page.returnMap(false, "非管理员不可操作！");
            }
            ;
            Map<String, Object> relMap = courseBmo.getIssueList();
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.page.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            List<Course> oneMonth = (List<Course>) MapTool.getObject(relMap, "oneMonth");
            if (oneMonth.isEmpty()) {
                return G.page.returnMap(false, "查找课程为空");
            }
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            returnMap.put("oneMonth", oneMonth);
            return returnMap;
        } catch (Exception e) {
            log.error("查找课程异常：", e);
            return G.page.returnMap(false, "查找课程异常");
        }
    }

    private List<Map<String, Object>> parseWeekCourseList(List<Course> courseList) throws Exception {
        List<String> typeList = getCourseTypeList();
        if (typeList.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, Object> returnMap = new HashMap<>();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (String s : typeList) {
            List<Course> sl = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            for (Course c : courseList) {
                String sc = c.getCOURSE_TYPE_NAME();
                if (s.equals(sc)) {
                    sl.add(c);
                }
            }
            map.put("value", sl.size());
            map.put("courseList", sl);
            map.put("name", s);
            map.put("tID", courseBmo.getCourseIDbyName(s));
            mapList.add(map);
        }
        return mapList;
    }

    private Map<String, Object> parseMonthCourseList(List<Map<String, Object>> courseList) throws Exception {
        List<String> typeList = getCourseTypeList();
        if (typeList.isEmpty()) {
            return new HashMap<>();
        }
        List<Map<String, Object>> sl2 = new ArrayList<>();
        for (String s : typeList) {
            List<Integer> sl = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            String st = "";
            String lastName = "";
            int k = 0;
            for (Map<String, Object> m : courseList) {
                String mtime = MapTool.getString(m, "time");
                String cs = MapTool.getString(m, "name");
                if ("".equals(cs) || cs == null) {
                    sl.add(0);
                    k++;
                } else {
                    if (st.equals(mtime)) {
                        if (s.equals(cs)) {
                            sl.set((k - 1), sl.get(k - 1) + 1);
                        }
                    } else {
                        if (s.equals(cs)) {
                            sl.add(1);
                        } else {
                            sl.add(0);
                        }
                        k++;
                    }
                }
                lastName = cs;
                st = mtime;
            }
            map.put("name", s);
            map.put("type", "line");
            map.put("data", sl);
            sl2.add(map);
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("courseList", sl2);
        return returnMap;
    }

    private List<String> getCourseTypeList() throws Exception {
        Map<String, Object> typeMap = courseBmo.getAllCourseType();
        boolean relMapBoolean = G.bmo.returnMapBool(typeMap);
        if (!relMapBoolean) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> typeMapList = (List<Map<String, Object>>) MapTool.getObject(typeMap, "typeList");
        List<String> typeList = new ArrayList<>();
        for (Map<String, Object> m : typeMapList) {
            String type = MapTool.getString(m, "COURSE_TYPE_NAME");
            typeList.add(type);
        }
        return typeList;
    }

    /**
     * 新增课程类型名
     *
     * @param inMap
     * @param httpSession
     * @return
     */
    @RequestMapping("addCType")
    @ResponseBody
    public Map<String, Object> addCourseType(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        try {
            if (!adminBmo.isAdmin(httpSession)) {
                return G.page.returnMap(false, "非管理员不可操作！");
            }
            String CT_name = MapTool.getString(inMap, "ctName");
            if ("".equals(CT_name)) {
                return G.page.returnMap(false, "非法输入");
            }
            Map<String, Object> relMap = courseBmo.addCourseType(CT_name);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.page.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            return G.page.returnMap(true, "ok");
        } catch (Exception e) {
            log.error("新增异常：", e);
            return G.page.returnMap(false, "新增异常");
        }
    }

    /**
     * 删除课程类型
     *
     * @param inMap
     * @param httpSession
     * @return
     */
    @RequestMapping("delCType")
    @ResponseBody
    public Map<String, Object> delCourseType(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        try {
            if (!adminBmo.isAdmin(httpSession)) {
                return G.page.returnMap(false, "非管理员不可操作！");
            }
            String CT_name = MapTool.getString(inMap, "ctName");
            if ("".equals(CT_name)) {
                return G.page.returnMap(false, "非法输入");
            }
            Map<String, Object> relMap = courseBmo.delCourseType(CT_name);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.page.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            return G.page.returnMap(true, "ok");
        } catch (Exception e) {
            log.error("删除异常：", e);
            return G.page.returnMap(false, "删除异常");
        }
    }

    /**
     * 获取所有用户
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("getAllUser")
    @ResponseBody
    public Map<String, Object> getAllUser(HttpSession httpSession) {
        try {
            if (!adminBmo.isAdmin(httpSession)) {
                return G.page.returnMap(false, "非管理员不可操作！");
            }
            Map<String, Object> relMap = userBmo.getAllUser();
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.page.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            List<User> stuList = (List<User>) MapTool.getObject(relMap, "stuList");
            List<User> teaList = (List<User>) MapTool.getObject(relMap, "teaList");
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            returnMap.put("stuList", stuList);
            returnMap.put("teaList", teaList);
            return returnMap;
        } catch (Exception e) {
            log.error("查找异常：", e);
            return G.page.returnMap(false, "查找异常");
        }
    }

    /**
     * 新增用户
     *
     * @param inMap
     * @param httpSession
     * @return
     */
    @RequestMapping("addUser")
    @ResponseBody
    public Map<String, Object> addUser(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        if (!adminBmo.isAdmin(httpSession)) {
            return G.page.returnMap(false, "非管理员不可操作！");
        }
        String userName = MapTool.getString(inMap, "userName");
        String userType = MapTool.getString(inMap, "userType");
        String userNbr = MapTool.getString(inMap, "userNbr");
        String userSex = MapTool.getString(inMap, "userSex");
        BigInteger userPhone = (BigInteger) MapTool.getObject(inMap, "evaluateScore");
        try {
            userName.substring(1);//探测非空
            userType.substring(1);
            userNbr.substring(1);
            userSex.substring(1);
        } catch (Exception e) {
            return G.page.returnMap(false, "输入为空！");
        }
        try {
            User user = new User();
            user.setUSER_NAME(userName);
            user.setTYPE_NAME(userType);
            user.setUSER_SEX(userSex);
            user.setUSER_NBR(BigInteger.valueOf(Integer.valueOf(userNbr)));
            if (!("".equals(userPhone))) {
                user.setUSER_PHONE(userPhone);
            }
            Map<String, Object> relMap = userBmo.userAdd(user);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.bmo.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            return G.page.returnMap(true, "ok");
        } catch (Exception e) {
            log.error("用户新建异常", e);
            return G.page.returnMap(false, "用户新建异常");
        }
    }

    @RequestMapping("getUserDet")
    @ResponseBody
    public Map<String, Object> getUserDet(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        if (!adminBmo.isAdmin(httpSession)) {
            return G.page.returnMap(false, "非管理员不可操作！");
        }
        String userNbr = MapTool.getString(inMap, "userNbr");
        BigInteger userPhone = (BigInteger) MapTool.getObject(inMap, "evaluateScore");
        try {
            userNbr.substring(1);
        } catch (Exception e) {
            return G.page.returnMap(false, "输入为空！");
        }
        try {
            Map<String, Object> relMap = userBmo.selectUserByUserNbr(userNbr);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.bmo.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            User user = (User) MapTool.getObject(relMap, "user");
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            returnMap.put("user", user);
            return returnMap;
        } catch (Exception e) {
            log.error("用户查找异常", e);
            return G.page.returnMap(false, "用户查找异常");
        }
    }

    @RequestMapping("upUserDet")
    @ResponseBody
    public Map<String, Object> upUserDet(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        if (!adminBmo.isAdmin(httpSession)) {
            return G.page.returnMap(false, "非管理员不可操作！");
        }
        String userName = MapTool.getString(inMap, "userName");
        String userNbr = MapTool.getString(inMap, "userNbr");
        String userSex = MapTool.getString(inMap, "userSex");
        String userPass = MapTool.getString(inMap, "userPass");
        String userPhone = MapTool.getString(inMap, "userPhone");
        try {
            userNbr.substring(1);
        } catch (Exception e) {
            return G.page.returnMap(false, "输入为空！");
        }
        try {
            Map<String, Object> relMap = userBmo.selectUserByUserNbr(userNbr);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                String msg = G.bmo.returnMapMsg(relMap);
                return G.page.returnMap(false, msg);
            }
            User user = (User) MapTool.getObject(relMap, "user");
            user.setUSER_PHONE(BigInteger.valueOf(Long.parseLong(userPhone)));
            user.setUSER_SEX(userSex);
            user.setUSER_NAME(userName);
            user.setUSER_PASS(userPass);
            Map<String, Object> userMap = userBmo.upDataUser(user);
            boolean userMapBoolean = G.bmo.returnMapBool(userMap);
            if (!userMapBoolean) {
                String msg = G.bmo.returnMapMsg(userMap);
                return G.page.returnMap(false, msg);
            }
            return G.page.returnMap(true, "ok");
        } catch (Exception e) {
            log.error("用户更新异常", e);
            return G.page.returnMap(false, "用户更新异常");
        }
    }

    @RequestMapping("resetPass")
    @ResponseBody
    public Map<String, Object> checkForgetPass(@RequestBody Map<String, Object> inMap, HttpSession httpSession) {
        if (!adminBmo.isAdmin(httpSession)) {
            return G.page.returnMap(false, "非管理员不可操作！");
        }
        try {
            String id = MapTool.getString(inMap, "id");
            String status = MapTool.getString(inMap, "status");
            Map<String,Object> map = new HashMap<>();
            map.put("id",id);
            map.put("status",status);
            Map<String, Object> relmap = requestBmo.upDataForgetRequest(map);
            boolean returnMapBool = G.bmo.returnMapBool(relmap);
            if (!returnMapBool) {
                String msg = G.bmo.returnMapMsg(relmap);
                return G.page.returnMap(false, msg);
            }
            return G.page.returnMap(true, "ok");
        } catch (
                Exception e) {
            log.error("用户更新异常", e);
            return G.page.returnMap(false, "用户更新异常");
        }
    }
}
