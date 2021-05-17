package cn.edu.mju.ccce.dtrsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.controller.PageContronller<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>页面跳转控制<br>
 * <b>创建时间：</b>2020-02-04 17:49<br>
 */
@Controller
@RequestMapping("/dtr")
public class PageController {

    @RequestMapping(value = {"", "/", "/home", "home.html"})
    public String home(HttpSession session) {
        try {
            Map<String, Object> user = (Map<String, Object>) session.getAttribute(session.getId());
        } catch (Exception e) {
            return "login";
        }
        return "home";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("issue")
    public String issue() {
        return "issue";
    }

    @RequestMapping("reservation")
    public String reservation() {
        return "reservation";
    }

    @RequestMapping("history")
    public String history() {
        return "history";
    }

    @RequestMapping("evaluate")
    public String evaluate() {
        return "evaluate";
    }

    @RequestMapping("animate/homeBackground")
    public String homeBackground(){
        return "animate/homeBackground";
    }

    @RequestMapping("admin-login")
    public  String admin(){ return "admin/admin-login";}

    @RequestMapping("admin")
    public  String adminHome(){ return "admin/admin";}
}
