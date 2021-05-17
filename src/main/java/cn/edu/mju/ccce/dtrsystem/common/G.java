package cn.edu.mju.ccce.dtrsystem.common;

import cn.edu.mju.ccce.dtrsystem.util.ObjectUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.common.G<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>交互数据格式定义<br>
 * <b>创建时间：</b>2020-02-02 17:54<br>
 */
public class G {
    /**
     * 与页面交互的数据格式定义
     */
    public static class page {
        /**
         * 生成一个map，适用于在controller与页面之间约定返回的JSON-map值
         *
         * @param code 返回code
         * @param msg  返回msg
         * @return 返回给UI页面page的数据
         */
        public static Map<String, Object> returnMap(String code, String msg) {
            Map<String, Object> healthMap = new HashMap<String, Object>();
            Map<String, Object> returnMap = new HashMap<String, Object>();
            healthMap.put("code", code); // controller与页面之间约定的字段
            healthMap.put("message", msg); // controller与页面之间约定的字段
            returnMap.put("health", healthMap); // controller与页面之间约定的字段
            return returnMap;
        }

        /**
         * 生成一个map，适用于在controller与页面之间约定返回的JSON-map值
         *
         * @param success 返回成功/失败
         * @param msg     返回msg
         * @return 返回给UI页面page的数据
         */
        public static Map<String, Object> returnMap(boolean success, String msg) {
            return returnMap(success ? "0" : "1", msg);
        }

        /**
         * 读取returnMap中的health返回的code
         *
         * @param returnMap
         * @return
         */
        public static String returnMapCode(Map<String, Object> returnMap) {
            Object o = json.get(returnMap, "$.health.code");
            o = o == null ? "" : o;
            String code = o instanceof String ? (String) o : String.valueOf(o);
            return code;
        }

        /**
         * 读取returnMap中的health返回的code是否等于给定的值
         *
         * @param returnMap
         * @param testCode
         * @return
         */
        public static boolean returnMapCodeEquals(Map<String, Object> returnMap, String testCode) {
            return ObjectUtils.equals(returnMapCode(returnMap), testCode);
        }

        /**
         * 读取returnMap中的health返回的code是否成功
         *
         * @param returnMap
         * @return
         */
        public static boolean returnMapBool(Map<String, Object> returnMap) {
            return "0".equals(returnMapCode(returnMap));
        }

        /**
         * 读取returnMap中的health返回的message
         *
         * @param returnMap
         * @return
         */
        public static String returnMapMsg(Map<String, Object> returnMap) {
            Object o = json.get(returnMap, "$.health.message");
            o = o == null ? "" : o;
            String msg = o instanceof String ? (String) o : String.valueOf(o);
            return msg;
        }

    }

    /**
     * 与bmo交互的数据格式定义
     */
    public static class bmo {

        /**
         * 生成一个map，适用于在controller与bmo之间约定返回的JSON-map值
         *
         * @param code 返回code
         * @param msg  返回msg
         * @return
         */
        public static Map<String, Object> returnMap(String code, String msg) {
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("resultCode", code);
            returnMap.put("resultMsg", msg);
            return returnMap;
        }

        /**
         * 生成一个map，适用于在controller与bmo之间约定返回的JSON-map值
         *
         * @param success 返回成功/失败
         * @param msg     返回msg
         * @return
         */
        public static Map<String, Object> returnMap(boolean success, String msg) {
            return returnMap(success ? "0" : "1", msg);
        }

        /**
         * 读取returnMap中的返回的【结果编码状态】
         *
         * @param returnMap
         * @return
         */
        public static String returnMapCode(Map<String, Object> returnMap) {
            Object o = json.get(returnMap, "$." + "resultMsg");
            o = o == null ? "" : o;
            String code = o instanceof String ? (String) o : String.valueOf(o);
            return code;
        }

        /**
         * 读取returnMap中的返回的【结果编码状态】 是否等于给定的值
         *
         * @param returnMap
         * @param testCode
         * @return
         */
        public static boolean returnMapCodeEquals(Map<String, Object> returnMap, String testCode) {
            return ObjectUtils.equals(returnMapCode(returnMap), testCode);
        }

        /**
         * 读取returnMap中的返回的【结果编码状态】 是否成功
         *
         * @param returnMap
         * @return
         */
        public static boolean returnMapBool(Map<String, Object> returnMap) {
            return "0".equals(MapTool.getString(returnMap,"resultCode"));
        }

        /**
         * 读取returnMap中的返回的【结果转态描述】 描述信息
         *
         * @param returnMap
         * @return
         */
        public static String returnMapMsg(Map<String, Object> returnMap) {
            Object o = json.get(returnMap, "$." + "resultMsg");
            o = o == null ? "" : o;
            String msg = o instanceof String ? (String) o : String.valueOf(o);
            return msg;
        }

        /**
         * 读取returnMap中的返回的【业务对象】对象中指定路径的值，缺省默认值为null
         *
         * @param returnMap
         * @param path      描述路径
         * @param <T>
         * @return
         */
        public static <T> T bizResultValue(Map<String, Object> returnMap, String path) {
            return bizResultValue(returnMap, path, null);
        }

        /**
         * 读取returnMap中的返回的【业务对象】对象中指定路径的值，缺省默认值为null=defaultValue
         *
         * @param returnMap
         * @param path         描述路径
         * @param defaultValue 默认值
         * @param <T>
         * @return
         */
        public static <T> T bizResultValue(Map<String, Object> returnMap, String path, T defaultValue) {
            return json.get(returnMap, path, defaultValue);
        }

        /**
         * 读取returnMap中的返回的【业务对象】对象中指定路径的值，是否等于给定的值
         *
         * @param returnMap
         * @param path
         * @param testValue
         * @return
         */
        public static boolean bizResultValueEquals(Map<String, Object> returnMap, String path, Object testValue) {
            return ObjectUtils.equals(bizResultValue(returnMap, path), testValue);
        }

        /**
         * bmo数据格式-->转换为-->page页面的交互数据格式
         *
         * @param bmoMap
         * @return
         */
        public static Map<String, Object> parsePageReturnMap(Map<String, Object> bmoMap) {
            Map<String, Object> clone = json.cloneAsJson2Object(bmoMap);
            boolean success = bmo.returnMapBool(clone);
            String messages = bmo.returnMapMsg(clone);
            clone.putAll(page.returnMap(success, messages));
            return clone;
        }
    }

    /**
     * json-path取值相关
     */
    public static final class json {

        /**
         * 在给定的根对象下，通过指定路径下的获取值
         * <p color=red>引用可能类型转换异常ClassCastException，调用时候，必须明确返回类型</p>
         *
         * @param rootObject   根对象
         * @param path         路径
         * @param defaultValue 当返回值为null时，指定默认值
         * @param <T>          返回类型
         * @return
         */
        public static final <T> T get(Object rootObject, String path, T defaultValue) {
            T result = (T) JSONPath.eval(rootObject, path);
            result = result != null ? result : defaultValue;
            return result;
        }

        /**
         * 在给定的根对象下，通过指定路径下的获取值
         * <p color=red>引用可能类型转换异常ClassCastException，调用时候，必须明确返回类型</p>
         *
         * @param rootObject 根对象
         * @param path       路径
         * @param <T>        返回类型
         * @return
         */
        public static final <T> T get(Object rootObject, String path) {
            return get(rootObject, path, null);
        }

        /**
         * 迭代指定根对象的一系列path动作，对每一个值进行逐个处理
         *
         * @param root    根对象
         * @param handler 对每一个值进行逐个处理的回调函数
         * @param paths   path中使用[i]来实现目标列表迭代
         */
        public static void handleJSONPath(Object root,
                                          JSONPathValueHandler handler, String... paths) {
            if (root == null || paths == null || paths.length == 0
                    || handler == null) {
                return;
            }
            for (String path : paths) {
                handleJSONPath0(root, handler, path);
            }
        }

        /**
         * 迭代指定根对象的一系列path动作，对每一个值进行逐个处理--处理结果为list
         *
         * @param root
         * @param paths
         */
        public static void handleJSONPathAsList(Object root, String... paths) {
            if (root == null || paths == null || paths.length == 0) {
                return;
            }
            JSONPathValueHandler handler = new json.JSONPathValueHandler() {
                @Override
                public void handle(JSONPath jsonpath, Object root, Object current) {
                    if (current != null) {
                        List<Object> list = null;
                        if (current instanceof Map) {
                            if (root instanceof JSON) {
                                list = new JSONArray();
                            } else {
                                list = new ArrayList<>();
                            }
                            list.add(current);
                        }
                        if (list != null) {
                            jsonpath.set(root, list); // 设置列表
                        }
                    }
                }
            };
            for (String path : paths) {
                handleJSONPath0(root, handler, path);
            }
        }

        private static void handleJSONPath0(Object root,
                                            JSONPathValueHandler handler, String path) {
            String flag = "[i]", formatIndex = "[%d]";
            int index = path == null ? -1 : path.indexOf(flag);
            if (index > -1) {
                String prefix = path.substring(0, index);
                String suffix = "$" + path.substring(index + flag.length());
                int size = 0;
                try {
                    size = JSONPath.size(root, prefix);
                } catch (Exception ignore) {
                }
                for (int i = 0; i < size; i++) {
                    String iPath = prefix + String.format(formatIndex, i);
                    Object iObject = null;
                    try {
                        iObject = JSONPath.eval(root, iPath);
                    } catch (Exception ignore) {
                    }
                    if (iObject != null) {
                        handleJSONPath0(iObject, handler, suffix);
                    }
                }
            } else {
                JSONPath jsonpath = null;
                try {
                    jsonpath = JSONPath.compile(path);
                } catch (Exception ignore) {
                }
                if (jsonpath != null) {
                    Object o = null;
                    try {
                        o = jsonpath.eval(root);
                    } catch (Exception ignore) {
                    }
                    handler.handle(jsonpath, root, o);
                }
            }
        }

        /**
         * 按json字符串格式标准来复制一个对象
         *
         * @param object
         * @return
         */
        @SuppressWarnings("unchecked")
        public static <T> T cloneAsJson2Object(T object) {
            String jsonString = JSON.toJSONString(object);
            T cloneObject = (T) JSON.parse(jsonString);
            return cloneObject;
        }

        /**
         * 判断指定路径下的值，是否等于给定的值
         *
         * @param map
         * @param path
         * @param testValue
         * @return
         */
        public static boolean bizValueEquals(Map<String, Object> map, String path, Object testValue) {
            return ObjectUtils.equals(get(map, path), testValue);
        }

        /**
         * json路径处理回调接口
         */
        public static interface JSONPathValueHandler {
            void handle(JSONPath jsonpath, Object root, Object current);
        }
    }

}
