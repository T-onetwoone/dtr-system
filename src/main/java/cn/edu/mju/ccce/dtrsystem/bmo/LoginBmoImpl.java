package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.dao.LoginDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.LoginBmoImpl<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-02-02 17:44<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.Bmo.LoginBmoImpl")
public class LoginBmoImpl implements LoginBmo {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.LoginDao")
    protected LoginDao loginDao;

    /**
     * 验证登录
     *
     * @param inMap
     * @return map key=msg
     */
    @Override
    public Map<String, Object> chackLogin(Map<String, Object> inMap) {
        try {
            Map<String, Object> relMap = new HashMap<>();
            try {
                relMap = loginDao.selectUser(inMap);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "查询为空！");
            }
            if (relMap == null) {
                return G.bmo.returnMap(false, "查询为空！");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "查询成功");
            returnMap.put("msg", relMap);
            return returnMap;

        } catch (Exception e) {
            log.error("查询错误", e);
            Map<String, Object> returnMap = G.bmo.returnMap(false, "查询错误");
            return returnMap;
        }

    }
}
