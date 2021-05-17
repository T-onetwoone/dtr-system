package cn.edu.mju.ccce.dtrsystem.dao;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.AdminDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>管理员dao<br>
 * <b>创建时间：</b>2020-04-15 13:35<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.AdminDao")
public interface AdminDao {
    /**
     * 验证管理员登录信息
     * @param inMap
     * @return
     */
    public Map<String,Object> checkAdmin(Map<String,Object> inMap);

    /**
     * 查找管理员信息
     * @param adminID
     * @return
     */
    public Map<String,Object> selectAdmin(String adminID);
}
