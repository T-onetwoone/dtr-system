package cn.edu.mju.ccce.dtrsystem.bmo;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.LoginBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-02-02 17:43<br>
 */
public interface LoginBmo {
    /**
     * 验证登录
     * @param inMap
     *  @return map key=msg
     */
    Map<String,Object> chackLogin(Map<String,Object> inMap);
}
