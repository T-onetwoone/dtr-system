package cn.edu.mju.ccce.dtrsystem.bmo;

import java.io.InputStream;
import java.util.Map;

/**
 * <b>项目名称：</b>dtr-system<br>
 * <b>类名称：</b>cn.edu.mju.ccce.dtrsystem.bmo.FileBmo<br>
 * <b>创建人：</b>yuks<br>
 * <b>类描述：</b>文件bmo<br>
 * <b>创建时间：</b>2020-04-07 09:56<br>
 */
public interface FileBmo {

    public Map<String,Object> parseExcelFileByUser(InputStream excelFile, String fileName);
}
