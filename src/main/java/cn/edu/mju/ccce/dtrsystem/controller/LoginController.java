package cn.edu.mju.ccce.dtrsystem.controller;

import cn.edu.mju.ccce.dtrsystem.bmo.LoginBmoImpl;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.MapTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.MapUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.controller.LoginContronller<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>用户登录控制<br>
 * <b>创建时间：</b>2020-02-02 16:49<br>
 */
@Controller
@RequestMapping("/dtr/user")
public class LoginController {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Resource(name = "cn.edu.mju.ccce.dtrsystem.Bmo.LoginBmoImpl")
    private LoginBmoImpl loginBmo;


    @RequestMapping("/check")
    @ResponseBody
    public Map<String, Object> check(@RequestBody Map<String, Object> inMap, HttpSession session) {
        Map<String, Object> returnMap = new HashMap<>();
        String unbr = MapTool.getString(inMap, "uNbr");
        String upass = MapTool.getString(inMap, "uPass");
//        String utype = MapTool.getString(inMap, "uType");
        try {
            unbr.substring(1); // 取1个串，探测非空
            upass.substring(1); // 取1个串，探测非空
        } catch (Exception e) {
            return G.page.returnMap(false, "输入信息错误！");
        }
        try {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userNbr", unbr);
            userMap.put("userPass", upass);
            Map<String, Object> relMap = loginBmo.chackLogin(userMap);
            Boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            Map<String, Object> userMsgMap = MapTool.getMap(relMap, "msg");
            if (!relMapBoolean) {
                return G.page.returnMap(false, "用户名或密码错误！");
            }
            userMsgMap.put("USER_NBR",unbr);
            session.setAttribute(session.getId(), userMsgMap);
            return G.page.returnMap(true, "ok");
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return G.page.returnMap(false, "登录异常！");
        }
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public Map<String, Object> getUser(HttpSession session) {
        try {
            Map<String, Object> userMap = (Map<String, Object>) session.getAttribute(session.getId());
            if (userMap.isEmpty()) {
                return G.page.returnMap(false, "用户未登录");
            }
            String userNbr = MapTool.getString(userMap, "USER_NBR");
            if("".equals(userNbr) || userNbr == null){
                return G.page.returnMap(false, "请先登录");
            }
            Map<String, Object> returnMap = G.page.returnMap(true, "ok");
            returnMap.put("user", userMap);
            return returnMap;
        } catch (Exception e) {
            return G.page.returnMap(false, "获取用户信息失败");
        }

    }

    @RequestMapping("/loginOut")
    @ResponseBody
    public Map<String, Object> loginOut(@RequestBody Map<String, Object> inMap, HttpSession session) {
        try {
            String loginOutUserNbr = MapTool.getString(inMap, "uNbr");
            String sessionID = session.getId();
            Map<String,Object> uMsg= (Map<String,Object>) session.getAttribute(sessionID);
            if (uMsg.isEmpty()){
                return G.page.returnMap(false,"用户已退出");
            }
            String userNbr = MapTool.getString(uMsg,"USER_NBR");
            if (!userNbr.equals(loginOutUserNbr)){
                return G.page.returnMap(false,"退出异常");
            }
            session.removeAttribute(sessionID);
            return G.page.returnMap(true, "ok");
        } catch (Exception e) {
            return G.page.returnMap(false, "退出失败");
        }

    }

}
