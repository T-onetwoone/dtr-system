package cn.edu.mju.ccce.dtrsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.controller.ErrorPageController<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-03-06 16:26<br>
 */
@Controller
@RequestMapping("/error")
public class ErrorPageController {
    @RequestMapping("/404")
    public String toPage404() { return "error/404"; }

    @RequestMapping("/400")
    public String toPage400() { return "error/400"; }

    @RequestMapping("/500")
    public String toPage500() { return "error/500"; }
}
