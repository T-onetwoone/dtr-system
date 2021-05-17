package cn.edu.mju.ccce.dtrsystem.common;

import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.common.MapTool<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>Map获取工具类<br>
 * <b>创建时间：</b>2020-02-26 13:15<br>
 */
public class MapTool {

    /**
     * 获得Map指定key的value  String类型返回
     *
     * @param map
     * @param key
     * @return
     */
    public static String getString(Map map, Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return null;
    }

    /**
     * 获得Map中的Map
     *
     * @param map
     * @param key
     * @return
     */
    public static Map getMap(Map map, Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null && answer instanceof Map) {
                return (Map) answer;
            }
        }
        return null;
    }

    /**
     * 获得Map指定key的value  Object类型返回
     * @param map
     * @param key
     * @return
     */
    public static Object getObject(Map map, Object key) {
        return map != null ? map.get(key) : null;
    }

}
