package cn.edu.mju.ccce.dtrsystem.dao;

import cn.edu.mju.ccce.dtrsystem.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.UserDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-07 10:46<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.UserDao")
public interface UserDao {

    /**
     * 创建用户
     * @param user
     * @return
     */
    public int createUser(User user);

    /**
     * 查找用户类别ID
     * @param typeName
     * @return
     */
    public int selectUserTypeID(String typeName);

    /**
     * 查找用户
     * @param userNbr
     * @return
     */
    public User selectUser(String userNbr);

    /**
     * 更新用户
     * @param user
     * @return
     */
    public int upDataUser(User user);

    /**
     * 获取所有的用户列表
     * @return
     */
    public List<User> getAllUser();

}
