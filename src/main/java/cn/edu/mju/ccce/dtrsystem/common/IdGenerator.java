package cn.edu.mju.ccce.dtrsystem.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.common.IdGenerator<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>mySQL-id生成<br>
 * <b>创建时间：</b>2020-02-18 21:34<br>
 */
public class IdGenerator {

    public static Long genLongId() {
        String timestamp = getTimestamp();
        String randomNumber = getRandomNumber(4);//js精度最多17位 超出位数都为0  改成16位
        String string = timestamp + randomNumber;
        return Long.valueOf(string);
    }

    /**
     * . 生成随机码
     *
     * @param m 位数
     * @return String
     */
    public static String getRandomNumber(int m) {
        Random rd = new Random();
        int number = 1;
        for (int i = 0; i < m; i++) {
            number *= 10;
        }
        String string = rd.nextInt(number) + String.valueOf(rd.nextInt(number)) + number;
        return string.substring(0, m);
    }

    /**
     * 获得格式化的时间戳
     *
     * @return
     */
    public static String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(Calendar.getInstance().getTime());
    }
}

