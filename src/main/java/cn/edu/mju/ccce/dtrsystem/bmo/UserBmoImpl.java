package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.User;
import cn.edu.mju.ccce.dtrsystem.common.G;
import cn.edu.mju.ccce.dtrsystem.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.UserBmoImpl<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-08 20:50<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.bmo.UserBmoImpl")
public class UserBmoImpl implements UserBmo {

    /**
     * 日志输出.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("cn.edu.mju.ccce.dtrsystem.dao.UserDao")
    protected UserDao userDao;

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> userAdd(User user) throws DataAccessException {
        try {
            String userTypeName = user.getTYPE_NAME();
            int type = userDao.selectUserTypeID(userTypeName);
            user.setTYPE_ID(type);
            user.setUSER_PASS("123456");
            user.setCREAT_TIME(new Date());
            user.setUSER_STATUS(0);
            try {
                int i = userDao.createUser(user);
                if (i > 0) {
                    return G.bmo.returnMap(true, "ok");
                }
                return G.bmo.returnMap(false, "用户新建失败");
            } catch (Exception e) {
                return G.bmo.returnMap(false, "用户Nbr重复！");
            }
        } catch (Exception e) {
            log.error("用户新建异常", e);
            return G.bmo.returnMap(false, "用户新建异常");
        }
    }

    /**
     * 查找用户
     *
     * @param UserNbr
     * @return map key=user
     */
    @Override
    public Map<String, Object> selectUserByUserNbr(String UserNbr) {
        try {
            User user = new User();
            try {
                user = userDao.selectUser(UserNbr);
            } catch (NullPointerException e) {
                return G.bmo.returnMap(false, "用户查找为空");
            }
            if (user == null) {
                return G.bmo.returnMap(false, "用户查找为空");
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("user", user);
            return returnMap;
        } catch (Exception e) {
            log.error("用户查找异常", e);
            return G.bmo.returnMap(false, "用户查找异常");
        }
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Override
    public Map<String, Object> upDataUser(User user) {
        try {
            user.setUPDATE_TIME(new Date());
            int i = userDao.upDataUser(user);
            if (i > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "用户更新失败");
        } catch (Exception e) {
            log.error("用户更新异常", e);
            return G.bmo.returnMap(false, "用户更新异常");
        }
    }

    /**
     * 获取所有的用户
     *
     * @param
     * @return map key=stuList key=teaList
     */
    @Override
    public Map<String, Object> getAllUser() {
        try{
            List<User> userList = userDao.getAllUser();
            List<User> stuList = new ArrayList<>();
            List<User> teaList = new ArrayList<>();
            for (User u:userList) {
                int ti = u.getTYPE_ID();
                switch (ti){
                    case 1:
                        stuList.add(u);
                        break;
                    case 2:
                        teaList.add(u);
                        break;
                    default:
                        break;
                }
            }
            Map<String, Object> returnMap = G.bmo.returnMap(true, "ok");
            returnMap.put("stuList",stuList);
            returnMap.put("teaList",teaList);
            return returnMap;
        }catch (Exception e){
            log.error("用户查找异常", e);
            return G.bmo.returnMap(false, "用户查找异常");
        }
    }

    @Override
    public Map<String, Object> registered(User user) {
        try {
            int i = userDao.createUser(user);
            if (i > 0) {
                return G.bmo.returnMap(true, "ok");
            }
            return G.bmo.returnMap(false, "用户注册失败");
        } catch (Exception e) {
            return G.bmo.returnMap(false, "用户Nbr重复！");
        }
    }
}
