package cn.edu.mju.ccce.dtrsystem.bmo;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.AdminBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-15 13:30<br>
 */
public interface AdminBmo {

    /**
     * 管理员验证登录
     * @param inMap
     * @return map key=admin
     */
    Map<String,Object> checkAdminLogin(Map<String,Object> inMap);

    /**
     * 获取管理员信息
     * @param adminID
     * @return map key=admin
     */
    Map<String,Object> getAdmin(String adminID);

    /**
     * 判断是否是管理员
     * @param session
     * @return
     */
    boolean isAdmin(HttpSession session);

}
