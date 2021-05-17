package cn.edu.mju.ccce.dtrsystem.bmo;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.PassForgetRequestBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b><br>
 * <b>创建时间：</b>2020-04-28 15:38<br>
 */
public interface PassForgetRequestBmo {

    /**
     * 创建请求
     * @param inMap
     * @return
     */
    public Map<String,Object> issueForgetRequest(Map<String,Object> inMap);

    /**
     * 获取请求
     * @param
     * @return map key=reList
     */
    public Map<String,Object> getForgetRequestList();

    /**
     * 获取请求
     * @param
     * @return
     */
    public Map<String,Object> getForgetRequest(String ID);


    /**
     * 更新请求
     * @param inMap
     * @return
     */
    public Map<String,Object> upDataForgetRequest(Map<String,Object> inMap);

}
