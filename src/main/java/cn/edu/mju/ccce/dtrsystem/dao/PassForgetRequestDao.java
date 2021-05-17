package cn.edu.mju.ccce.dtrsystem.dao;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.dao.PassForgetRequestDao<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-23 15:21<br>
 */
@Service("cn.edu.mju.ccce.dtrsystem.dao.PassForgetRequestDao")
public interface PassForgetRequestDao {

    /**
     * 创建申请
     *
     * @param inMap
     * @return
     */
    public int createRequest(Map<String,Object> inMap);

    /**
     * 获取申请
     *
     * @return
     */
    public Map<String,Object> getRequest(String REQUEST_ID);

    /**
     * 获取申请
     *
     * @return
     */
    public Map<String,Object> getRequestByUser(String UserNbr);

    /**
     * 获取申请列表
     *
     * @return
     */
    public List<Map<String,Object>> getRequestList();

    /**
     * 更新申请
     * @param inMap
     * @return
     */
    public int upDataRequest (Map<String,Object> inMap);
}
