package cn.edu.mju.ccce.dtrsystem.bmo;

import cn.edu.mju.ccce.dtrsystem.bean.User;

import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.UserBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-08 20:50<br>
 */
public interface UserBmo {

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    public Map<String,Object> userAdd (User user);

    /**
     * 查找用户
     *
     * @param UserNbr
     * @return map key=user
     */
    public Map<String,Object> selectUserByUserNbr (String  UserNbr);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public Map<String,Object> upDataUser (User user);

    /**
     * 获取所有的用户
     *
     * @param
     * @return map key=stuList key=teaList
     */
    public Map<String,Object> getAllUser ();

    /**
    * @author HS
    * @Description
    * @Date 16:15 2021/4/11
    * @Param
    * @return
    **/
    public Map<String,Object> registered(User user);

}
