package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.User;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.common.IdGenerator;
import cn.edu.mju.ccce.dtrsystem.common.MapTool;
import cn.edu.mju.ccce.dtrsystem.dao.PassForgetRequestDao;
import cn.edu.mju.ccce.dtrsystem.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.PassForgetRequestBmoImpl<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-28 15:38<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.bmo.PassForgetRequestBmoImpl")
public class PassForgetRequestBmoImpl implements PassForgetRequestBmo {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.PassForgetRequestDao")
    protected PassForgetRequestDao requestDao;

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.UserDao")
    protected UserDao userDao;

    @Override
    public Map<String, Object> issueForgetRequest(Map<String, Object> inMap) {
        try {
            String nbr = MapTool.getString(inMap, "nbr");
            User user = new User();
            try {
                user = userDao.selectUser(nbr);
            } catch (Exception e) {
                //ignore
            }
            if (user == null) {
                return G.bmo.returnMap(false, "申请失败,账号不存在！");
            }
            Map<String,Object> resetMap = new HashMap<>();
            try{
                resetMap = requestDao.getRequestByUser(nbr);
            } catch (Exception e){
                //ignore
            }
            if (resetMap != null){
                return G.bmo.returnMap(false, "该账号已经申请，请等待管理员审批！");
            }
            Date nowDate = new Date();
            Long id = IdGenerator.genLongId();
            String name = MapTool.getString(inMap, "name");
            String det = MapTool.getString(inMap, "detail");
            String iphone = MapTool.getString(inMap, "iphone");
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("REQUEST_ID", id);
            reMap.put("REQUEST_USER_NAME", name);
            reMap.put("REQUEST_USER_NBR", nbr);
            reMap.put("REQUEST_USER_IPHONE", iphone);
            reMap.put("REQUEST_DETAIL", det);
            reMap.put("REQUEST_STATUS", 0);
            reMap.put("CREAT_TIME", nowDate);
            reMap.put("UPDATE_TIME", nowDate);
            int i = requestDao.createRequest(reMap);
            if (i > 0) {
                return G.bmo.returnMap(true, String.valueOf(id));
            }
            return G.bmo.returnMap(false, "申请失败");
        } catch (Exception e) {
            log.error("申请异常", e);
            return G.bmo.returnMap(false, "申请异常");
        }
    }

    @Override
    public Map<String, Object> getForgetRequestList() {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            try {
                list = requestDao.getRequestList();
            } catch (Exception e) {
                //ignore
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("reList", list);
            return returnMap;
        } catch (Exception e) {
            log.error("查找异常", e);
            return G.bmo.returnMap(false, "查找异常");
        }
    }

    @Override
    public Map<String, Object> getForgetRequest(String ID) {
        try {
            Map<String, Object> map = new HashMap<>();
            try {
                map = requestDao.getRequest(ID);
            } catch (Exception e) {
                //ignore
            }
            if (map.isEmpty()){
                return G.bmo.returnMap(false, "查找为空");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("reList", map);
            return returnMap;
        } catch (Exception e) {
            log.error("查找异常", e);
            return G.bmo.returnMap(false, "查找异常");
        }
    }

    @Override
    public Map<String, Object> upDataForgetRequest(Map<String, Object> inMap) {
        try {
            String id = MapTool.getString(inMap, "id");
            Map<String, Object> map = new HashMap<>();
            try {
                map = requestDao.getRequest(id);
            } catch (Exception e) {
                //ignore
            }
            if (map == null) {
                return G.bmo.returnMap(false, "修改失败");
            }
            String status = MapTool.getString(inMap, "status");
            if ("1".equals(status)) {
                String request_user_nbr = MapTool.getString(map, "REQUEST_USER_NBR");
                User user = userDao.selectUser(request_user_nbr);
                user.setUSER_PASS("123456");
                int k = userDao.upDataUser(user);
                if (k < 0) {
                    return G.bmo.returnMap(false, "修改失败");
                }
            }
            map.put("REQUEST_STATUS", status);
            int i = requestDao.upDataRequest(map);
            if (i > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "修改失败");
        } catch (Exception e) {
            log.error("修改失败异常", e);
            return G.bmo.returnMap(false, "修改失败异常");
        }
    }
}
