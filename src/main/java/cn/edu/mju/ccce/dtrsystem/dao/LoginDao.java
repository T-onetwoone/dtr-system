package cn.edu.mju.ccce.dtrsystem.dao;

import cn.edu.mju.ccce.dtrsystem.bean.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.LoginDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-02-02 17:45<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.LoginDao")
public interface LoginDao {
    /**
     * 查找用户信息
     * @param inMap
     * @return
     */
    public Map<String,Object> selectUser (Map<String,Object> inMap);

    /**
     * 查找用户所有信息
     * @param userUbr
     * @return
     */
    public Map<String,Object> selectUserAllMsgByUserNbr(String userUbr);
}
