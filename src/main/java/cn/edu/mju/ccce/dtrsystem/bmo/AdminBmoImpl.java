package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.MapTool;
import cn.edu.mju.ccce.dtrsystem.dao.AdminDao;
import cn.edu.mju.ccce.dtrsystem.dao.LoginDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.AdminBmoImpl<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-15 13:30<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.bmo.AdminBmoImpl")
public class AdminBmoImpl implements AdminBmo {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.AdminDao")
    protected AdminDao adminDao;
    /**
     * 管理员验证登录
     *
     * @param inMap
     * @return map key=admin
     */
    @Override
    public Map<String, Object> checkAdminLogin(Map<String, Object> inMap) {
        try{
            Map<String, Object> relMap = new HashMap<>();
            try {
                relMap = adminDao.checkAdmin(inMap);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空！");
            }
            if (relMap.isEmpty()) {
                return G.bmo.returnMap(false, "查询为空！");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "查询成功");
            returnMap.put("admin", relMap);
            return returnMap;
        }catch (Exception e){
            log.error("查询错误", e);
            return G.bmo.returnMap(false, "查询错误");
        }
    }

    /**
     * 获取管理员信息
     * @param adminID
     * @return map key=admin
     */
    @Override
    public Map<String, Object> getAdmin(String adminID) {
        try{
            Map<String, Object> relMap = new HashMap<>();
            try {
                relMap = adminDao.selectAdmin(adminID);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空！");
            }
            if (relMap.isEmpty()) {
                return G.bmo.returnMap(false, "查询为空！");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "查询成功");
            returnMap.put("admin", relMap);
            return returnMap;
        }catch (Exception e){
            log.error("查询错误", e);
            return G.bmo.returnMap(false, "查询错误");
        }
    }

    /**
     * 判断是否是管理员
     * @param session
     * @return
     */
    @Override
    public boolean isAdmin(HttpSession session) {
        try {
            String sessionID = session.getId();
            Map<String, Object> userMsgMap = (Map<String, Object>) session.getAttribute(sessionID);
            if (userMsgMap.isEmpty()) {
                return false;
            }

            String id = MapTool.getString(userMsgMap, "id");
            String sex = MapTool.getString(userMsgMap, "sex");
            String name = MapTool.getString(userMsgMap, "name");
            String phone = MapTool.getString(userMsgMap, "phone");
            Map<String, Object> relMap = getAdmin(id);
            boolean relMapBoolean = G.bmo.returnMapBool(relMap);
            if (!relMapBoolean) {
                return false;
            }
            Map<String, Object> admin = MapTool.getMap(relMap, "admin");
            String rel_id = MapTool.getString(admin, "id");
            String rel_name = MapTool.getString(admin, "name");
            String rel_sex = MapTool.getString(admin, "sex");
            String rel_phone = MapTool.getString(admin, "phone");
            return id.equals(rel_id) && name.equals(rel_name) && phone.equals(rel_phone) && sex.equals(rel_sex);
        }catch (Exception e){
            return false;
        }
    }
}
