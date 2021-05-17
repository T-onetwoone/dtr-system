package cn.edu.mju.ccce.dtrsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.controller.testcontroller<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-02-02 16:10<br>
 */
@Controller
@RequestMapping("/hello")
public class testcontroller {


    @RequestMapping("/test")
    @ResponseBody
    public String test (){
        return "hello world!";
    }
}
